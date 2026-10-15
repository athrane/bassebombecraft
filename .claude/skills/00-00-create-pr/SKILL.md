---
name: 00-00-create-pr
description: "Generates a complete PR requirements file in .github/.requirements/pr/ from a plain-language problem description or a Gradle build error: type, scope, affected files, implementation/test/doc plans. Use to draft a new BasseBombeCraft PR requirements file."
---

# Skill: Generate Pull Request Description

Produce a properly formatted pull request requirements file that adheres to the BasseBombeCraft PR template, including a detailed implementation plan, test plan, and documentation plan derived from codebase analysis. The agent infers everything — type, scope, affected files, implementation plan, test plan, documentation plan, and related-issue handling — from the problem description and codebase. No further input is required from the user beyond the initial problem description.

## When to Use This Skill

- The user asks to create/draft a PR description or PR requirements
- The user wants a `.github/.requirements/pr/` file generated for a planned change (a feature, a bug, a Forge/Minecraft API migration step, a CI fix)
- The user is starting the `00-00-create-pr` → `00-01-implement-pr` workflow

For a batch of compile errors straight from `./gradlew build`, the [`create-pr-from-build-errors`](../create-pr-from-build-errors/SKILL.md) prompt and the [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) skill rank the errors and write the top three requirements files; use this skill for everything that is a single, already-understood change.

If the scope is too large or too fogged-in for one PR — several interdependent, unresolved design questions spanning multiple files or PRs — chart a map with [`00-05-wayfinder`](../00-05-wayfinder/SKILL.md) first, then invoke this skill once per resolved ticket to produce each implementable PR.

## Prerequisites

