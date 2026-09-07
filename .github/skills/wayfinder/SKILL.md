---
name: wayfinder
description: 'Chart large, multi-session planning work as a shared map document with individually-resolvable decision tickets, keeping planning (resolving open questions) separate from execution (writing PR spec files). Use when a task has too many unresolved, interdependent design questions to fit in a single PR requirements file — e.g. a feature spanning several .github/.requirements/pr/ files whose open questions block each other, or when the user keeps surfacing new cross-cutting dependencies faster than they can be resolved.'
argument-hint: '[map-slug | "new" to chart a fresh map]'
---

# Wayfinder — Chart Large Multi-Session Planning Work

Adapts Matt Pocock's [wayfinder planning pattern](https://github.com/mattpocock/skills/blob/main/skills/engineering/wayfinder/SKILL.md) to BasseBombeCraft's file-based `.github/.requirements/` workflow. Instead of a GitHub-issue map with child ticket issues, the map and its tickets are markdown files under `.github/.requirements/wayfinding/`: a single **map** file plus a folder of small **ticket** files, each sized to resolve in one session.

The core principle carries over unchanged: **plan, don't do.** A ticket resolves one decision — it does not produce a PR spec. When the decision is resolved and its scope is small enough to implement, it graduates into a regular PR requirements file under [`.github/.requirements/pr/`](../../.requirements/pr/), produced by [`00-00-create-pr`](../00-00-create-pr/SKILL.md).

## When to Use This Skill

Load this skill when:

