---
name: 00-03-review-pr
description: 'Reviews a PR against the BasseBombeCraft project conventions (Java 17, Forge 1.17.1, project architecture, scope compliance, Javadoc, Conventional Commits) and generates a Markdown compliance report. Third step of the 00-00-create-pr → 00-01-implement-pr → 00-03-review-pr workflow.'
argument-hint: '[pr-number or branch-name]'
---

# Skill: Review PR

Analyse all changed files in a pull request, verify compliance with project conventions, and produce a structured Markdown compliance report identifying conformance, violations, emerging patterns, and documentation gaps. Third step of the [`00-00-create-pr`](../00-00-create-pr/SKILL.md) → [`00-01-implement-pr`](../00-01-implement-pr/SKILL.md) → `00-03-review-pr` workflow.

## When to Use This Skill

- A pull request is ready for review, or code changes need validation against project standards
- Architectural compliance or documentation alignment needs verification
- Continuing the `00-00-create-pr` → `00-01-implement-pr` → `00-03-review-pr` workflow

Skip when there are no file changes in the workspace, or the PR contains only documentation updates and doc review wasn't requested.

## Prerequisites

| Resource | Purpose |
|----------|---------|
| Changed files list (`git diff` or `git diff --name-only main`) | Defines review scope |
| [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) | The plain PR template + Review Checklist |
| `.github/.requirements/pr/[nnnn]-[slug].md` | The matching PR requirements file (if any) — produced by `00-00-create-pr` or graduated from `wayfinder` |
| `src/main/java/bassebombecraft/` | Existing source for pattern matching |
| PR number (optional) and scope (`changed` default, or `full`) | Report naming and analysis breadth |

## Project Conventions Snapshot

Standing facts this skill should check against. Derived from existing `.github/.requirements/pr/14XX-*.md` files plus the codebase; do not invent conventions that contradict these.

| Convention | What to Check |
|------------|---------------|
| **Language & toolchain** | Java 17 only — no Java 8 patterns; `./gradlew build`, `./gradlew test`, `./gradlew compileJava` must pass |
| **Minecraft version** | Forge 1.17.1 baseline — no pre-1.17 OpenGL (`GlStateManager.pushMatrix()` etc.); no deprecated Forge APIs (`Capability.writeNBT`, `RenderSystem.color4f`, `TextureManager.bind`) |
| **Architecture patterns** | `RegisteredItems` / `RegisteredEntities` registration; `TypeUtils`-style validation; operator pattern (`Operator` interface + concrete operators); event-driven side effects |
| **Java style** | Javadoc on public APIs; one top-level class per file; static factory methods (`create()`); immutable records with `Object.freeze(this)` |
| **Commit messages** | `<type>(<scope>): <description>` — Conventional Commits |
| **File scope** | `git diff --stat` must show only files named in the PR requirements file's **Changes** section (excluding pre-existing failures in unrelated files) |
| **Randomness source** | `RandomSource` (Minecraft 1.17.1+ API) where needed — never `Math.random()` |

## Step-by-Step Workflow

1. **Gather changed files** — run `git diff --name-only main` (or `git diff --name-only HEAD~N` for N commits); if scope is `full`, include all `src/main/java/bassebombecraft/**.java` files instead. Categorise by type: source (`src/main/java/bassebombecraft/**.java`), resource (`src/main/resources/**`), configuration (`build.gradle`, `Dockerfile`, `docker-log4j2.xml`, `.github/workflows/**`), documentation (`*.md`).
2. **Load project context**:
   - [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) for the PR spec shape conventions and the Review Checklist
   - The matching `.github/.requirements/pr/[nnnn]-[slug].md` if one exists — to verify scope
   - 2–3 existing `.github/.requirements/pr/14XX-*.md` files for phrasing conventions
3. **Analyse source files** — for each changed source file, verify in order:
   - **Java 17 + Forge 1.17.1 compliance** — no pre-1.17 OpenGL; no deprecated APIs (see Conventions Snapshot)
   - **Architecture patterns** — registration present where applicable; `TypeUtils` validation on cross-boundary inputs; operator pattern used for new behaviour
   - **Java style** — Javadoc on every public class and method; one top-level class per file; static factory methods (`create()`); `Object.freeze(this)` on records
   - **Commit messages** — every commit in `git log` follows Conventional Commits
