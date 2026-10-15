---
name: 00-05-wayfinder
description: "Charts large, multi-session planning work as a shared map document with individually-resolvable decision tickets, keeping planning (resolving open questions) separate from execution (implementing PRs). Use when a BasseBombeCraft task has too many unresolved, interdependent design questions to fit in a single 00-00-create-pr or 00-02-grill-me pass — e.g. a Minecraft/Forge version migration or feature spanning several .github/.requirements/pr/ files whose open questions block each other, or a grill-me session that keeps surfacing new cross-cutting dependencies instead of converging."
---

# Skill: Wayfinder — Chart Large Multi-Session Planning Work

Adapts the ["wayfinder" planning pattern](https://github.com/mattpocock/skills/blob/main/skills/engineering/wayfinder/SKILL.md) to BasseBombeCraft's file-based `.github/.requirements/` workflow. Instead of a GitHub-issue map with child ticket issues, the map and its tickets are `.github/.requirements/wayfinding/` markdown files: a single **map** file plus a folder of individually small **ticket** files, each sized to resolve in one session.

The core principle carries over unchanged: **plan, don't do.** A ticket resolves one decision — it does not deliver code. Once a ticket's decision is resolved and its scope is small enough to implement, it graduates into a normal [`00-00-create-pr`](../00-00-create-pr/SKILL.md) pass, which produces the implementable PR requirements file under `.github/.requirements/pr/`; that file is then executed with [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md).

## When to Use This Skill

Load this skill when:
- A task is too large or too fogged-in for a single `00-00-create-pr` pass — multiple interdependent, unresolved design questions across several subsystems or PRs (e.g. migrating to a new Minecraft/Forge version where the networking, rendering and registry changes constrain each other).
- A `00-02-grill-me` session keeps surfacing new blocking dependencies faster than it resolves them (the signal that a single-session interview isn't the right shape for the problem).
- The user asks to "chart a map," "wayfind," "scope this out across sessions," or references planning work that spans more `.github/.requirements/pr/` files than one session can hold in context.
- Several `.github/.requirements/pr/` files reference each other's open questions as blocking (e.g. "this PR depends on how that PR resolves the registry-ordering question").

**Do not use this skill** for a task that fits a normal `00-00-create-pr` → `00-01-implement-pr` pass (or the build-error batch flow in [`create-pr-from-build-errors`](../create-pr-from-build-errors/SKILL.md) with the [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) skill) — wayfinder's overhead (a map file, ticket files, multi-session bookkeeping) is wasted on work small enough for one PR. Typical one-symbol-rename compile fixes never need a map.

## Prerequisites

| Resource | Purpose |
|----------|---------|
| [map-template.md](./templates/map-template.md) | Starter scaffold for a new map file |
| [ticket-template.md](./templates/ticket-template.md) | Starter scaffold for a new ticket file |
| [examples.md](./references/examples.md) | A worked example (a retrofitted map for the 1.18.2 migration, `#1440`–`#1461`) |
| Existing `.github/.requirements/pr/*.md` files in scope | The PRs/decisions the map needs to survey |
| [`00-02-grill-me`](../00-02-grill-me/SKILL.md) | Invoked to resolve `grilling`-type tickets |
| [`00-00-create-pr`](../00-00-create-pr/SKILL.md) | Invoked once a ticket's decision is resolved and ready to become an implementable PR |
| [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md) | Executes a graduated PR requirements file |
| [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) | Forge API migration patterns — consult when resolving `research` tickets about removed/renamed Forge APIs |

## The Map

One markdown file per planning effort: `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` (next available `<n>`; sequential per wayfinding effort, unlike PR files, which are numbered by GitHub issue). Contains, in order:

- **Destination** — 1–2 sentences: what "done" looks like for this whole effort.
- **Notes** — domain context and standing preferences that every ticket should inherit (e.g. "target is Minecraft 1.18.2 / Forge 40.3.0," "no behavioural changes, API migration only").
- **Decisions So Far** — a table of resolved tickets: `| Ticket | Gist | Resolved |`. One line per closed ticket — do not re-explain the decision here, link to the ticket file.
- **Not Yet Specified** — bullets: fog too unclear to phrase as a concrete ticket question yet. One-line test: if you can phrase the question precisely now (even without answering it), it belongs in **Tickets** instead, not here.
- **Out of Scope** — bullets: work consciously ruled out, with a one-line reason. A ticket discovered to be out-of-scope moves here (with a link), not into Decisions So Far.
- **Tickets** — a table of open tickets: `| Ticket | Type | Status | Blocked By | File |`.

## Tickets

Each ticket is its own file: `.github/.requirements/wayfinding/wayfinding-<n>-tickets/<ticket-slug>.md`, sized to resolve in a single session. Use [ticket-template.md](./templates/ticket-template.md). Fields: `Type`, `Status` (`open` / `claimed` / `resolved` / `ruled-out`), `Blocked By` (links to other tickets), `Question` (the one thing this ticket must answer), `Resolution` (filled in on close).

### Ticket Types → Resolution Mechanism

| Type | Resolved by | BasseBombeCraft equivalent |
|------|-------------|----------------------------|
| `research` | Gathering external/codebase knowledge, no user interaction needed | Spawn an `Explore` or `general-purpose` agent, or use `WebFetch`/`WebSearch` (Forge docs, Forge/Minecraft changelogs, MixinExtras/MC source), in the background. Consult [forge-api-migration-patterns.md](../forge-upgrade-pr/references/forge-api-migration-patterns.md) first |
| `grilling` | Structured design interview | Invoke [`00-02-grill-me`](../00-02-grill-me/SKILL.md) |
| `prototype` | A rough artifact to sharpen discussion | Write a scratch file or spike under the session scratchpad — throwaway, not production code. Never leave it in `src/` |
| `task` | Manual prerequisite work | A concrete action (e.g. run `./gradlew compileJava --info` and group the errors, run a `grep` audit of an API's usage, check whether a branch merge is needed) — HITL or AFK depending on risk |

## Step-by-Step Workflow

### Chart the Map (once, at the start)

1. **Survey the fog** — read every `.github/.requirements/pr/` file in scope plus the relevant source (and, for upgrades, a fresh `./gradlew compileJava` error list), breadth-first across the *whole* effort rather than depth-first on one plan. State findings before writing anything.
2. **Write the Destination and Notes** — 1–2 sentences for done-looks-like, plus any standing constraints already surfaced during the survey.
3. **Leave Decisions So Far empty** — nothing is resolved yet at charting time.
4. **Create one ticket file per specifiable question** — each ticket gets a `Type`, a single `Question`, and `Blocked By` links where one ticket's answer gates another.
5. **List remaining fog under Not Yet Specified** — anything too vague to phrase as a ticket question yet.
6. **Stop.** Charting is one session. Do not resolve any ticket in the same pass that creates the map — that defeats the purpose of splitting the work across sessions.

### Work Through the Map (every subsequent session)

1. **Load the map file only** — not every ticket file. The map is the low-resolution view; only open the ticket file you're about to resolve.
2. **Pick the next ticket** — user-specified, or the first ticket in **Tickets** with no unresolved `Blocked By` entries.
3. **Resolve it** using the mechanism its `Type` indicates (see table above).
4. **Record the resolution** in the ticket file itself; update its `Status` to `resolved`.
5. **Update the map**: append a one-line gist to Decisions So Far, remove the ticket from the open Tickets table.
6. **Handle what the resolution surfaced**: create new ticket files for newly-specifiable fog; graduate any Not Yet Specified item that's now precise enough into a real ticket; if the ticket turns out to be out-of-scope, move it to Out of Scope with a one-line reason instead of resolving it.
7. **Graduate finished decisions**: once enough tickets are resolved that a concrete, implementable slice exists, invoke [`00-00-create-pr`](../00-00-create-pr/SKILL.md) to produce the actual PR requirements file for that slice (`.github/.requirements/pr/<issue-number>-<kebab-name>.md`). The map does not implement anything itself; implementation happens via [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md).

Expect concurrent editing if multiple sessions work the same map — re-read the map file immediately before claiming a ticket to avoid claiming one another session already took.

## Guard Rails

- **Plan, don't do.** A ticket's resolution is a decision recorded in its file (and gisted into the map) — not a code change. Code changes happen via `00-00-create-pr` → `00-01-implement-pr`, same as any other work in this repo.
- Resolve at most one ticket per session unless it's a `research` ticket running in the background.
- Never resolve a ticket in the same session that charts the map.
- Do not create a wayfinder map for work that fits one `00-00-create-pr` pass — see "When to Use This Skill."
- Map and ticket files describe decisions, not build results: do not paste long Gradle output into them; summarise and link to the PR requirements file or issue instead.

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Unsure whether this task needs a map at all | If you can list every open question in one `00-02-grill-me` pass without new ones appearing, it doesn't — use `00-02-grill-me` / `00-00-create-pr` directly |
| A ticket turns out to be much bigger than one session | Split it into multiple tickets with `Blocked By` relationships, don't resolve it partially in one file |
| Two sessions claimed the same ticket | Re-read the map, keep whichever resolution is more complete, note the discarded one under Out of Scope with a pointer |
| The map itself is going stale (many tickets, few resolved) | Re-run "Survey the fog" as a fresh charting pass rather than continuing to add tickets indefinitely |
| A ticket's answer depends on whether the code actually compiles/runs | Add a `task` ticket for the `./gradlew` run first; don't guess at build state |

## Validation Checklist

- [ ] Map file exists at `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` with Destination, Notes, Decisions So Far, Not Yet Specified, Out of Scope, and Tickets sections
- [ ] Every open ticket has its own file under `.github/.requirements/wayfinding/wayfinding-<n>-tickets/`, with `Type`, `Status`, `Blocked By`, and `Question` populated
- [ ] No ticket was resolved in the same session that charted the map
- [ ] Resolved tickets have a one-line gist in the map's Decisions So Far, and are removed from the open Tickets table
- [ ] Nothing under Not Yet Specified is actually precise enough to be a ticket (if it is, promote it)

## Related Documents

- [00-00-create-pr/SKILL.md](../00-00-create-pr/SKILL.md) — produces the implementable PR once a ticket's decision is resolved and scoped
- [00-01-implement-pr/SKILL.md](../00-01-implement-pr/SKILL.md) — implements the PR requirements file a ticket graduated into
- [00-02-grill-me/SKILL.md](../00-02-grill-me/SKILL.md) — resolves `grilling`-type tickets
- [forge-upgrade-pr/SKILL.md](../forge-upgrade-pr/SKILL.md) — ranks build errors and produces PR requirement files for a single-pass Forge fix
- [`create-pr-from-build-errors`](../create-pr-from-build-errors/SKILL.md) — the single-pass alternative when a map isn't warranted
- [map-template.md](./templates/map-template.md) / [ticket-template.md](./templates/ticket-template.md) — bundled starter scaffolds
- [examples.md](./references/examples.md) — a worked example
- Original pattern: [mattpocock/skills — wayfinder](https://github.com/mattpocock/skills/blob/main/skills/engineering/wayfinder/SKILL.md) (GitHub-issue-native; adapted here to `.github/.requirements/wayfinding/` files since this repo's planning artifacts are already file-based)