- A task is too large or too fogged-in for a single PR spec file — multiple interdependent, unresolved design questions across several candidate PRs.
- A planning conversation keeps surfacing new blocking dependencies faster than it resolves them (the signal that a single session isn't the right shape for the problem).
- The user asks to "chart a map," "wayfind," "scope this out across sessions," or references planning work that will span multiple `.github/.requirements/pr/` files.
- Several candidate PRs reference each other's open questions as blocking (e.g. "this PR depends on that PR's vocabulary" style cross-references).

**Do not use this skill** for work that fits one PR requirements file under [`.github/.requirements/pr/`](../../.requirements/pr/). Wayfinder's overhead (a map file, ticket files, multi-session bookkeeping) is wasted on work small enough for one PR.

## Prerequisites

| Resource | Purpose |
|----------|---------|
| [references/map-template.md](./references/map-template.md) | Starter scaffold for a new map file |
| [references/ticket-template.md](./references/ticket-template.md) | Starter scaffold for a new ticket file |
| [references/pr-graduation.md](./references/pr-graduation.md) | How a resolved ticket becomes a real PR spec file (via [`00-00-create-pr`](../00-00-create-pr/SKILL.md)) |
| [references/examples.md](./references/examples.md) | A worked example showing map + tickets in action |
| Existing `.github/.requirements/pr/*.md` files in scope | The PRs/decisions the map needs to survey |
| [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) | The project's plain PR template (used by existing simple PR files) |
| [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) | **The graduation skill.** Invoked to produce the PR spec file from a resolved slice |
| `ask_questions` tool | Used to resolve `grilling`-type tickets |

## The Map

One markdown file per planning effort: `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` (next available `<n>`, mirrors `.github/.requirements/pr/[nnn]-[slug].md` numbering, but sequential per wayfinding effort rather than random). Contains, in order:

- **Destination** — 1–2 sentences: what "done" looks like for this whole effort.
- **Notes** — domain context and standing preferences that every ticket should inherit (e.g. "Java 17 only — no legacy Forge patterns," "AI-side selection lands before GUI").
- **Decisions So Far** — a table of resolved tickets: `| Ticket | Gist | Resolved |`. One line per closed ticket — do not re-explain the decision here, link to the ticket file.
- **Not Yet Specified** — bullets: fog too unclear to phrase as a concrete ticket question yet. One-line test: if you can phrase the question precisely now (even without answering it), it belongs in **Tickets** instead.
- **Out of Scope** — bullets: work consciously ruled out, with a one-line reason. A ticket discovered to be out-of-scope moves here (with a link), not into Decisions So Far.
- **Tickets** — a table of open tickets: `| Ticket | Type | Status | Blocked By | File |`.

## Tickets

Each ticket is its own file: `.github/.requirements/wayfinding/wayfinding-<n>-tickets/<ticket-slug>.md`, sized to resolve in a single session. Use [references/ticket-template.md](./references/ticket-template.md). Fields:

- `Type` — one of `research`, `grilling`, `prototype`, `task` (see table below).
- `Status` — `open` / `claimed` / `resolved` / `ruled-out`.
- `Blocked By` — links to other tickets.
- `Question` — the one thing this ticket must answer.
- `Resolution` — filled in on close.
- `GitHub Issue` (optional) — number of a tracking GitHub issue, if one exists.

### Ticket Types → Resolution Mechanism

| Type | Resolved by | Tool / mechanism |
|------|-------------|------------------|
| `research` | Gathering codebase or external knowledge, no user interaction needed | Spawn an `Explore` subagent in the background; use file reads; check existing `.github/.requirements/pr/*.md` |
| `grilling` | Structured design interview | Invoke the `ask_questions` tool to interview the user, then record the answer in the ticket's Resolution. For a one-shot interview outside a map, use [`00-02-grill-me`](../00-02-grill-me/SKILL.md) instead |
| `prototype` | A rough artifact to sharpen discussion | Write a scratch file or throwaway spike under `bin/` or a temp dir — never production code |
| `task` | Manual prerequisite work the user must perform | A concrete action (e.g. run a `grep`, execute a merge command, open a GitHub issue) — typically HITL/AFK depending on risk |

## Step-by-Step Workflow

### Chart the Map (once, at the start)

1. **Survey the fog** — read every `.github/.requirements/pr/` file in scope plus the relevant source/docs, exactly as a planning pass would, but breadth-first across the *whole* effort rather than depth-first on one plan. State findings before writing anything.
2. **Write the Destination and Notes** — 1–2 sentences for done-looks-like, plus any standing constraints already surfaced during the survey (e.g. "no new Java 8 patterns — Java 17 is mandatory," "match the existing `[nnn]-[slug].md` PR naming").
3. **Leave Decisions So Far empty** — nothing is resolved yet at charting time.
4. **Create one ticket file per specifiable question** — each ticket gets a `Type`, a single `Question`, and `Blocked By` links where one ticket's answer gates another.
5. **List remaining fog under Not Yet Specified** — anything too vague to phrase as a ticket question yet.
6. **Stop.** Charting is one session. Do not resolve any ticket in the same pass that creates the map — that defeats the purpose of splitting the work across sessions.

### Work Through the Map (every subsequent session)

1. **Load the map file only** — not every ticket file. The map is the low-resolution view; only open the ticket file you're about to resolve.
2. **Pick the next ticket** — user-specified, or the first ticket in **Tickets** with no unresolved `Blocked By` entries.
3. **Claim it** — set `Status: claimed` in the ticket file before starting work, to signal to other sessions.
4. **Resolve it** using the mechanism its `Type` indicates (see table above).
5. **Record the resolution** in the ticket file itself; update its `Status` to `resolved` (or `ruled-out` if the work was discarded).
6. **Update the map**: append a one-line gist to Decisions So Far, remove the ticket from the open Tickets table.
7. **Handle what the resolution surfaced**: create new ticket files for newly-specifiable fog; graduate any Not Yet Specified item that's now precise enough into a real ticket; if the ticket turns out to be out-of-scope, move it to Out of Scope with a one-line reason instead of resolving it.
8. **Graduate finished decisions**: once enough tickets are resolved that a concrete, implementable slice exists, follow [references/pr-graduation.md](./references/pr-graduation.md) to invoke [`00-00-create-pr`](../00-00-create-pr/SKILL.md), which produces the actual PR requirements file at `.github/.requirements/pr/[nnn]-[slug].md` for that slice — the map does not produce PR spec files itself.

Expect concurrent editing if multiple sessions work the same map — re-read the map file immediately before claiming a ticket to avoid claiming one another session already took.

## Guard Rails

- **Plan, don't do.** A ticket's resolution is a decision recorded in its file (and gisted into the map) — not a PR spec file and not a code change. PR spec files happen via graduation; code changes happen via implementing whatever the PR spec file describes, same as any other work in this repo.
- Resolve at most one ticket per session unless it's a `research` ticket running in the background.
- Never resolve a ticket in the same session that charted the map.
- Do not create a wayfinder map for work that fits one `.github/.requirements/pr/` file — see "When to Use This Skill."
- When graduating, invoke [`00-00-create-pr`](../00-00-create-pr/SKILL.md) — don't write the PR spec file by hand or invent a new shape.
- Don't invent ticket types beyond the four listed. If something doesn't fit, it's probably a `task` or out of scope.

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Unsure whether this task needs a map at all | If you can list every open question in one planning pass without new ones appearing, it doesn't — write a single `.github/.requirements/pr/` file directly instead |
| A ticket turns out to be much bigger than one session | Split it into multiple tickets with `Blocked By` relationships; don't resolve it partially in one file |
| Two sessions claimed the same ticket | Re-read the map, keep whichever resolution is more complete, note the discarded one under Out of Scope with a pointer |
| The map itself is going stale (many tickets, few resolved) | Re-run "Survey the fog" as a fresh charting pass rather than continuing to add tickets indefinitely |
| Graduation produces a PR spec that's still too foggy | That usually means a ticket resolved too hastily — reopen it, refine the question, re-resolve |

## Validation Checklist

- [ ] Map file exists at `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` with Destination, Notes, Decisions So Far, Not Yet Specified, Out of Scope, and Tickets sections
- [ ] Every open ticket has its own file under `.github/.requirements/wayfinding/wayfinding-<n>-tickets/`, with `Type`, `Status`, `Blocked By`, and `Question` populated
- [ ] No ticket was resolved in the same session that charted the map
- [ ] Resolved tickets have a one-line gist in the map's Decisions So Far, and are removed from the open Tickets table
- [ ] Nothing under Not Yet Specified is actually precise enough to be a ticket (if it is, promote it)
- [ ] Any graduated slice has a real `.github/.requirements/pr/[nnn]-[slug].md` file produced by [`00-00-create-pr`](../00-00-create-pr/SKILL.md) (matches the skill's bundled template)

## Related Documents

- [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) — the graduation skill; produces the PR spec file from a resolved slice
- [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) — the project's plain PR template (used by existing simple PR files)
- [`.github/.requirements/`](../../.requirements/) — current planning artifacts (top-level files + `pr/` subfolder)
- [references/map-template.md](./references/map-template.md) — starter scaffold for a map file
- [references/ticket-template.md](./references/ticket-template.md) — starter scaffold for a ticket file
- [references/pr-graduation.md](./references/pr-graduation.md) — the graduation procedure (invokes `00-00-create-pr`)
- [references/examples.md](./references/examples.md) — worked example
- [`00-01-implement-pr/SKILL.md`](../00-01-implement-pr/SKILL.md) — **stub**: not yet adopted in this repo. Would read the PR spec file this map graduates into and execute the Implementation Plan.
- Original pattern: [mattpocock/skills — wayfinder](https://github.com/mattpocock/skills/blob/main/skills/engineering/wayfinder/SKILL.md) (GitHub-issue-native; adapted here to `.github/.requirements/` files since this repo's planning artifacts are already file-based, not issue-based)