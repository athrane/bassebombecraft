#!/usr/bin/env node

/**
 * Issue-description sync.
 *
 * Pushes a .github/.requirements/pr/*.md file into its GitHub issue: the body between the pr-sync markers,
 * the title (from the file's `# ` heading), and overflow comments when the body exceeds GitHub's
 * size limit. `--dry-run` prints a five-line summary with a plan hash; `--apply` rebuilds the plan
 * from a fresh fetch, refuses to write when `--expect` no longer matches, then writes and verifies.
 *
 * Requires Node.js 18+ and an authenticated GitHub CLI (`gh auth status`); run from the repository root
 * so `gh` resolves the repo from the git remote.
 *
 * Usage: node .claude/skills/00-06-sync-issue-description/scripts/sync-issue.mjs <pr-file> [--issue N] (--dry-run | --apply)
 *        [--no-title] [--expect <planHash>] [--allow-closed] [--replace-unmarked]
 *
 * `--replace-unmarked` drops a body that has no pr-sync markers instead of keeping it above the
 * appended block. Use it when the unmarked body is an earlier hand-pasted copy of the PR file.
 *
 * Exit codes: 0 ok, 1 gh/network or internal failure, 2 usage/ambiguous issue/missing file,
 * 3 plan-changed, 4 issue closed without --allow-closed.
 */

import { spawnSync } from 'child_process';
import { createHash } from 'crypto';
import { existsSync, mkdtempSync, readFileSync, rmSync, writeFileSync } from 'fs';
import { tmpdir } from 'os';
import { join, relative, resolve } from 'path';
import { pathToFileURL } from 'url';

/** GitHub's documented cap on issue and comment body length, in characters. */
export const BODY_LIMIT = 65536;

/** Characters reserved per chunk for markers, headers and notices. */
export const HEADROOM = 300;

/** Below this many characters of body budget, all PR content goes to overflow comments. */
const MIN_BODY_BUDGET = 1000;

/** Opens the synced block in the issue body; it must stand alone on its line. */
export const START_MARKER = '<!-- pr-sync:start -->';

/** Closes the synced block in the issue body; it must stand alone on its line. */
export const END_MARKER = '<!-- pr-sync:end -->';

/** Matches an overflow comment's own marker, which must open the comment. */
const OVERFLOW_MARKER_RE = /^\s*<!-- pr-sync:overflow:(\d+) -->/;

/** Body of an overflow comment that a shorter, later sync no longer needs. */
const STUB_TEXT = '_Superseded — no longer part of the current sync. See the issue body and earlier comments for the current content._';

/** Number of hex characters of the SHA-256 shown as the plan hash. */
const PLAN_HASH_LENGTH = 8;

/** spawnSync output cap; comment listings of long issues exceed Node's 1 MB default. */
const MAX_BUFFER_BYTES = 64 * 1024 * 1024;

/** A leading byte-order mark, written as an escape so the source has no invisible character. */
const BOM_RE = /^﻿/;

/** A heading line that may start a chunk: `##` or `###`, never `#` or `####`. */
const SECTION_HEADING_RE = /^ {0,3}#{2,3}\s/;

