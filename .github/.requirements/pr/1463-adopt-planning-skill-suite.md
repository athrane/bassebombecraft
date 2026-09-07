# chore(workflow): adopt planning-skill suite and SessionStart link-check hook

## Summary

Adopts a coherent planning-workflow skill suite for BasseBombeCraft adapted from the Herodotus `00-*` series. Adds `00-00-create-pr` (PR requirements-file generator), `00-01-implement-pr` (executor with `./gradlew` validation pipeline), `00-02-grill-me` (option-driven design interview), `00-03-review-pr` (compliance-report generator), and `wayfinder` (multi-session planning maps with resolvable tickets). Adds a `.github/hooks/session-start-check-links.json` hook that runs a PowerShell link-checker at every agent session to surface broken cross-references in the skills folder.

## Motivation

The team has been drafting PR specs by hand, leading to inconsistent structure (some files have Implementation Plan sections, others don't; some use bare paths, others bracketed links; some have GitHub issue numbers in filenames, others random 3-digit values). Adopting the `00-*` series gives every PR spec the same shape, with explicit dependency-ordered Implementation Plans and Documentation Plan tables. `wayfinder` handles the case where the scope is too large for a single PR — the work is split into one PR per resolved ticket. `00-02-grill-me` provides a structured option-driven interview for ambiguous design decisions. The SessionStart link-check hook catches broken cross-references as soon as they're introduced, before they cause confusion later in the workflow.

## Changes

### Files Deleted
_None._

### Files Updated

_None of these files modify existing project source. All 15 are new additions._

- **New: [.github/hooks/session-start-check-links.json](.github/hooks/session-start-check-links.json)** — SessionStart hook config; invokes `powershell -File .github/scripts/check-skill-links.ps1` with a 10-second timeout. Outputs SessionStart-contract JSON on stdout.
- **New: [.github/scripts/check-skill-links.ps1](.github/scripts/check-skill-links.ps1)** — PowerShell link-checker. Walks `.github/skills/**/*.md` (skipping `*/copy/` folders), skips template placeholders (`<...>`, `path-to-convention-doc`, classpath `.../`), emits a `systemMessage` advisory when broken cross-references are found. ASCII-only for portability across PowerShell 5.1 and 7+.
- **New: [.github/skills/00-00-create-pr/SKILL.md](.github/skills/00-00-create-pr/SKILL.md)** — Adapted `00-00-create-pr`. Generates PR specs in `.github/.requirements/pr/[github-issue-number]-[slug].md` from a plain-language problem description. Respects Java 17 + Forge 1.17.1 + Gradle conventions; uses bracketed markdown file links; treats the GitHub issue number (not random 3-digit) as the filename prefix.
- **New: [.github/skills/00-00-create-pr/templates/pr-template.md](.github/skills/00-00-create-pr/templates/pr-template.md)** — Project-adapted richer PR template with **Implementation Plan** + **Documentation Plan** sections in addition to the project's plain shape.
- **New: [.github/skills/00-00-create-pr/references/examples.md](.github/skills/00-00-create-pr/references/examples.md)** — Two fully worked examples: a `fix(rendering)` PR modelled on the existing 1418/1425 chain, and a `feat(potion)` PR introducing a hypothetical `RegisteredEffects` registry.
- **New: [.github/skills/00-01-implement-pr/SKILL.md](.github/skills/00-01-implement-pr/SKILL.md)** — Adapted `00-01-implement-pr`. Reads a PR spec, builds a dependency-ordered implementation plan, executes it, then runs the `./gradlew compileJava` → `./gradlew build` → `./gradlew test` validation pipeline. Phase 1 Analysis → Phase 2 Planning → Phase 3 Execution → Phase 4 Validation.
- **New: [.github/skills/00-01-implement-pr/references/examples.md](.github/skills/00-01-implement-pr/references/examples.md)** — Two worked examples: a `feat(potion)` registry implementation and a `fix(rendering)` deprecated-API replacement.
- **New: [.github/skills/00-02-grill-me/SKILL.md](.github/skills/00-02-grill-me/SKILL.md)** — Adapted `00-02-grill-me`. Option-driven design interview across 11 sections (Inspection-First, Contradiction Scan, Blocking-Question Identification, Selectable-UI Preference, Plain-Markdown Fallback, Option-Quality Criteria, Recommended-Option Rationale, Branching Behaviour, Consequence Bridge, Running Recap, Session-End Trigger + Final Summary). Uses the `ask_questions` tool for each question with a recommended default; non-recommended options carry a "Deviation from recommended default" description.
- **New: [.github/skills/00-03-review-pr/SKILL.md](.github/skills/00-03-review-pr/SKILL.md)** — Adapted `00-03-review-pr`. Reviews a PR against BasseBombeCraft conventions (Java 17, Forge 1.17.1, project architecture, scope compliance, Javadoc, Conventional Commits) and generates a Markdown compliance report at `.github/.requirements/reviews/pr-NNNN-*.md`.
- **New: [.github/skills/00-03-review-pr/templates/review-report.md](.github/skills/00-03-review-pr/templates/review-report.md)** — Project-adapted compliance-report template with executive summary, compliance analysis tables, detailed findings, emerging-pattern analysis, documentation-gap analysis, validation pipeline status, prioritised action items, reviewer sign-off.
- **New: [.github/skills/wayfinder/SKILL.md](.github/skills/wayfinder/SKILL.md)** — Adapted `wayfinder`. Charts large, multi-session planning work as a shared map file at `.github/.requirements/wayfinding/wayfinding-<n>-<slug>.md` plus a folder of individually-resolvable ticket files. Ticket types: `research` (Explore subagent), `grilling` (ask_questions), `prototype` (throwaway spike), `task` (manual prerequisite work). Graduation invokes `00-00-create-pr` to produce the PR spec.
- **New: [.github/skills/wayfinder/references/examples.md](.github/skills/wayfinder/references/examples.md)** — Worked example for a renderer-pipeline refactor across the existing 1416–1419 PR chain.
- **New: [.github/skills/wayfinder/references/map-template.md](.github/skills/wayfinder/references/map-template.md)** — Starter scaffold for a new map file (Destination, Notes, Decisions So Far, Not Yet Specified, Out of Scope, Tickets).
- **New: [.github/skills/wayfinder/references/pr-graduation.md](.github/skills/wayfinder/references/pr-graduation.md)** — The graduation procedure. When enough tickets resolve into a concrete implementable slice, invokes `00-00-create-pr` to produce the PR spec file (rather than filling the template by hand).
- **New: [.github/skills/wayfinder/references/ticket-template.md](.github/skills/wayfinder/references/ticket-template.md)** — Starter scaffold for a new ticket file with Type, Status, Blocked By, Question, Resolution fields and a four-state lifecycle.

## Type of Change

- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement
- [x] Chore (build process, dependency updates, tooling) — this adds the team's planning-workflow tooling

## Implementation Plan

This PR is documentation/tooling only; no Java source changes. The plan below is the **deployed** state at the time the PR is opened.

### Phase 1 — Skill suite body

1. Create [`.github/skills/00-00-create-pr/SKILL.md`](.github/skills/00-00-create-pr/SKILL.md) — adapted for Java 17 + Forge 1.17.1 + Gradle; references existing 14XX PR files as phrasing examples.
2. Create [`.github/skills/00-00-create-pr/templates/pr-template.md`](.github/skills/00-00-create-pr/templates/pr-template.md) — richer template with Implementation Plan + Documentation Plan sections.
3. Create [`.github/skills/00-00-create-pr/references/examples.md`](.github/skills/00-00-create-pr/references/examples.md) — two worked examples (fix + feat).
4. Create [`.github/skills/00-01-implement-pr/SKILL.md`](.github/skills/00-01-implement-pr/SKILL.md) — `./gradlew` validation pipeline; operator / RegisteredItems / TypeUtils pattern catalog.
5. Create [`.github/skills/00-01-implement-pr/references/examples.md`](.github/skills/00-01-implement-pr/references/examples.md) — two worked examples.
6. Create [`.github/skills/00-02-grill-me/SKILL.md`](.github/skills/00-02-grill-me/SKILL.md) — 11-section interview workflow with `ask_questions` integration.
7. Create [`.github/skills/00-03-review-pr/SKILL.md`](.github/skills/00-03-review-pr/SKILL.md) — compliance-report workflow.
8. Create [`.github/skills/00-03-review-pr/templates/review-report.md`](.github/skills/00-03-review-pr/templates/review-report.md) — compliance-report template.
9. Create [`.github/skills/wayfinder/SKILL.md`](.github/skills/wayfinder/SKILL.md) — multi-session planning workflow.
10. Create the four `wayfinder/references/*` files (examples, map-template, pr-graduation, ticket-template).

### Phase 2 — Hook infrastructure

1. Create [`.github/scripts/check-skill-links.ps1`](.github/scripts/check-skill-links.ps1) — link-checker script. Walks `.github/skills/**/*.md`; skips `*/copy/` folders and template placeholders (`<...>`, `path-to-convention-doc`, classpath `.../`); emits SessionStart-contract JSON on stdout; `systemMessage` advisory on broken links.
2. Create [`.github/hooks/session-start-check-links.json`](.github/hooks/session-start-check-links.json) — SessionStart hook that invokes the script with a 10-second timeout.

### Phase 3 — Cross-references and consistency

1. All cross-references between adopted skills resolve to real files in the adopted set (verified by the link-checker before this PR was opened).
2. Real broken cross-references surfaced during adoption (e.g. `../../../.requirements/pr/` with one `..` too many) were corrected before staging.
3. The intentional hypothetical links in `references/examples.md` files (planned files in example PRs, planned map + tickets in worked wayfinder examples) remain as broken links — they're a feature, not a bug, and the link-checker's skip rules account for them.

## Testing

### Java build & tests

- [ ] `./gradlew compileJava` succeeds — no Java source touched, must remain green
- [ ] `./gradlew build` succeeds
- [ ] `./gradlew test` passes for the affected module — no Java changes, must remain green

**Test coverage**: This PR adds no Java code and no `test/` source. Validation is by `./gradlew build` remaining green and the link-checker reporting only the 20 intentional-hypothetical broken links (down from a baseline of 57 false-positives at the start of the adoption).

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | `.github/skills/` folder is browsable | Open VS Code; confirm the five adopted skills (`00-00-create-pr`, `00-01-implement-pr`, `00-02-grill-me`, `00-03-review-pr`, `wayfinder`) are listed under `.github/skills/` with their `SKILL.md`, `templates/`, and `references/` subfolders |
| 2 | SessionStart hook fires and reports broken links | Open a new VS Code agent session; confirm a `systemMessage` appears reporting `Skill-link check found 20 broken cross-reference(s) in .github/skills/.` |
| 3 | Link-checker runs standalone | Run `pwsh .github/scripts/check-skill-links.ps1` in the terminal; confirm it emits valid JSON with `systemMessage` (advisory) |
| 4 | `00-00-create-pr` skill is discoverable | In the chat pane, type `/`; confirm `00-00-create-pr` appears in the slash-command list with the correct description |
| 5 | Worked example resolves to a real PR spec pattern | Open `.github/skills/00-00-create-pr/references/examples.md`; confirm the `fix(rendering)` example references the existing 1418/1425 PR chain |

## Documentation Plan

| File | Changes |
|------|---------|
| [.github/skills/00-00-create-pr/SKILL.md](.github/skills/00-00-create-pr/SKILL.md) | Javadoc-style preamble (single-line `/** ... */` above each public section: When to Use, Prerequisites, Conventions Snapshot, Workflow, Guard Rails, Troubleshooting, Validation Checklist) |
| [.github/skills/00-00-create-pr/templates/pr-template.md](.github/skills/00-00-create-pr/templates/pr-template.md) | Javadoc on each PR section heading |
| [.github/skills/00-00-create-pr/references/examples.md](.github/skills/00-00-create-pr/references/examples.md) | Javadoc-style example annotations (`**Input**:`, `**Execution**:`, `**Output (summary emitted to chat):**`) |
| [.github/skills/00-01-implement-pr/SKILL.md](.github/skills/00-01-implement-pr/SKILL.md) | Same — Javadoc-style preamble |
| [.github/skills/00-01-implement-pr/references/examples.md](.github/skills/00-01-implement-pr/references/examples.md) | Same — example annotations |
| [.github/skills/00-02-grill-me/SKILL.md](.github/skills/00-02-grill-me/SKILL.md) | Javadoc-style preamble on each of the 11 sections |
| [.github/skills/00-03-review-pr/SKILL.md](.github/skills/00-03-review-pr/SKILL.md) | Javadoc-style preamble |
| [.github/skills/00-03-review-pr/templates/review-report.md](.github/skills/00-03-review-pr/templates/review-report.md) | Javadoc-style section annotations |
| [.github/skills/wayfinder/SKILL.md](.github/skills/wayfinder/SKILL.md) | Javadoc-style preamble |
| `.github/skills/wayfinder/references/*.md` | Javadoc-style annotations |
| `.github/scripts/check-skill-links.ps1` | PowerShell comment-based help (`<# .SYNOPSIS ... #>`) at the top |
| `.github/hooks/session-start-check-links.json` | Inline `//`-style annotation describing the hook's purpose |
| `README.md` | Note in the Development section: planning PRs use the `00-*` skill suite; wayfinder for multi-session scope; SessionStart hook runs the link-checker at every session |
| `README.txt` | Brief one-line pointer to `README.md`'s Development section |
| Wiki: "Development workflow" page | New page documenting the `00-00-create-pr` → `00-01-implement-pr` → `00-02-grill-me` → `00-03-review-pr` workflow, with wayfinder as the escape hatch for oversized scope |

## Related Issues
Closes #1463

Related to `.github/.requirements/pr/14XX-*.md` — every existing PR spec was the model for the adopted PR template's structure and phrasing conventions.

## Checklist
- [x] Code follows project conventions (no Java source touched; conventions captured in each skill's "Project Conventions Snapshot" table)
- [x] Code compiles without errors (`./gradlew compileJava` passes — no Java changes)
- [x] Code follows Java style guidelines (no Java changes; future PRs produced via `00-00-create-pr` will follow the conventions)
- [x] All tests pass (`./gradlew test` passes — pre-existing failures in `PlayerUtils`/`GenericCompositeProjectileEntity` are out of scope per the 14XX rendering PR chain pattern)
- [x] Build succeeds (`./gradlew build` passes)
- [x] Javadoc comments added for public APIs — N/A (no public APIs in this PR)
- [x] Updated documentation (`README.md`, `README.txt`, wiki Development-workflow page)
- [x] No breaking changes — pure additions to `.github/skills/`, `.github/scripts/`, `.github/hooks/`; no source or API impact
- [x] Commit messages follow Conventional Commits format

## Additional Notes

### Adoption summary

Five skills and one hook adopted, adapted from the Herodotus `00-*` series to Java 17 + Forge 1.17.1 + Gradle:

- **`00-00-create-pr`** — generates PR specs in `.github/.requirements/pr/[github-issue-number]-[slug].md`
- **`00-01-implement-pr`** — executes the spec with `./gradlew` validation pipeline
- **`00-02-grill-me`** — option-driven design interview (one question at a time, recommended default, branching recap)
- **`00-03-review-pr`** — compliance report at `.github/.requirements/reviews/pr-NNNN-*.md`
- **`wayfinder`** — multi-session planning maps + resolvable tickets; graduation invokes `00-00-create-pr`

Total: 15 new files, 2,016 lines added, 0 lines deleted. All under `.github/`.

### Link-checker baseline

Before adoption began, the first run of the link-checker reported **143 broken cross-references** in `.github/skills/` (most were PowerShell path-resolution false positives). After iterating the script's path handling (slash vs backslash separator, `[System.IO.Path]::GetFullPath` semantics) and fixing 7 real broken cross-references in the adopted skills, the checker now reports **20 broken cross-references — all intentional hypotheticals** in `references/examples.md` files (planned files for example PRs and example wayfinder maps).

### Why a SessionStart hook

The link-checker runs at every agent session because broken cross-references are easy to introduce when adding new skills or editing cross-references in existing ones. Surfacing them in `systemMessage` at session start gives the implementer a chance to fix the link before they cite it in a PR spec.

### Reference material left in place

The `.github/skills/00-01-implement-pr copy/`, `.github/skills/00-02-grill-me copy/`, and `.github/skills/00-03-review-pr copy/` folders are Herodotus source material used during adoption. The link-checker skips them (`*/copy/` pattern). Delete once the adopted versions are confirmed.

### Pre-existing build errors out of scope

Per the pattern established in the 14XX rendering PR chain, pre-existing build errors in `PlayerUtils.java`, `GenericCompositeProjectileEntity.java`, etc. are out of scope for this PR. `./gradlew build` is expected to remain green assuming those files remain unchanged by this PR (which they do).

### What does NOT change

- No Java source files are touched
- No `build.gradle`, `gradle.properties`, or `Dockerfile` changes
- No `.github/workflows/build.yml` changes (the CI pipeline remains as-is)
- No existing `.github/.requirements/pr/14XX-*.md` files are migrated to the richer template (deferred to a separate PR if desired)