4. **Analyse documentation files** — for each changed `.md` file:
   - Bracketed markdown links (`[Foo.java](src/main/java/.../Foo.java)`), not bare paths
   - Cross-references resolve to existing files
   - Consistent with the PR spec's **Documentation Plan** section (if a matching PR spec exists)
5. **Identify emerging patterns** — for any structure used in 3+ files that solves a specific recurring problem and follows project principles, record candidates with: name, occurrences, description, benefits, recommendation, example files. If a candidate has more than one viable capture approach, invoke either [`00-02-grill-me`](../00-02-grill-me/SKILL.md) for a one-shot interview, or [`wayfinder`](../wayfinder/SKILL.md) to chart a `grilling` ticket if the capture approach needs deeper multi-session planning.
6. **Identify missing documentation** — check for new domains without `README.md` / wiki entries; Javadoc gaps on public APIs; missing Conventional Commits scope names. If a gap has multiple viable remediation approaches, invoke [`wayfinder`](../wayfinder/SKILL.md) for a `grilling` ticket.
7. **Generate the compliance report** — use [templates/review-report.md](./templates/review-report.md) (this skill's bundled template) and save to:
   - With PR number: `.github/.requirements/reviews/pr-${prNumber}-review-report.md`
   - Fallback (no PR number): `.github/.requirements/reviews/pr-latest-review-report.md`
   - Create the `reviews/` subfolder under `.github/.requirements/` if it doesn't exist

## Templates

| Template | Purpose |
|----------|---------|
| [review-report.md](./templates/review-report.md) | Full compliance report template with all sections and placeholder tokens |

## Output Location

- **With PR number**: `.github/.requirements/reviews/pr-${prNumber}-review-report.md`
- **Fallback (no PR number)**: `.github/.requirements/reviews/pr-latest-review-report.md`

**Report structure**: executive summary with compliance metrics, categorised compliance analysis tables, detailed findings with file/line references, emerging patterns analysis, documentation gap recommendations, prioritised action items, validation pipeline status. GitHub-flavoured Markdown, tables for structured data, emoji severity indicators (🔴 critical, 🟡 high priority, 💡 recommendation), relative links to project docs.

## Guard Rails

- Report generation only — no code modification
- Every changed file must be categorised and analysed before the report is generated
- Leave no placeholder text in the final report; all file paths must be relative and accurate
- Do not invoke `00-02-grill-me` directly unless the user explicitly asks; for pattern / knowledge-capture decisions prefer [`wayfinder`](../wayfinder/SKILL.md)`

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Changed files cannot be retrieved | Ask the user to confirm git repository status; offer to analyse all `src/main/java/bassebombecraft/**.java` files instead (full scope) |
| Matching `.github/.requirements/pr/[nnnn]-[slug].md` not found | Skip scope verification; flag in the report under "Missing PR Spec" |
| Write permissions denied | Suggest an alternative report location, or offer to output the report to chat instead |
| No violations found | Produce a positive report; still check emerging patterns and documentation gaps; note exemplary adherence |

## Validation Checklist

- [ ] `git diff --name-only` returned changed files; `src/main/java/bassebombecraft/` is accessible; write permissions confirmed
- [ ] Report file created at the expected location under `.github/.requirements/reviews/`
- [ ] All file categories analysed (source, resource, configuration, documentation)
- [ ] Compliance tables populated with actual findings — no placeholder text
- [ ] Action items have clear priorities and references
- [ ] All file paths relative and accurate; all documentation references link correctly
- [ ] Emerging patterns documented or explicitly noted as none; documentation gaps likewise

## Related Documents

- [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) — first step of the workflow: generates the PR description
- [`00-01-implement-pr/SKILL.md`](../00-01-implement-pr/SKILL.md) — second step: implements the PR this skill reviews
- [`wayfinder/SKILL.md`](../wayfinder/SKILL.md) — invoked at Steps 5/6 when a knowledge-capture candidate has multiple viable approaches (realised as a `grilling` ticket)
- [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) — the plain PR template (whose Review Checklist this skill automates)
- [`.github/.requirements/pr/`](../../.requirements/pr/) — past PR specs to match scope against

## Maintenance Notes

Update this skill when:
- New compliance checks are added (e.g. SpotBugs, Checkstyle integration)
- The report template changes
- Java or Minecraft version upgrades change the conventions being checked
- New registration patterns emerge (`RegisteredItems`-style additions for new domains)