| Resource | Purpose |
|----------|---------|
| [pr-template.md](./templates/pr-template.md) | Canonical PR requirements template (this skill's own template; extends [pr-template-generic.md](./templates/pr-template-generic.md)) |
| [project-patterns.md](../00-01-implement-pr/references/project-patterns.md) | Lists the project's code patterns (Operator2/Ports, DeferredRegister, event buses, proxies) the Implementation Plan should reference |
| [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) | Forge API migration patterns, when the change is a version-upgrade fix |
| `src/main/java/bassebombecraft/` | Existing files in the affected package, found via codebase search |
| `.github/.requirements/pr/` | Existing requirements files — used to pick the next number and to avoid overlapping scope |

## Step-by-Step Workflow

1. **Read the template** — read [pr-template.md](./templates/pr-template.md).
2. **Analyze problem and explore codebase**
   - Identify the primary package(s) affected: `item`, `entity`, `block`, `event`, `client`, `operator`, `player`, `world`, `config`, `network`, `projectile`, `potion`, `sound`, `structure`, `inventory`, `proxy`, `tab`, or the repo root (`build.gradle`, `gradle.properties`, `.github/workflows/`)
   - Search `src/main/java/bassebombecraft/` for existing files in that package and read one representative class
   - Identify the applicable patterns (`DeferredRegister` in a `Registered*` class, `@Mod.EventBusSubscriber`, `Operator2`/`Ports`, `SimpleChannel` packets, `ClientProxy`/`ServerProxy`, `ForgeConfigSpec`)
   - For a Minecraft/Forge version-upgrade fix, consult the migration patterns in [forge-api-migration-patterns.md](../forge-upgrade-pr/references/forge-api-migration-patterns.md)
   - Infer the Conventional Commits type (`feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `perf`, `style`) and scope (the package or area above)
3. **Generate the PR title** — `<type>(<scope>): <description>`, imperative mood, Conventional Commits format.
4. **Generate the PR description**, starting with the PR title as a level-1 heading (`# <type>(<scope>): <description>`) on the file's first line, then filling every section of the template:
   - **Summary / Motivation** — derived from the problem description (for a build failure, quote the exact compiler error)
   - **Changes** — every affected file under `### Files Deleted` / `### Files Updated` (new files get descriptions starting with `New ...`)
   - **Type of Change** — mark the single most appropriate checkbox
   - **Implementation Plan** — phased, ordered steps (dependencies before dependents: enums → interfaces → classes → registration → event wiring → tests)
   - **Testing** — the tests to add or update, concrete cases, the Gradle commands that must pass, and a manual in-game validation table (`./gradlew runClient`). This project currently has **no `src/test` directory and no JUnit/Mockito dependency in `build.gradle`**: do not plan unit tests unless the PR itself adds the test dependency, and say so in the section instead
   - **Documentation Plan** — a `| File | Changes |` table for every affected doc (e.g. `README.md`); omit the section entirely if none apply
   - **Related Issues** — `Closes #N` / `Related to #N` if known, else `_None._`
5. **Optionally stress-test the plan** — if the Implementation Plan involves significant or ambiguous design decisions, invoke the [`00-02-grill-me`](../00-02-grill-me/SKILL.md) skill to interview the user before finalizing the file.
6. **Format and write the output**
   - Number the file with the GitHub issue number when the user gave one; otherwise use the highest numeric prefix in `.github/.requirements/pr/` plus 1 (this matches `create-pr-from-build-errors`)
   - Write to `.github/.requirements/pr/[issue-number]-[kebab-case-keywords].md`, with the PR title as the file's first line (`# <type>(<scope>): <description>`) — do not wrap the file in a code fence
   - Verify every required section is populated, then report the file path and PR title

See [examples.md](./references/examples.md) for two worked examples (an API-migration fix and a feature) showing the expected level of detail.

## Guard Rails

- Do not create the actual PR — this skill only generates the PR requirements file
- Only create/modify files in `.github/.requirements/pr/`; never touch project source files
- Do not ask the user for information inferable from the codebase or problem description
- If scope is genuinely ambiguous, ask exactly one clarifying question
- Leave all checklist items unchecked (the implementer ticks them)
- Never reuse an existing number; check `.github/.requirements/pr/` first
- Do not invent Forge APIs: when naming the replacement class or method for a removed API, confirm it against the Forge 40.3.0 / Minecraft 1.18.2 sources or docs, and flag it as unverified in Additional Notes if you could not

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Problem description too vague to infer the package | Ask one targeted clarifying question |
| Template cannot be read | Report the error and fall back to the built-in section list above |
| Codebase search returns no relevant files | Note the assumption explicitly and proceed with best inference |
| The replacement Forge API is uncertain | Add a `research`-style note under Additional Notes, or chart it with `00-05-wayfinder` if several such unknowns block each other |
| Requested change would need unit tests but there is no test setup | Put "add JUnit 5 dependency" in the Implementation Plan as its own phase, or state that no tests are planned and why |

## Validation Checklist

- [ ] `.github/.requirements/pr/` exists
- [ ] Filename matches `[issue-number]-[kebab-case-keywords].md` and the number is unused
- [ ] File's first line is the PR title as a level-1 heading (`# <type>(<scope>): <description>`)
- [ ] All required template sections are present and populated (no placeholder text)
- [ ] Type of Change has exactly the appropriate box marked
- [ ] Implementation Plan phases are dependency-ordered
- [ ] Testing section lists the Gradle commands plus a manual in-game validation table
- [ ] Documentation Plan present only when documentation changes are required
- [ ] PR title follows `<type>(<scope>): <description>`

## Related Documents

- [00-05-wayfinder/SKILL.md](../00-05-wayfinder/SKILL.md) — charts oversized, multi-session scope into resolvable tickets before this skill produces an implementable PR per ticket
- [00-01-implement-pr/SKILL.md](../00-01-implement-pr/SKILL.md) — reads and executes the requirements file this skill produces
- [00-02-grill-me/SKILL.md](../00-02-grill-me/SKILL.md) — invoked at Step 5 to stress-test an ambiguous implementation plan before writing the file
- [00-06-sync-issue-description/SKILL.md](../00-06-sync-issue-description/SKILL.md) — optional follow-up: pushes this file's plan (and title) to its GitHub issue after it's generated or revised
- [forge-upgrade-pr/SKILL.md](../forge-upgrade-pr/SKILL.md) — ranks Gradle build errors from a Forge/Minecraft upgrade
- [`create-pr-from-build-errors`](../create-pr-from-build-errors/SKILL.md) — batch variant for build errors
- [pr-template.md](./templates/pr-template.md) — the bundled template this skill fills in
- [examples.md](./references/examples.md) — worked examples
