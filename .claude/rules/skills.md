---
paths:
  - ".claude/skills/**"
---

# Skill authoring rules

Skills live in `.claude/skills/<skill-name>/SKILL.md`. Each skill has its own directory. A skill can be invoked by the model when its description matches, or by the user as `/<skill-name>`. This project has no separate prompt files: a task the user starts by hand is a skill with `disable-model-invocation: true`.

## Frontmatter

```yaml
---
name: skill-name
description: What it does and when to use it, with the keywords a user would say.
---
```

| Field | Notes |
|-------|-------|
| `name` | Lowercase letters, digits and hyphens; matches the directory name; max 64 characters |
| `description` | **What** the skill does, **when** to use it, and trigger keywords; keep under 1024 characters. Claude decides whether to load the skill from this text alone, so a vague description means the skill never runs |
| `disable-model-invocation` | `true` for workflows with side effects that only the user should start (writing files in bulk, pushing to GitHub) |
| `argument-hint` | Shown while typing the command, e.g. `[start-issue-number]` |
| `allowed-tools` | Minimum tools the skill needs without prompting, e.g. `Bash(./gradlew *)` |

Read the arguments in the body with `$ARGUMENTS`. State what to do when they are missing.

## Body

- Imperative mood ("Run", "Create"); specific commands with exact parameters.
- Recommended sections: when to use, prerequisites, numbered workflow, guard rails, troubleshooting, validation checklist, related documents.
- Keep `SKILL.md` under 500 lines. Move long catalogues and worked examples into `references/` and link to them with relative paths.
- Never claim a check passed unless it ran.

## Bundled resources

| Folder | Purpose |
|--------|---------|
| `scripts/` | Deterministic automation the agent would otherwise rewrite each time. Cross-platform (Node.js here), with `--help`, clear errors and no stored credentials |
| `references/` | Documentation read on demand |
| `templates/` | Scaffolds the agent fills in or modifies |
| `assets/` | Static files used unchanged in output |

## Asking the user

Use `AskUserQuestion` for decisions that are the user's to make. If it is unavailable, ask in plain text and wait for the answer.

## Cross-references

- Refer to sibling skills by name and relative path (`../00-02-grill-me/SKILL.md`).
- Refer to scripts as `.claude/skills/<skill>/scripts/<file>`.
- Requirements files stay in `.github/.requirements/pr/`; they are project records, not agent configuration.

## Before saving

- [ ] `name` matches the directory; `description` states what, when and keywords
- [ ] Every relative link resolves
- [ ] Scripts have `--help`, error handling, no secrets
- [ ] Destructive or outward-facing steps need explicit user confirmation
