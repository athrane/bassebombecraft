---
name: create-pr-from-build-errors
description: "Runs the Gradle build, picks the top 3 compilation errors caused by a Minecraft/Forge version upgrade, and writes a PR requirements file in .github/.requirements/pr/ for each. Use when asked to turn build errors into PR requirements or to capture the next three upgrade fixes."
argument-hint: "[start-issue-number]"
disable-model-invocation: true
---

# Create PR Requirements from Gradle Build Errors

Analyse the Gradle build output, identify the three highest-impact compilation errors introduced by a Minecraft or Forge upgrade, and write one PR requirements file per error. Ranking criteria and common API replacements are in the [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) skill; load it first.

## Inputs

`$ARGUMENTS` — the first issue number to use, e.g. `1443`. Each later file takes the next number. If empty, find the highest numeric prefix in `.github/.requirements/pr/` and add 1.

## Workflow

1. **Run the build** and capture all output:
   ```
   ./gradlew build --info
   ```
   If it fails at configuration, before compiling (e.g. cannot create task), run `./gradlew compileJava --info` instead.
2. **Group the errors by root cause**, not by occurrence. The same missing import in six files is one error.
3. **Rank the groups** and keep the top three, in this order of priority:
   1. Blast radius — how many files one root cause breaks
   2. Subsystem criticality — networking, entity AI and rendering outrank peripheral utilities
   3. Actionability — clear 1:1 renames outrank errors needing architectural investigation
4. **Resolve the numbers.** Use `$ARGUMENTS` or `max + 1` as described above. Never reuse an existing number.
5. **Write one file per error**, in rank order, to `.github/.requirements/pr/<number>-<kebab-description>.md`. The description is 3 to 6 words, e.g. `replace-renderworldlastevent`. Fill in every section of [pr-template-generic.md](../00-00-create-pr/templates/pr-template-generic.md):
   - Start the file with the PR title as `# fix(<scope>): <description>`
   - Summary, Motivation (which Forge/Minecraft API changed), Changes (one checklist item per file and transformation)
   - Type of Change: Bug fix
   - Testing: the Gradle commands and any in-game check; do not plan unit tests, since the project has no test setup
   - Related Issues: `Closes #<number>`
   - Additional Notes: every affected file, and any replacement API you could not verify against Forge 40.3.0 / Minecraft 1.18.2
6. **Report** each file path, its PR title, and how many files its error group affects.

## Guard rails

- Only create files in `.github/.requirements/pr/`; do not touch source files.
- Leave checklist boxes unchecked.
- Do not invent replacement APIs. Flag unverified ones in Additional Notes.
- If fewer than three root causes exist, write as many files as there are and say so.

## Validation

- Each file exists at the expected path and contains every template section.
- The `Closes #` number matches the filename number.
- Titles follow Conventional Commits: `fix(<scope>): <description>`.
- No two files describe the same root cause.
