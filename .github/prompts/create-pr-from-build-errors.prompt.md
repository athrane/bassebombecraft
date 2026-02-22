---
description: 'Run the Gradle build, identify the top 3 compilation errors caused by a Minecraft/Forge version upgrade, and generate a PR requirement description for each error.'
agent: 'agent'
tools: ['run_in_terminal', 'read_file', 'create_file', 'file_search', 'grep_search']
---

# Create PR Requirements from Gradle Build Errors

Analyse the current Gradle build output, identify the three highest-impact compilation errors introduced by a Minecraft or Forge version upgrade, and produce a PR requirement description file for each error.

## Scope & Preconditions

- Workspace is a Minecraft Forge mod project built with Gradle.
- Build tool is `./gradlew` (Unix-style wrapper available on Windows via PowerShell).
- PR requirement files follow the pattern `.github/.requirements/pr/<issue-number>-<kebab-name>.md`.
- PR descriptions must follow the template in [pr-template.md](./pr-template.md).
- The **forge-upgrade-pr** agent skill provides error-ranking criteria and common Forge migration patterns. Load it if available.

## Inputs

- **Starting issue number** `${input:startIssueNumber:Next GitHub issue number (e.g. 1443)}` — first number used to name the three output files. Increment by 1 for each subsequent file. If unknown, detect it by listing `.github/.requirements/pr/` and adding 1 to the highest existing number.

## Workflow

### Step 1 — Run the Gradle build

Run the Gradle build and capture all output:

```
./gradlew build --info
```

If the build fails with a configuration error before compiling (e.g. cannot create task), also run:

```
./gradlew compileJava --info
```

Collect the full compiler error output.

### Step 2 — Identify the top 3 most relevant build errors

From the compiler output, group errors by root cause rather than by individual occurrence. Rank the groups using these criteria (highest priority first):

1. **Blast radius** — how many files or classes are broken by this single root cause.
2. **Subsystem criticality** — errors in core systems (networking, entity AI, rendering pipeline) rank above peripheral utilities.
3. **Actionability** — clear 1:1 API renames rank above errors requiring architectural investigation.

Select the three highest-ranking distinct root causes. Ignore duplicated symptoms that share the same underlying cause (e.g. the same missing import appearing in six files counts as one error).

### Step 3 — Resolve the next issue numbers

If `${input:startIssueNumber}` was not provided or left as placeholder:

1. List all files in `.github/.requirements/pr/`.
2. Parse the numeric prefix from each filename.
3. Set `nextNumber` = highest found + 1.

Otherwise use `${input:startIssueNumber}` as `nextNumber` and increment for each subsequent file.

### Step 4 — Create a PR requirement file for each of the 3 errors

For each error (in ranked order), create a file at:

```
.github/.requirements/pr/<nextNumber>-<kebab-description>.md
```

Where `<kebab-description>` is a short (3–6 word) kebab-case summary of the fix (e.g. `replace-renderworldlastevent`, `migrate-fmllegacy-network-api`).

Each file must be populated using the structure from [pr-template.md](./pr-template.md):

```markdown
## Summary
[1–3 sentences describing what the fix accomplishes]

## Motivation
[Why the error exists — which Forge/Minecraft API changed and why]

## Changes
- [ ] Change 1: [Specific file and transformation]
- [ ] Change 2: ...

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] ...

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: [Brief note on what manual or automated verification is appropriate]

## Related Issues
Closes #[nextNumber]

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] Code follows Java style guidelines
- [ ] All tests pass (`./gradlew test` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## PR Title
`fix(<scope>): <description>`

## Additional Notes
[List all affected files; note any API method verification needed]
```

### Step 5 — Confirm output

After creating all three files, report:
- The path of each file created.
- The PR title line from each file.
- The number of files affected by each error group.

## Output

Three Markdown files saved to `.github/.requirements/pr/`:

| # | File | Covers |
|---|------|--------|
| 1 | `<n>-<name>.md`   | Top-ranked error |
| 2 | `<n+1>-<name>.md` | Second-ranked error |
| 3 | `<n+2>-<name>.md` | Third-ranked error |

## Validation

- Each file exists at the expected path.
- Each file contains all sections from the PR template.
- The `Closes #` line in each file matches its filename number.
- PR titles follow Conventional Commits format `fix(<scope>): <description>`.
- No two files describe the same root cause.
