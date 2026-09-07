---
name: 00-00-create-pr
description: 'Generates a complete PR requirements file in .github/.requirements/pr/ from a plain-language problem description: type, scope, affected files, implementation/test/doc plans. Use to draft a new PR requirements file for the BasseBombeCraft Minecraft Forge mod.'
argument-hint: '[problem description]'
---

# Skill: Generate PR Requirements File

Produce a properly formatted PR requirements file under `.github/.requirements/pr/` that adheres to [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) — including a detailed implementation plan, test plan, and documentation plan derived from codebase analysis. The agent infers everything — type, scope, affected files, implementation plan, test plan, documentation plan, related-issue handling — from the problem description and the codebase. No further input from the user is required beyond the initial problem description.

## When to Use This Skill

- The user asks to create/draft a PR description or PR requirements file
- The user wants a `.github/.requirements/pr/` file generated for a planned change
- The user is starting a "draft PR spec → implement → review" workflow

If the scope is too large or too fogged-in for one PR — several interdependent, unresolved design questions spanning multiple files or PRs — chart a map with [`wayfinder`](../wayfinder/SKILL.md) first, then invoke this skill once per resolved ticket to produce each implementable PR.

## Prerequisites

| Resource | Purpose |
|----------|---------|
| [templates/pr-template.md](./templates/pr-template.md) | Canonical PR description template this skill fills in (richer than the project's plain PR template — adds **Implementation Plan**, **Documentation Plan**) |
| [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) | The project's plain PR template; used by existing PR files for simpler bug-fix / chore PRs |
| `src/main/java/bassebombecraft/` | Existing Java source for affected-domain search |
| `version.json` | Minecraft + mod version reference |
| Existing `.github/.requirements/pr/*.md` files | Past PR specs — mirror their phrasing, scope names, file/line citation style |
| [`wayfinder`](../wayfinder/SKILL.md) | For oversized scope — chart a map first, then invoke this skill per ticket |

## Project Conventions Snapshot

These are the standing facts this skill should respect when generating a PR spec. They are derived from the existing `.github/.requirements/pr/14XX-*.md` files plus the codebase; do not invent conventions that contradict these.

| Convention | Source of truth |
|------------|-----------------|
| **Language & toolchain** | Java 17 only (matches `build.gradle` `java.toolchain.languageVersion = JavaLanguageVersion.of(17)`). `./gradlew build`, `./gradlew test` are the canonical validation commands — **not** `npm run`. |
| **Minecraft version** | Forge 1.17.1 baseline; new code must not use pre-1.17 OpenGL or deprecated Forge APIs |
| **PR title format** | `<type>(<scope>): <description>` — Conventional Commits. Common scopes observed: `potion`, `rendering`, `projectile`, `entity`, `inventory`, `block`, `event`, `ci`, `docker`, `chore`, `readme` |
| **Filename format** | `[github-issue-number]-[short-kebab-slug].md` — e.g. `1418-resolve-renderingutils-import-error.md`. **Use the GitHub issue number, not a random 3-digit value.** If no issue exists yet, ask the user once or use `9999-pending-issue-number.md` as a placeholder and flag it in the file's Additional Notes. |
| **File & line citation** | Use bracketed markdown links: `[MobPrimingEffect.java](src/main/java/bassebombecraft/potion/effect/MobPrimingEffect.java#L87)`, not bare paths |
| **Architecture patterns** | `RegisteredItems` / `RegisteredEntities` registration pattern; `TypeUtils`-style validation; operator pattern (`src/main/java/bassebombecraft/operator/`); event-driven side effects (`src/main/java/bassebombecraft/event/`) |
| **Conventional Commits types** | `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `perf`, `style` |
| **Java style** | Javadoc on public APIs; one top-level class per file; static factory methods (`create()`); `RegisteredItems.register(...)` style registration; `PlayerUtils` / `EntityUtils` / `BlockUtils` helper classes |
| **Randomness** | N/A — no `RandomComponent` analogue; this is a Minecraft mod, not an ECS framework |
| **What "done" looks like** | `./gradlew build` succeeds and `./gradlew test` passes for the affected module; for GUI/rendering changes, manual in-game testing is also expected |

## Step-by-Step Workflow

1. **Read template and project context** — read [templates/pr-template.md](./templates/pr-template.md) and skim 2–3 existing `.github/.requirements/pr/14XX-*.md` files to absorb the in-repo phrasing conventions. Read [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) for the PR title format.
2. **Analyze problem and explore codebase**
   - Identify the primary domain(s) affected — common domains: `potion`, `entity`, `projectile`, `inventory`, `client/rendering`, `client/screen`, `event`, `block`, `item`, `config`, `sound`, `network`, `world`
   - Search `src/main/java/bassebombecraft/` for existing files in that domain
   - Identify applicable architectural patterns from `src/main/java/bassebombecraft/operator/`, `event/`, `RegisteredItems`, `RegisteredEntities`
   - Infer the Conventional Commits type (`feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `perf`, `style`) and scope from the problem description
3. **Resolve the GitHub issue number**
   - Look at existing `.github/.requirements/pr/*.md` files for the largest `nnnn` currently in use
   - If the user has stated a GitHub issue number, use it
   - If a tracking GitHub issue already exists for this work (search repo issues), use that number
   - If none of the above, use `9999` as a placeholder and add a clear note in **Additional Notes** that the filename must be renamed once an issue is filed