/** An opening or closing code fence; group 1 is the fence run. */
const FENCE_RE = /^ {0,3}(`{3,}|~{3,})/;

/** GitHub's rejection text for an oversized body or comment. */
const TOO_LONG_RE = /too long|maximum is \d+ characters/i;

/** Process exit codes, one per branch the driving skill handles differently. */
export const EXIT = Object.freeze({ OK: 0, GH: 1, USAGE: 2, PLAN_CHANGED: 3, CLOSED: 4 });

/** A failure with a stable error code and exit code. */
export class SyncError extends Error {
  /**
   * @param {string} code - Stable kebab-case error code.
   * @param {number} exitCode - Process exit code.
   * @param {string} message - One-line explanation.
   * @param {string[]} [details] - Extra stdout lines, such as candidate issues.
   */
  constructor(code, exitCode, message, details = []) {
    super(message);
    /** Stable kebab-case error code. */
    this.code = code;
    /** Process exit code. */
    this.exitCode = exitCode;
    /** Extra lines printed on stdout. */
    this.details = details;
  }
}

// ---------------------------------------------------------------------------
// Pure planning logic
// ---------------------------------------------------------------------------

/**
 * Normalizes line endings and strips a byte-order mark.
 * @param {string} text - Raw file text.
 * @returns {string}
 */
export function normalizeText(text) {
  return text.replace(BOM_RE, '').replace(/\r\n/g, '\n');
}

/**
 * Picks the target issue: `--issue` wins, then exactly one `Closes #N`, then exactly one `Related to #N`.
 * @param {string} prText - PR file text.
 * @param {number | null} cliIssue - Issue number from `--issue`, or null.
 * @returns {number}
 * @throws {SyncError} `ambiguous-issue` when zero or several candidates are found.
 */
export function resolveIssue(prText, cliIssue) {
  if (cliIssue !== null) return cliIssue;

  const section = extractSection(prText, 'Related Issues');
  const closes = uniqueNumbers(section, /\bCloses\s+#(\d+)/gi);
  if (closes.length === 1) return closes[0];

  const related = closes.length === 0 ? uniqueNumbers(section, /\bRelated to\s+#(\d+)/gi) : [];
  if (closes.length === 0 && related.length === 1) return related[0];

  const candidates = closes.length > 0 ? closes : related;
  throw new SyncError('ambiguous-issue', EXIT.USAGE,
    `found ${candidates.length} candidate issues in ## Related Issues; pass --issue N`,
    [`candidates: ${candidates.map((n) => `#${n}`).join(' ') || '(none)'}`]);
}

/**
 * Returns the text of a `## <name>` section, up to the next `## ` heading. Headings inside code
 * fences are ignored, so a PR file that quotes the PR template cannot shadow its real section.
 * @param {string} text - Markdown text.
 * @param {string} name - Section heading text.
 * @returns {string} Empty when the section is missing.
 */
export function extractSection(text, name) {
  const lines = scanLines(text);
  const isHeading = (line) => !line.inFence && /^## /.test(line.text);
  const start = lines.findIndex((line) => isHeading(line) && line.text.trim() === `## ${name}`);
  if (start === -1) return '';
  const end = lines.findIndex((line, i) => i > start && isHeading(line));
  return lines.slice(start + 1, end === -1 ? undefined : end).map((line) => line.text).join('\n');
}

/**
 * Collects the distinct numbers captured by a global regex, in first-seen order.
 * @param {string} text - Text to search.
 * @param {RegExp} regex - Global regex whose group 1 is a number.
 * @returns {number[]}
 */
function uniqueNumbers(text, regex) {
  return [...new Set([...text.matchAll(regex)].map((m) => Number(m[1])))];
}

/**
 * Returns the first `# ` heading outside a code fence, or null.
 * @param {string} prText - PR file text.
 * @returns {string | null}
 */
export function extractTitle(prText) {
  const heading = scanLines(prText).find((line) => !line.inFence && /^# \S/.test(line.text));
  return heading ? heading.text.slice(2).trim() : null;
}

/**
 * Splits text into lines tagged with their start offset and whether they sit inside a code fence.
 * Fence lines themselves count as inside. A fence closes only on the same character with a run at
 * least as long and nothing else on the line.
 * @param {string} text - Markdown text.
 * @returns {{ text: string, offset: number, inFence: boolean }[]}
 */
function scanLines(text) {
  const result = [];
  let offset = 0;
  let fence = null;
  for (const line of text.split('\n')) {
    const match = FENCE_RE.exec(line);
    const opensOrCloses = match !== null;
    if (fence === null) {
      result.push({ text: line, offset, inFence: opensOrCloses });
      if (opensOrCloses) fence = match[1];
    } else {
      result.push({ text: line, offset, inFence: true });
      const closes = opensOrCloses && match[1][0] === fence[0] && match[1].length >= fence.length
        && line.trim() === match[1];
      if (closes) fence = null;
    }
    offset += line.length + 1;
  }
  return result;
}

/**
 * Finds the offset to cut text at so the head fits the budget. Prefers the last `##`/`###` heading
 * outside a fence; falls back to the last blank line outside a fence, then to the last line start
 * (inside a fence only when a single fence is larger than the budget), then to a hard cut.
 * @param {string} text - Text to cut.
 * @param {number} budget - Maximum head length.
 * @returns {number} Cut offset; `text.length` when everything fits.
 */
export function findCut(text, budget) {
  if (text.length <= budget) return text.length;

  const fitting = scanLines(text).filter((line) => line.offset > 0 && line.offset <= budget);
  const outside = fitting.filter((line) => !line.inFence);
  const heading = outside.filter((line) => SECTION_HEADING_RE.test(line.text)).pop();
  if (heading) return heading.offset;

  const blank = outside.filter((line) => line.text.trim() === '').pop();
  if (blank) return blank.offset;

  const lineStart = (outside.pop() ?? fitting.pop());
  return lineStart ? lineStart.offset : budget;
}

/**
 * Splits text into chunks of at most `budget` characters, cutting at section boundaries.
 * @param {string} text - Text to split.
 * @param {number} budget - Maximum chunk length.
 * @returns {string[]} Chunks with trailing whitespace trimmed; empty chunks dropped.
 */
export function splitAtSections(text, budget) {
  const chunks = [];
  let rest = text;
  while (rest.trim() !== '') {
    const cut = findCut(rest, budget);
    chunks.push(rest.slice(0, cut).trimEnd());
    rest = rest.slice(cut);
  }
  return chunks;
}

/**
 * Returns the body text outside the pr-sync markers, and whether the markers were found.
 * @param {string} body - Current issue body.
 * @param {boolean} [replaceUnmarked] - Drop a body without markers instead of keeping it.
 * @returns {{ before: string, after: string, hasMarkers: boolean }}
 * @throws {SyncError} `unpaired-marker` when `replaceUnmarked` is set and the body holds one
 *   marker without its pair: the body is not unmarked, and dropping it would lose the text
 *   around the surviving marker.
 */
export function splitBody(body, replaceUnmarked = false) {
  const starts = markerLineOffsets(body, START_MARKER);
  const ends = markerLineOffsets(body, END_MARKER);
  const start = starts[0] ?? -1;
  const end = ends.filter((offset) => offset > start).pop() ?? -1;
  if (start !== -1 && end !== -1) {
    return { before: body.slice(0, start), after: body.slice(end + END_MARKER.length), hasMarkers: true };
  }
  if (replaceUnmarked && (starts.length > 0 || ends.length > 0)) {
    throw new SyncError('unpaired-marker', EXIT.USAGE,
      'the issue body has a pr-sync marker without its pair; restore the pair by hand, or sync without --replace-unmarked');
  }
  if (body === '' || replaceUnmarked) return { before: '', after: '', hasMarkers: false };
  const separator = body.endsWith('\n\n') ? '' : body.endsWith('\n') ? '\n' : '\n\n';
  return { before: body + separator, after: '', hasMarkers: false };
}

/**
 * Returns the offsets of a marker that stands alone on its line. Markers quoted inline in the PR
 * text (for example in backticks) are ignored; the caller takes the first start and the last end,
 * so markers inside fenced examples in the synced content are skipped too.
 * @param {string} body - Issue body.
 * @param {string} marker - Marker text.
 * @returns {number[]}
 */
function markerLineOffsets(body, marker) {
  const offsets = [];
  let offset = 0;
  for (const line of body.split('\n')) {
    if (line.replace(/\r$/, '').trim() === marker) offsets.push(offset + line.indexOf(marker));
    offset += line.length + 1;
  }
  return offsets;
}

/**
 * Replaces the text between the markers, or appends a marked block after one blank line.
 * Text outside the markers is kept byte-for-byte.
 * @param {string} currentBody - Current issue body.
 * @param {string} content - Text to place between the markers.
 * @param {boolean} [replaceUnmarked] - Drop a body without markers instead of appending to it.
 * @returns {string}
 */
export function mergeBody(currentBody, content, replaceUnmarked = false) {
  const { before, after } = splitBody(currentBody, replaceUnmarked);
  return `${before}${START_MARKER}\n${content}\n${END_MARKER}${after}`;
}

/**
 * Builds the text of overflow comment `index` of `total`.
 * @param {number} index - 1-based part number.
 * @param {number} total - Number of parts.
 * @param {string} chunk - Chunk content.
 * @returns {string}
 */
export function overflowComment(index, total, chunk) {
  const header = `<!-- pr-sync:overflow:${index} -->\n_Continued from the issue body — part ${index} of ${total}_\n\n`;
  const trailer = index < total ? `\n\n_Continued in the next comment (part ${index + 1} of ${total})_` : '';
  return `${header}${chunk}${trailer}`;
}

/**
 * Builds the superseded stub for an overflow slot that is no longer needed.
 * @param {number} index - 1-based part number.
 * @returns {string}
 */
export function stubComment(index) {
  return `<!-- pr-sync:overflow:${index} -->\n${STUB_TEXT}`;
}

/**
 * Indexes existing overflow comments by part number, keeping the oldest per index.
 * @param {{ id: number, body: string, html_url: string }[]} comments - All issue comments, oldest first.
 * @returns {Map<number, { id: number, body: string, html_url: string }>}
 */
export function indexOverflowComments(comments) {
  const byIndex = new Map();
  for (const comment of comments) {
    const match = OVERFLOW_MARKER_RE.exec(comment.body ?? '');
    if (match && !byIndex.has(Number(match[1]))) byIndex.set(Number(match[1]), comment);
  }
  return byIndex;
}

/**
 * Computes everything --apply would write. Side-effect free.
 * @param {object} input - Plan input.
 * @param {{ number: number, title: string, body: string, state: string, url: string }} input.issue - Fetched issue.
 * @param {{ id: number, body: string, html_url: string }[]} input.comments - Fetched comments, oldest first.
 * @param {string} input.prText - Normalized PR file text.
 * @param {string} input.prPath - Repo-relative PR file path, used in the truncation notice.
 * @param {boolean} input.noTitle - Skip the title change.
 * @param {boolean} [input.replaceUnmarked] - Drop a body without markers instead of appending to it.
 * @param {number} [input.limit] - Body/comment size limit; lowered on the retry path.
 * @returns {object} Plan with title, body, overflow, stubs, summary facts and hash.
 */
export function buildPlan({ issue, comments, prText, prPath, noTitle, replaceUnmarked = false, limit = BODY_LIMIT }) {
  const content = prText.trimEnd();
  const notice = `> ⚠️ Truncated — continued in comments below. Full content: \`${prPath}\``;
  const { before, after, hasMarkers } = splitBody(issue.body ?? '', replaceUnmarked);
  const budget = limit - HEADROOM - before.length - after.length - notice.length;

  let bodyContent = content;
  let overflowText = '';
  let mode = 'fits';
  if (mergeBody(issue.body ?? '', content, replaceUnmarked).length > limit - HEADROOM) {
    const cut = budget >= MIN_BODY_BUDGET ? findCut(content, budget) : 0;
    mode = cut > 0 ? 'truncated' : 'no-room';
    bodyContent = cut > 0 ? `${content.slice(0, cut).trimEnd()}\n\n${notice}` : notice;
    overflowText = content.slice(cut);
  }

  const body = mergeBody(issue.body ?? '', bodyContent, replaceUnmarked);
  const chunks = splitAtSections(overflowText, limit - HEADROOM);
  const existing = indexOverflowComments(comments);
  const overflow = planOverflow(chunks, existing);
  const stubs = planStubs(chunks.length, existing);

  const heading = extractTitle(prText);
  const proposedTitle = heading !== null && heading !== issue.title ? heading : null;
  const firstOverflowLine = overflowText.trimStart().split('\n')[0];

  const plan = {
    issue: { number: issue.number, url: issue.url, state: issue.state, title: issue.title },
    proposedTitle,
    title: noTitle ? null : proposedTitle,
    titleSkipped: noTitle && proposedTitle !== null,
    body,
    bodyChanged: !sameText(issue.body ?? '', body),
    currentBodyLength: (issue.body ?? '').length,
    mode,
    hasMarkers,
    replacedUnmarked: !hasMarkers && replaceUnmarked && (issue.body ?? '') !== '',
    cutBefore: mode === 'fits' ? null : firstOverflowLine,
    overflow,
    stubs,
  };
  return { ...plan, hash: hashPlan(plan) };
}

/**
 * Plans one comment per overflow chunk, reusing the existing comment at the same index.
 * @param {string[]} chunks - Overflow chunks in order.
 * @param {Map<number, { id: number, body: string, html_url: string }>} existing - Overflow comments by index.
 * @returns {object[]} Planned comments with index, body, commentId, url and action.
 */
function planOverflow(chunks, existing) {
  return chunks.map((chunk, i) => {
    const index = i + 1;
    const text = overflowComment(index, chunks.length, chunk);
    const prior = existing.get(index);
    const action = !prior ? 'create' : sameText(prior.body, text) ? 'unchanged' : 'update';
    return { index, body: text, commentId: prior?.id ?? null, url: prior?.html_url ?? null, action };
  });
}

/**
 * Plans a superseded stub for every existing overflow comment past the last needed index.
 * @param {number} needed - Number of overflow chunks in the new plan.
 * @param {Map<number, { id: number, body: string, html_url: string }>} existing - Overflow comments by index.
 * @returns {object[]} Planned stubs, in index order.
 */
function planStubs(needed, existing) {
  return [...existing.entries()]
    .filter(([index]) => index > needed)
    .sort(([a], [b]) => a - b)
    .map(([index, prior]) => {
      const text = stubComment(index);
      const action = sameText(prior.body, text) ? 'unchanged' : 'update';
      return { index, body: text, commentId: prior.id, url: prior.html_url, action };
    });
}

/**
 * Compares two GitHub text bodies, ignoring line-ending style and trailing whitespace.
 * @param {string} a - First body.
 * @param {string} b - Second body.
 * @returns {boolean}
 */
function sameText(a, b) {
  return normalizeText(a).trimEnd() === normalizeText(b).trimEnd();
}

/**
 * Hashes only the planned payloads and actions, never fetched metadata such as timestamps.
 * The proposed title is hashed, not the applied one, so `--no-title` keeps the previewed hash:
 * the user approved one preview and only chooses whether its title change is applied.
 * @param {object} plan - Plan without its hash.
 * @returns {string} First 8 hex characters of a SHA-256.
 */
function hashPlan(plan) {
  const payload = JSON.stringify({
    issue: plan.issue.number,
    title: plan.proposedTitle,
    body: plan.body,
    bodyChanged: plan.bodyChanged,
    comments: [...plan.overflow, ...plan.stubs].map((c) => [c.index, c.commentId, c.action, c.body]),
  });
  return createHash('sha256').update(payload).digest('hex').slice(0, PLAN_HASH_LENGTH);
}

/**
 * Formats the compact dry-run summary.
 * @param {object} plan - Plan from buildPlan().
 * @returns {string}
 */
export function formatSummary(plan) {
  const count = (list, action) => list.filter((c) => c.action === action).length;
  const all = [...plan.overflow, ...plan.stubs];
  const unchanged = count(all, 'unchanged');
  const lines = [
    `issue:    #${plan.issue.number} ${plan.issue.state} ${plan.issue.url}`,
    `title:    ${formatTitle(plan)}`,
    `body:     ${formatBody(plan)}`,
    `overflow: ${count(plan.overflow, 'create')} create, ${count(plan.overflow, 'update')} update, `
      + `${count(plan.stubs, 'update')} stub${unchanged > 0 ? `, ${unchanged} unchanged` : ''}`,
    `plan:     ${plan.hash}`,
  ];
  return lines.join('\n');
}

/**
 * Formats the summary's title line: the change, a skipped change, or unchanged.
 * @param {object} plan - Plan from buildPlan().
 * @returns {string}
 */
function formatTitle(plan) {
  if (plan.title !== null) return `${JSON.stringify(plan.issue.title)} → ${JSON.stringify(plan.title)}`;
  if (plan.titleSkipped) return 'unchanged (--no-title)';
  return 'unchanged';
}

/**
 * Formats the summary's body line: old and new size, and how the content was placed.
 * @param {object} plan - Plan from buildPlan().
 * @returns {string}
 */
function formatBody(plan) {
  const sizes = `${formatNumber(plan.currentBodyLength)} → ${formatNumber(plan.body.length)} chars`;
  if (!plan.bodyChanged) return `${sizes} (unchanged)`;
  const markers = plan.hasMarkers ? 'markers replaced'
    : plan.replacedUnmarked ? 'unmarked body replaced' : 'markers appended';
  if (plan.mode === 'fits') return `${sizes} (fits; ${markers})`;
  if (plan.mode === 'no-room') return `${sizes} (no room; all content in overflow; ${markers})`;
  return `${sizes} (truncated before ${JSON.stringify(plan.cutBefore)}; ${markers})`;
}

/**
 * Formats a count for the summary.
 * @param {number} n - Number to format.
 * @returns {string} Number with thousands separators.
 */
function formatNumber(n) {
  return n.toLocaleString('en-US');
}

// ---------------------------------------------------------------------------
// gh I/O
// ---------------------------------------------------------------------------

/**
 * Runs gh without a shell, so arguments with spaces or quotes pass through intact.
 * @param {string[]} args - gh arguments.
 * @returns {string} Trimmed stdout.
 * @throws {SyncError} `payload-too-long` or `gh-failed`.
 */
function gh(args) {
  const result = spawnSync('gh', args, { encoding: 'utf8', maxBuffer: MAX_BUFFER_BYTES });
  if (result.error) throw new SyncError('gh-failed', EXIT.GH, `could not run gh: ${result.error.message}`);
  if (result.status === 0) return result.stdout.trim();
  const stderr = (result.stderr || result.stdout).trim();
  if (TOO_LONG_RE.test(stderr)) throw new SyncError('payload-too-long', EXIT.GH, firstLine(stderr));
  throw new SyncError('gh-failed', EXIT.GH, `gh ${args[0]} ${args[1] ?? ''}: ${firstLine(stderr)}`);
}

/**
 * Picks the line worth showing from gh's error output.
 * @param {string} text - Multi-line text.
 * @returns {string} First non-empty line.
 */
function firstLine(text) {
  return text.split('\n').find((line) => line.trim() !== '') ?? '(no output)';
}

/**
 * Fetches the issue and all of its comments, oldest first.
 * @param {number} number - Issue number.
 * @returns {{ issue: object, comments: object[] }}
 */
function fetchIssue(number) {
  const issue = { number, ...JSON.parse(gh(['issue', 'view', String(number), '--json', 'title,body,state,url'])) };
  const raw = gh(['api', `repos/{owner}/{repo}/issues/${number}/comments`, '--paginate',
    '--jq', '.[] | {id, body, html_url}']);
  const comments = raw === '' ? [] : raw.split('\n').map((line) => JSON.parse(line));
  return { issue, comments };
}

/**
 * Writes the plan to GitHub. Unchanged pieces are skipped. Temp files are removed afterwards.
 * @param {object} plan - Plan from buildPlan().
 * @returns {{ index: number, url: string, kind: string }[]} Written or kept comment URLs.
 */
function applyPlan(plan) {
  const dir = mkdtempSync(join(tmpdir(), 'sync-issue-'));
  try {
    const number = String(plan.issue.number);
    if (plan.bodyChanged || plan.title !== null) {
      const args = ['issue', 'edit', number];
      if (plan.bodyChanged) args.push('--body-file', writeTemp(dir, 'body.md', plan.body));
      if (plan.title !== null) args.push('--title', plan.title);
      withPayloadLength(plan.bodyChanged ? plan.body.length : 0, () => gh(args));
    }
    const urls = [];
    for (const comment of [...plan.overflow, ...plan.stubs]) {
      const kind = plan.stubs.includes(comment) ? 'stub' : 'part';
      urls.push({ index: comment.index, kind, url: writeComment(dir, number, comment) });
    }
    return urls;
  } finally {
    rmSync(dir, { recursive: true, force: true });
  }
}

/**
 * Creates, updates or keeps one overflow comment.
 * @param {string} dir - Temp directory.
 * @param {string} number - Issue number.
 * @param {object} comment - Planned comment.
 * @returns {string} Comment URL.
 */
function writeComment(dir, number, comment) {
  if (comment.action === 'unchanged') return comment.url;
  if (comment.action === 'create') {
    const file = writeTemp(dir, `part-${comment.index}.md`, comment.body);
    return withPayloadLength(comment.body.length, () => gh(['issue', 'comment', number, '--body-file', file]));
  }
  const input = writeTemp(dir, `part-${comment.index}.json`, JSON.stringify({ body: comment.body }));
  const path = `repos/{owner}/{repo}/issues/comments/${comment.commentId}`;
  withPayloadLength(comment.body.length, () => gh(['api', '-X', 'PATCH', path, '--input', input, '--silent']));
  return comment.url;
}

/**
 * Runs one gh write and, when GitHub rejects it as too long, records the rejected payload's
 * length on the error, so the retry can lower the limit below that payload.
 * @param {number} length - Length of the body or comment being written.
 * @param {() => string} write - The gh call.
 * @returns {string} The gh call's stdout.
 */
function withPayloadLength(length, write) {
  try {
    return write();
  } catch (error) {
    if (error instanceof SyncError && error.code === 'payload-too-long') error.payloadLength = length;
    throw error;
  }
}

/**
 * @param {string} dir - Temp directory.
 * @param {string} name - File name.
 * @param {string} text - File content.
 * @returns {string} Path written.
 */
function writeTemp(dir, name, text) {
  const path = join(dir, name);
  writeFileSync(path, text, 'utf8');
  return path;
}

/**
 * Re-fetches the issue and checks the title, body and every planned comment.
 * @param {object} plan - Applied plan.
 * @returns {boolean}
 */
function verify(plan) {
  const { issue, comments } = fetchIssue(plan.issue.number);
  const byIndex = indexOverflowComments(comments);
  const titleOk = plan.title === null || issue.title === plan.title;
  const commentsOk = [...plan.overflow, ...plan.stubs]
    .every((c) => byIndex.has(c.index) && sameText(byIndex.get(c.index).body, c.body));
  return titleOk && sameText(issue.body ?? '', plan.body) && commentsOk;
}

// ---------------------------------------------------------------------------
// CLI
// ---------------------------------------------------------------------------

/**
 * Parses command-line arguments.
 * @param {string[]} argv - Arguments after the script path.
 * @returns {{ file: string, issue: number | null, mode: string, noTitle: boolean, expect: string | null, allowClosed: boolean, replaceUnmarked: boolean }}
 * @throws {SyncError} `usage` on invalid arguments.
 */
export function parseArgs(argv) {
  const options = { file: null, issue: null, mode: null, noTitle: false, expect: null, allowClosed: false, replaceUnmarked: false };
  for (let i = 0; i < argv.length; i++) {
    const arg = argv[i];
    if (arg === '--dry-run' || arg === '--apply') {
      if (options.mode !== null) throw usage('pass exactly one of --dry-run and --apply');
      options.mode = arg.slice(2);
    } else if (arg === '--no-title') {
      options.noTitle = true;
    } else if (arg === '--allow-closed') {
      options.allowClosed = true;
    } else if (arg === '--replace-unmarked') {
      options.replaceUnmarked = true;
    } else if (arg === '--issue') {
      options.issue = Number(argv[++i]);
      if (!Number.isInteger(options.issue) || options.issue <= 0) throw usage('--issue needs a positive integer');
    } else if (arg === '--expect') {
      options.expect = argv[++i] ?? '';
      if (!/^[0-9a-f]{8}$/.test(options.expect)) throw usage('--expect needs an 8-character hex plan hash');
    } else if (arg.startsWith('--')) {
      throw usage(`unknown flag ${arg}`);
    } else if (options.file === null) {
      options.file = arg;
    } else {
      throw usage(`unexpected argument ${arg}`);
    }
  }
  if (options.file === null) throw usage('missing <pr-file>');
  if (options.mode === null) throw usage('pass --dry-run or --apply');
  return options;
}

/**
 * @param {string} message - What was wrong.
 * @returns {SyncError}
 */
function usage(message) {
  return new SyncError('usage', EXIT.USAGE,
    `${message}. Usage: sync-issue <pr-file> [--issue N] (--dry-run | --apply) [--no-title] [--expect <plan>] [--allow-closed] [--replace-unmarked]`);
}

/**
 * Loads the PR file and the issue, and builds the plan.
 * @param {object} options - Parsed arguments.
 * @param {number} [limit] - Size limit override for the retry path.
 * @returns {object} Plan.
 */
function loadPlan(options, limit = BODY_LIMIT) {
  const path = resolve(options.file);
  if (!existsSync(path)) throw new SyncError('file-not-found', EXIT.USAGE, `no such file: ${options.file}`);
  const prText = normalizeText(readFileSync(path, 'utf8'));
  const prPath = relative(process.cwd(), path).replace(/\\/g, '/');
  const number = resolveIssue(prText, options.issue);
  const { issue, comments } = fetchIssue(number);
  if (issue.state === 'CLOSED' && !options.allowClosed) {
    throw new SyncError('issue-closed', EXIT.CLOSED, `#${number} is CLOSED; pass --allow-closed to sync it anyway`);
  }
  return buildPlan({ issue, comments, prText, prPath, noTitle: options.noTitle,
    replaceUnmarked: options.replaceUnmarked, limit });
}

/**
 * Runs --apply: checks the plan hash, writes, retries once on a too-long rejection, then verifies.
 * @param {object} options - Parsed arguments.
 * @returns {string[]} Output lines.
 */
function runApply(options) {
  let plan = loadPlan(options);
  if (options.expect !== null && plan.hash !== options.expect) {
    throw new SyncError('plan-changed', EXIT.PLAN_CHANGED,
      `plan is now ${plan.hash}, expected ${options.expect}; the issue or file changed after the preview`);
  }

  const lines = [];
  let urls;
  try {
    urls = applyPlan(plan);
  } catch (error) {
    if (!(error instanceof SyncError) || error.code !== 'payload-too-long' || !error.payloadLength) throw error;
    const limit = error.payloadLength - 1;
    lines.push(`retried:  GitHub rejected a payload as too long; re-split with limit ${formatNumber(limit)}`);
    plan = loadPlan(options, limit);
    urls = applyPlan(plan);
  }

  lines.push(`issue:    ${plan.issue.url}`);
  lines.push(`title:    ${plan.title !== null ? 'changed' : 'unchanged'}`);
  lines.push(`body:     ${plan.bodyChanged ? 'updated' : 'unchanged'}`);
  for (const { index, kind, url } of urls) lines.push(`${kind === 'stub' ? 'stub' : 'part'} ${index}:   ${url}`);
  lines.push(`verified: ${verify(plan)}`);
  return lines;
}

/**
 * Entry point. Prints results on stdout and one `error: <code> — <message>` line on stderr.
 * @param {string[]} argv - Arguments after the script path.
 * @returns {number} Exit code.
 */
export function main(argv) {
  try {
    const options = parseArgs(argv);
    const output = options.mode === 'dry-run' ? formatSummary(loadPlan(options)) : runApply(options).join('\n');
    console.log(output);
    return EXIT.OK;
  } catch (error) {
    if (!(error instanceof SyncError)) {
      console.error(`error: internal — ${error instanceof Error ? error.message : String(error)}`);
      return EXIT.GH;
    }
    for (const line of error.details) console.log(line);
    console.error(`error: ${error.code} — ${error.message}`);
    return error.exitCode;
  }
}

if (process.argv[1] && import.meta.url === pathToFileURL(resolve(process.argv[1])).href) {
  process.exitCode = main(process.argv.slice(2));
}
