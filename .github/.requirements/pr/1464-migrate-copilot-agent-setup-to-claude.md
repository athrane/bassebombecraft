# chore(claude): Migrate GitHub Copilot agent setup to Claude Code

## Summary
Move the agentic configuration in `.github/` (instructions, prompts and skills), which targets GitHub Copilot, to the layout Claude Code reads: a root `CLAUDE.md`, a `.claude/rules/` file and `.claude/skills/`. The mod's source code and CI workflow are unchanged.

## Motivation
Claude Code does not read `.github/instructions/`, `.github/prompts/` or `.github/skills/`. Skills are discovered only under `.claude/skills/`, and project instructions only from `CLAUDE.md` or `.claude/rules/`. Left as is, none of the `00-*` PR workflow skills or the project conventions were available to Claude. Several files also contained Copilot-only wiring: `${input:...}` prompt variables, `agent:` and `model: Claude Sonnet 4.6 (copilot)` frontmatter, `applyTo:` globs, and a `vscode_askQuestions` column in the `00-02-grill-me` skill.

`implement-pr.prompt.md` also contradicted the newer skills. It told the agent to write JUnit 5 and Mockito tests, but the project has no `src/test` directory and no test dependency in `build.gradle`.

## Changes

### Files Deleted

- **Delete `.github/instructions/agent-skills.instructions.md`** — Copilot-specific skill authoring guide (`applyTo` frontmatter); replaced by `.claude/rules/skills.md`.
- **Delete `.github/instructions/prompt.instructions.md`** — Guidelines for Copilot `.prompt.md` files, which Claude Code has no equivalent of; a user-started task is now a skill with `disable-model-invocation: true`, covered by `.claude/rules/skills.md`.
- **Delete `.github/prompts/create-pr-from-build-errors.prompt.md`** — Converted to the `create-pr-from-build-errors` skill.
- **Delete `.github/prompts/implement-pr.prompt.md`** — Duplicated the `00-01-implement-pr` skill; its unique pattern catalogue moved to `references/project-patterns.md`.
- **Delete `.github/prompts/pr-template.md`** — Moved to `.claude/skills/00-00-create-pr/templates/pr-template-generic.md`.

### Files Updated

- **`.claude/skills/00-00-create-pr/`, `00-01-implement-pr/`, `00-05-wayfinder/`, `00-06-sync-issue-description/`, `forge-upgrade-pr/`** — Moved from `.github/skills/`. Links to `.github/prompts/*` now point at the new skill, template or reference files. Script paths changed from `.github/skills/...` to `.claude/skills/...` in `00-06-sync-issue-description/SKILL.md`, `references/examples.md` and the usage comment of `scripts/sync-issue.mjs`.
- **`.claude/skills/00-02-grill-me/SKILL.md`** — Removed the `vscode_askQuestions` column and Copilot wording; `AskUserQuestion` is the only selectable-question tool, with the plain-markdown fallback kept.
- **`.claude/skills/00-06-sync-issue-description/SKILL.md`** — "Copilot fallback" renamed "Fallback".
- **`.claude/skills/00-00-create-pr/templates/pr-template-generic.md`** — New location of the generic PR template (moved from `.github/prompts/pr-template.md`).
- **`.claude/skills/00-00-create-pr/templates/pr-template.md`** — Link to the generic template updated.
- **`.claude/skills/00-05-wayfinder/templates/map-template.md`** — Link to the generic template updated.

### Files Created

- **`CLAUDE.md`** — New. Project overview, build commands, code conventions (taken from the `implement-pr` prompt's guard rails), the PR workflow and a table of the skills.
- **`.claude/rules/skills.md`** — New. Skill authoring rules, scoped with `paths: .claude/skills/**`, adapted from `agent-skills.instructions.md` and `prompt.instructions.md` for Claude Code frontmatter.
- **`.claude/skills/create-pr-from-build-errors/SKILL.md`** — New. Converted from the prompt: `$ARGUMENTS` replaces `${input:startIssueNumber}`, `argument-hint` and `disable-model-invocation: true` added, unit-test steps dropped to match the project's state.
- **`.claude/skills/00-01-implement-pr/references/project-patterns.md`** — New. The pattern catalogue (registration, event buses, operators, config, network, proxies) from `implement-pr.prompt.md`, with the test guidance corrected.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [x] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Relocate the skills

Move each directory under `.github/skills/` to `.claude/skills/`. Move `.github/prompts/pr-template.md` to `.claude/skills/00-00-create-pr/templates/pr-template-generic.md`. Use `git mv` or stage both sides so git records renames.

### Phase 2 — Convert the prompts

Create `.claude/skills/create-pr-from-build-errors/SKILL.md` from the build-errors prompt. Extract the pattern catalogue and guard rails from `implement-pr.prompt.md` into `00-01-implement-pr/references/project-patterns.md`, then delete both prompt files.

### Phase 3 — Replace the instructions

Write `CLAUDE.md` and `.claude/rules/skills.md`. Delete `.github/instructions/`.

### Phase 4 — Fix references

Update every link and script path that still points at `.github/skills/`, `.github/prompts/` or a `.prompt.md` file. Remove Copilot wording from `00-02-grill-me` and `00-06-sync-issue-description`. Then confirm no stale references remain:

```
grep -rniE "\.github/(skills|prompts|instructions)|\.prompt\.md|copilot|vscode_ask" .claude CLAUDE.md
```

Confirm every relative Markdown link under `.claude/` and in `CLAUDE.md` resolves. The only expected non-resolving link is the `<ticket-slug>` placeholder in the wayfinder map template.

### Phase 5 — Validate in Claude Code

Start a new session and check that each skill is listed and can be invoked (see the manual steps below).

## Testing

No unit tests are planned: the project has no `src/test` directory or JUnit dependency, and no Java source changes. The change is configuration and documentation only, so verification is by loading it in Claude Code.

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: None; no code under `src/` changes.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | `CLAUDE.md` is loaded | Start a Claude Code session in the repo root; ask for the build commands. The answer matches `CLAUDE.md` |
| 2 | Skills are discovered | Type `/` and confirm `00-00-create-pr`, `00-01-implement-pr`, `00-02-grill-me`, `00-05-wayfinder`, `00-06-sync-issue-description`, `forge-upgrade-pr` and `create-pr-from-build-errors` are listed |
| 3 | The converted slash command takes an argument | Run `/create-pr-from-build-errors 1500` and confirm it uses 1500 as the first file number (stop after the numbering step if you do not want the build to run) |
| 4 | The rules file scopes correctly | Edit any `SKILL.md`; the skill authoring rules are loaded |
| 5 | The sync script still runs | `node .claude/skills/00-06-sync-issue-description/scripts/sync-issue.mjs --help`, then a `--dry-run` on a requirements file |
| 6 | Build is unaffected | `./gradlew compileJava` gives the same result as before the change |

## Related Issues
Closes #1464

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
- `.github/.requirements/pr/` and `.github/workflows/` stay where they are: the first holds project records that the skills read and write, the second is CI.
- Unverified: the Claude Code frontmatter fields used in `.claude/rules/skills.md` and `create-pr-from-build-errors/SKILL.md` (`paths:`, `disable-model-invocation`, `argument-hint`) were written from memory and not checked against the Claude Code documentation. Manual steps 2 to 4 above exercise them.
- `.claude/settings.local.json` still contains allow-rules for paths in a different repository (`herodotus`). It is a local settings file and was left alone.
- Dropping `implement-pr.prompt.md` also drops its JUnit 5 and Mockito instructions on purpose; see Motivation. If a test setup is added later, restore that guidance in `references/project-patterns.md` (its Tests section).