4. **Generate the PR title** — `<type>(<scope>): <description>`, imperative mood, ≤72 chars, Conventional Commits format. The scope should be a single short noun mirroring the affected package.
5. **Generate the PR description**, filling every section of the template:
   - **Summary / Motivation** — derived from the problem description; reference affected files via bracketed markdown links
   - **Changes** — list affected files in dependency order (interfaces/enums → classes → registration → tests). Use `New ...` to mark newly created files. For deleted files, list them under a `Files Deleted` sub-heading.
   - **Type of Change** — tick exactly the most appropriate box
   - **Implementation Plan** — phased, ordered steps (data model → registration → wiring → event handlers → tests). Each phase lists the files to create/edit.
   - **Test Plan** — describe existing tests to update (none in most cases — this mod has no `test/` source set), new test files if applicable (most changes are in-game verification), a manual-validation table (in-game steps + expected outcome), and which `./gradlew` commands must pass
   - **Documentation Plan** — a `| File | Changes |` table for every affected `README.md`, `README.txt`, `wiki` page, or inline Javadoc; omit the section entirely if no doc changes are required
   - **Related Issues** — `Closes #[issue-number]` if a GitHub issue exists; `Related to #[issue-number]` for known cross-references; `_None._` otherwise. If graduated from a wayfinder map, link the source ticket files.
   - **Checklist** + **Additional Notes** — match the template; cite real file:line evidence where possible
6. **Optionally stress-test the plan** — if the Implementation Plan involves significant or ambiguous design decisions (e.g. choosing between two valid Forge APIs), invoke [`wayfinder`](../wayfinder/SKILL.md) instead to chart a map with one `grilling` ticket for the decision. **Do not** ask the user clarifying questions inline; if the plan is genuinely ambiguous, that's a signal to use wayfinder, not to interview inline.
7. **Format and write the output**
   - Construct the slug from 2–3 short keywords that summarise the change (lowercase, hyphen-separated)
   - Write to `.github/.requirements/pr/[github-issue-number]-[slug].md`
   - Create the `pr/` directory if it doesn't exist
   - Verify every required section is populated, then report the file path and PR title

See [references/examples.md](./references/examples.md) for two fully worked examples (a `fix(rendering)` PR and a `feat(potion)` PR) showing the expected level of detail.

## Guard Rails

- Do not create the actual GitHub PR — this skill only generates the PR requirements file
- Only create/modify files in `.github/.requirements/pr/` and `.github/.requirements/wayfinding/` (when invoked from wayfinder); never touch project source files (`src/main/java/`, `gradle/`, `Dockerfile`, etc.)
- Do not ask the user for information inferable from the codebase or problem description
- If scope is genuinely ambiguous (more than one reasonable Forge API choice, cross-cutting changes), use [`wayfinder`](../wayfinder/SKILL.md) instead of asking inline
- Leave all checklist items unchecked (the implementer checks them)
- Use the GitHub issue number in the filename, not a random 3-digit value
- Cite affected files with bracketed markdown links (`[Foo.java](src/main/java/.../Foo.java)`), not bare paths
- Match Java 17 + Forge 1.17.1 conventions in any code snippets in the PR (do not show Java 8 patterns or pre-1.17 Forge APIs)

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Problem description too vague to infer domain | Ask exactly one targeted clarifying question, then proceed |
| No GitHub issue number available | Use `9999` as a placeholder; flag in **Additional Notes** that the filename must be renamed once the issue is filed |
| Template cannot be read | Report the error and fall back to the in-line section list in Step 5 |
| Codebase search returns no relevant files | Note the assumption explicitly in **Additional Notes** and proceed with best inference (e.g. "this is a new entity type — pattern inferred from `RegisteredEntities` and `RegisteredItems`") |
| Scope spans multiple unrelated areas | This is a signal to use [`wayfinder`](../wayfinder/SKILL.md) — invoke it instead and tell the user the scope needs planning first |
| Affected file is in `bin/` | Confirm the file is throwaway. If it's intended to be permanent, it shouldn't be in `bin/` — flag the relocation in **Additional Notes** |

## Validation Checklist

- [ ] `.github/.requirements/pr/` exists or was created
- [ ] Filename matches `[github-issue-number]-[short-slug].md` (issue number, not random 3-digit)
- [ ] PR title follows `<type>(<scope>): <description>` (Conventional Commits, ≤72 chars)
- [ ] All required template sections are present and populated (no placeholder text)
- [ ] **Type of Change** has exactly one appropriate box marked
- [ ] **Implementation Plan** phases are dependency-ordered (data model → registration → wiring → tests)
- [ ] **Test Plan** lists concrete manual-validation steps plus a `./gradlew` command list
- [ ] **Documentation Plan** present only when documentation changes are required
- [ ] **Related Issues** cites a real GitHub issue number when one exists (or `_None._`)
- [ ] Every cited file uses bracketed markdown link syntax
- [ ] Code snippets in the PR use Java 17 + Forge 1.17.1 patterns only

## Related Documents

- [`wayfinder/SKILL.md`](../wayfinder/SKILL.md) — charts oversized, multi-session scope into resolvable tickets before this skill produces an implementable PR per ticket
- [`00-01-implement-pr/SKILL.md`](../00-01-implement-pr/SKILL.md) — **stub**: not yet adopted in this repo. Reads and executes the requirements file this skill produces.
- [`00-02-grill-me/SKILL.md`](../00-02-grill-me/SKILL.md) — option-driven design interview. Invoked at Step 6 to stress-test an ambiguous implementation plan before writing the file.
- [`00-03-review-pr/SKILL.md`](../00-03-review-pr/SKILL.md) — **stub**: not yet adopted in this repo. Reviews the resulting implementation for compliance.
- [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) — the project's plain PR template (used by existing simple PR files)
- [templates/pr-template.md](./templates/pr-template.md) — the bundled, project-adapted richer template this skill fills in
- [references/examples.md](./references/examples.md) — worked examples
- Existing PR files in `.github/.requirements/pr/` — read 2–3 of these before writing to absorb the project's phrasing conventions