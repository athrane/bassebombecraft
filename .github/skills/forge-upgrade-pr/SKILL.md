---
name: forge-upgrade-pr
description: Toolkit for analysing Minecraft Forge mod build failures caused by API version upgrades and generating PR requirement descriptions. Use when asked to triage Gradle build errors from a Minecraft/Forge version upgrade, rank compilation errors by impact, identify removed or renamed Forge/NeoForge APIs, or produce PR requirement files from `./gradlew build` output.
license: Complete terms in LICENSE.txt
---

# forge-upgrade-pr

Structured workflow and reference knowledge for converting Minecraft Forge compilation failures — caused by API changes across mod-loader version upgrades — into actionable, numbered PR requirement files.

## When to Use This Skill

- User runs `./gradlew build` and it fails with `cannot find symbol`, `static import only from classes and interfaces`, or `has private access` errors.
- User asks to "create PRs from build errors" or "capture build errors as requirements".
- User is upgrading the mod to a new Forge, NeoForge, or Minecraft version and wants to track each fix as a separate issue.
- User needs to rank or triage a large list of compiler errors.

## Prerequisites

- Gradle wrapper (`./gradlew`) present and functional.
- `.github/.requirements/pr/` directory exists (or will be created).
- `.github/prompts/pr-template.md` present for PR description format.
- Java compiler available on `PATH` (provided by the Forge toolchain).

## Step-by-Step Workflow

### 1. Collect build output

```powershell
./gradlew build --info 2>&1
```

If configuration fails before compilation, run the compile task directly:

```powershell
./gradlew compileJava --info 2>&1
```

Capture the full output including all `error:` lines.

### 2. Group errors by root cause

Parse the compiler output and group individual error lines into **root-cause buckets**:

- Errors sharing the same missing class/symbol → one bucket.
- Errors in different files caused by the same removed API → one bucket.
- Access-control errors on the same field/method → one bucket.

See [forge-api-migration-patterns.md](./references/forge-api-migration-patterns.md) for common Forge API removal/rename patterns and their replacements.

### 3. Rank buckets by impact

Apply criteria in priority order:

| Criterion | Higher rank | Lower rank |
|-----------|-------------|------------|
| **Blast radius** | Affects many files | Affects one file |
| **Subsystem** | Networking, AI, rendering pipeline | Utility helpers |
| **Actionability** | 1:1 rename, clear replacement | Requires architectural change |
| **Cascading** | Unblocks other errors when fixed | Independent |

Select the **top 3** distinct buckets.

### 4. Determine next issue number

1. List files in `.github/.requirements/pr/`.
2. Extract the numeric prefix from each filename.
3. `nextNumber = max(found) + 1`.

If the directory is empty, confirm the starting number with the user.

### 5. Create requirement files

For each ranked bucket, create `.github/.requirements/pr/<nextNumber>-<kebab-name>.md` using the full template from [pr-template.md](../../prompts/pr-template.md). Populate every section:

- **Summary**: What the fix does (1–3 sentences).
- **Motivation**: Which API was removed/changed and why.
- **Changes**: One checklist item per file and transformation.
- **Type of Change**: Mark `Bug fix`.
- **Testing**: Note whether in-game testing is needed.
- **Related Issues**: `Closes #<nextNumber>`.
- **PR Title**: `fix(<scope>): <description>` in Conventional Commits format.
- **Additional Notes**: List all affected files; flag any API methods that need runtime verification.

### 6. Validate and report

After saving all three files confirm:

- File paths created.
- PR title from each file.
- Count of affected files per error group.

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Build fails at configuration, not compilation | Run `./gradlew compileJava --info` directly to bypass task-graph errors |
| Fewer than 3 distinct root causes found | Create files for however many exist; note the shortfall |
| Next issue number unknown | List `.github/.requirements/pr/` and use `max + 1`; ask user if directory is empty |
| API replacement not in patterns reference | Search Forge/NeoForge release notes or diff the Forge changelog for the target version |
| Access-control error (`private access`) | Check if a public accessor method was introduced; prefer the public API over reflection |

## References

- [forge-api-migration-patterns.md](./references/forge-api-migration-patterns.md) — Catalogue of removed/renamed APIs and their replacements by Forge/Minecraft version
- [pr-template.md](../../prompts/pr-template.md) — PR description template used for all output files
