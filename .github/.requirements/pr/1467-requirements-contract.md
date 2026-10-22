# chore(requirements): move requirements to .requirements/ and adopt Loopy's frontmatter contract

## Summary

`.github/.requirements/` moves to `.requirements/`, as herodotus and Loopy have it. The skills that name the old path follow it. The live requirements files then get frontmatter that follows [Loopy](https://github.com/athrane/loopy)'s contract: the dev-4.1 map, its tickets and the PR files #1440–#1467. A one-off script does it from a dry run that the user reviews. After it, `loopy check`, `loopy ready`, `loopy generate`, merge sync and `loopy serve`'s map view work on bassebombecraft.

## Motivation

Loopy runs and shows a human + LLM development loop over a target repository. Its first three steps work on Loopy's own repo and are being adopted by herodotus:
- a contract check with generated map tables and a ready list;
- merge sync, which marks a merged PR's file `merged` with no session involved;
- a local web UI showing each map as a graph, the priority queue and lineage.

Bassebombecraft gets them once its files carry the contract. Two of Loopy's map 2 tickets decided how:

- **`bassebombecraft-flow-switch` (D3).** Requirements move to `.requirements/`, in one PR after the `dev-4.1` merge (#1466), together with the path strings in the skills.
- **`bassebombecraft-migration`.** The contract adoption rides in that same PR, as commits after a pure `git mv`. Its decisions:
  - **Scope: map-reachable files.** That is the dev-4.1 map, its tickets, and the live PR files #1440–#1467. The 17 files from the 1.17.1 attempt (#1337–#1432) keep no frontmatter, and Loopy counts them as skipped. All six stems that are not valid Loopy ids are among them, so nothing is renamed.
  - **Method:** herodotus's (#1644 there). A one-off script with `gh` writes a dry run for the user, the user's rulings go into a decisions file, and the script is rerun from current `master` just before merge.
  - **The dev-4.1 items are read from `gh` as `merged`,** because #1466 has closed them. None gets `pull`, because #1466 closed many items at once and `pull` must be unique.

`.loopy.json` sets `stateCommitSuffix: "[skip ci]"`. Once #1465 is in, every push to `master` builds, containerises and tests. Without the suffix, each of Loopy's merge-sync commits would do the same.

### What this PR does not do

- **The validate entry point and the loop skills** (flow-switch D4) are a separate PR.
- **Write in-flight states (`implemented`, `reviewed`).** A PR stays `planned` on `master` until merge sync marks it `merged`.
- **Migrate the 1.17.1 history,** or rename any file.
- **Change Java, Gradle or CI.**

## Field Mapping

These are the script's rules. The authority is Loopy's schema in `src/item/schema/` (athrane/loopy). Its key set is closed: an unknown key is a load error.

**Tickets** (`.requirements/wayfinding/wayfinding-1-tickets/*.md`):
- `id` = the file stem, `kind: ticket`, `map: wayfinding-1-dev-4-1-completion`.
- `type` comes from `**Type**`. `task` becomes `task-hitl` or `task-afk` by the user's ruling; there are four (`client-smoke-test`, `compile-baseline`, `jdk-on-path`, `server-smoke-test`).
- `status` comes from `**Status**`.
- `statusSince`: for resolved tickets, the date in the map's Decisions So Far row; for open tickets, the date the map was committed (#1466 Phase 1).
- `blockedBy` holds only live blockers.
- **Body.** The three header lines are removed. The original Blocked By text moves to a `**Blocked by, when charted:**` line under the H1. A closed ticket's Resolution opens with a `**Gist**:` paragraph from its map row.

**PR files** (`.requirements/pr/1440-…` to `1467-…`, 16 + 3 files):
- `id` = the stem, `kind: pr`, `issue` = the first `Closes #N` in `## Related Issues` (it matches the stem here).
- **`status` from `gh`:**
  - an issue closed by #1466 → `merged`, `statusSince` = #1466's merge date, no `pull`;
  - an issue closed by its own pull request (#1465) → `merged` with `pull`;
  - an open issue (#1454, and this file until it merges) → `planned`, `statusSince` = the date the file was committed;
  - **#1464, closed by hand with no pull request,** goes to the user. The expected ruling is `merged`, with #1466's merge date.
- `map: wayfinding-1-dev-4-1-completion` on #1454; and on #1466 and #1467, which the flow switch scheduled.

**The map** (`wayfinding-1-dev-4-1-completion.md`):
- frontmatter `id` (its stem) and `kind: map`;
- its Decisions So Far and Tickets tables become `decisions` and `tickets` regions;
- a new Graduated section holds a `graduated` region;
- Waves holds a `waves` region.

No queue file is written: the map keeps no hand-made order.

## Changes

### Files Deleted

- **Delete `migrate-loopy-contract.mjs`** and **`loopy-migration-decisions.json`** — in the last commit, once the final rerun is in.

### Files Updated

- **`.github/.requirements/` → `.requirements/`** — Moved with `git mv`, in a commit of its own so history follows every file.
- **Path strings `.github/.requirements` → `.requirements`** in the files that name the old path:
  - `CLAUDE.md` and `.claude/rules/skills.md`;
  - the skills `00-00-create-pr` (`SKILL.md`, `references/examples.md`), `00-01-implement-pr`, `00-05-wayfinder` (`SKILL.md`, `references/examples.md`, `templates/map-template.md`), `00-06-sync-issue-description` (`SKILL.md`, `references/examples.md`, `scripts/sync-issue.mjs`), `create-pr-from-build-errors` and `forge-upgrade-pr`.

  Requirements files' bodies are not edited, even where they name the old path.
- **`.loopy.json`** — New. `{ "schemaVersion": 1, "files": [".requirements/**/*.md"], "stateCommitSuffix": "[skip ci]" }`.
- **`.claude/skills/00-00-create-pr/templates/pr-template.md`** and **`pr-template-generic.md`** — The frontmatter block before the H1 (`id`, `kind: pr`, `status: planned`, `statusSince`, `graduatedFrom` or `map`, `issue`), as in Loopy's template.
- **`.claude/skills/00-00-create-pr/SKILL.md`** — Step 6 starts the file with the frontmatter block and runs `loopy check`. The number is still the issue number, as today.
- **`.claude/skills/00-05-wayfinder/SKILL.md`**, **`templates/map-template.md`**, **`templates/ticket-template.md`** — State in frontmatter, `**Gist**:` on close, map tables as generated regions refreshed with `loopy generate --write`, and `loopy ready`. These are the same sections as Loopy's adapted copy.
- **`.claude/skills/00-06-sync-issue-description/scripts/sync-issue.mjs`** — Strips a leading frontmatter block before building the issue body.
- **`migrate-loopy-contract.mjs`** — New and temporary. A dry run by default, and `--write` to apply. It implements the Field Mapping above. It refuses to write if a body changes beyond the moved header lines, or if a reference dangles.
- **`loopy-migration-decisions.json`** — New and temporary. The user's rulings: the four `task` types, and #1464.
- **The dev-4.1 map, its 13 tickets and 19 PR files** — Frontmatter, region markers and gists, as in Field Mapping.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Move

1. Branch `athrane/issue1467` from `master`, after #1466 has merged.
2. `git mv .github/.requirements .requirements`. Commit it alone.

### Phase 2 — Paths and contract tooling

1. Replace the path strings (see Changes).
2. Write `.loopy.json`.
3. Switch the templates and the `00-00`, `00-05` and `00-06` skills to frontmatter.
4. Commit.

### Phase 3 — Dry run and rulings

1. Write the script and run it with no flag. Give the report to the user. It shows every file's frontmatter, the skipped history and the undecided cases.
2. Record the rulings in the decisions file. Repeat until nothing is undecided.

### Phase 4 — Migrate

1. Run with `--write`.
2. From a Loopy checkout beside bassebombecraft:
   - `node ../loopy/dist/cli.js generate --target . --write`;
   - `node ../loopy/dist/cli.js check --target .` until it reports `0 issues`.
3. Commit.

### Phase 5 — Rerun at merge

1. Rebase on `master`, then drop the Phase 4 commit and run Phases 3 and 4 again. This picks up dev-4.1 map work committed in the meantime.
2. Last commit: delete the script and the decisions file.

## Testing

No Java or test changes. The script is checked by its self-checks, by Loopy's contract check, and by the user's review of the dry run.

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: unchanged.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | History follows the move | `git log --follow .requirements/pr/1440-registryobject-doesnt-exist.md` reaches back before this PR |
| 2 | No old path is left | `git grep -n ".github/.requirements"` matches only requirements files' bodies |
| 3 | The contract holds | `node ../loopy/dist/cli.js check --target .` reports `0 issues`: 1 map, 32 items (13 tickets, 19 PR files), and 17 skipped files |
| 4 | The dev-4.1 items are right | `node ../loopy/dist/cli.js ready --target .` lists the dev-4.1 map's ready tickets. #1440–#1461's files are `merged`, and #1454's is `planned` |
| 5 | Merge sync agrees | `node ../loopy/dist/cli.js sync --target .` (no `--write`) proposes no change |
| 6 | The map view shows bassebombecraft | `node ../loopy/dist/cli.js serve --target . --port 4702`, then open the URL it prints. The dev-4.1 map's graph shows its tickets and their blockers |
| 7 | The skills still work | Run `/00-00-create-pr` on a scratch problem: the file lands in `.requirements/pr/` and starts with frontmatter. Discard it |

## Related Issues
Closes #1467
Related to #1465, #1466

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes

- **Running Loopy before it has an install.** Build a Loopy checkout beside bassebombecraft once (`npm ci && npm run build` in `../loopy`). Then run `node ../loopy/dist/cli.js <command> --target .`. Use a port of its own for `serve` (4700 is Loopy's, 4701 herodotus's).
- **This file migrates too.** It is written without frontmatter, and Phase 4 gives it some.
- **Loopy's map 2 tracks this PR** as `bassebombecraft-adopts-contract`. The user resolves that ticket once this PR merges.
