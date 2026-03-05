# fix(ci): Fix three GitHub Actions workflow failures in build.yml

## Summary
The GitHub Actions workflow in `.github/workflows/build.yml` contains three separate defects that collectively cause the CI pipeline to fail: use of the deprecated `::set-output` command, multi-line JSON output being written to `$GITHUB_OUTPUT`, and a step that deletes `gradle.properties` before the Gradle build runs.

## Motivation
Three distinct errors were observed across workflow runs:

**Error 1 — Deprecated `set-output` command:**
```
Warning: The `set-output` command is deprecated and will be disabled soon.
Please upgrade to using Environment Files.
```
Six steps used the deprecated `echo ::set-output name=key::value` syntax. GitHub has disabled this command, causing each of those steps to silently produce no output, breaking all downstream steps that reference them.

**Error 2 — Multi-line JSON value rejected by `$GITHUB_OUTPUT`:**
```
Error: Unable to process file command 'output' successfully.
Error: Invalid format '  "modVersion": "1.18.2-4.1",'
```
After migrating to `$GITHUB_OUTPUT`, the `read_version_json` step used `jq '.'` (pretty-printed, multi-line), which is invalid in the environment file format. Each value must be a single line.

**Error 3 — `gradle.properties` deleted before build:**
```
Build file '/home/runner/work/bassebombecraft/bassebombecraft/build.gradle' line: 11
* What went wrong:
A problem occurred evaluating root project 'bassebombecraft'.
> Could not get unknown property 'mod_version' for root project 'bassebombecraft' of type org.gradle.api.Project.
```
A step explicitly deleted `gradle.properties` before `./gradlew build`. This file is required by `build.gradle` for at least ten properties (`mod_version`, `mod_group_id`, `mod_id`, `minecraft_version`, `forge_version`, `minecraft_version_range`, `forge_version_range`, `loader_version_range`, `mapping_channel`, `mapping_version`).

## Changes

- [x] Change 1: Replace all 6 deprecated `echo ::set-output name=key::value` commands with `echo "key=value" >> $GITHUB_OUTPUT`.
- [x] Change 2: Change `jq '.'` to `jq -c '.'` in the `read_version_json` step to produce compact single-line JSON, which is required by the `$GITHUB_OUTPUT` file format.
- [x] Change 3: Remove the "Delete gradle.properties" step entirely so `./gradlew build` can resolve all required project properties.

### Fix 1 — Replace deprecated `set-output` (before)
```yaml
- name: Read version.json into output variable
  id: read_version_json
  run: echo ::set-output name=json::$(cat ./version.json | jq '.')

- name: Read MOD version into output variable
  id: read_mod_version
  run: echo ::set-output name=version::$(cat ./version.json | jq -r '.modVersion')
```

### Fix 1 — Replace deprecated `set-output` (after)
```yaml
- name: Read version.json into output variable
  id: read_version_json
  run: echo "json=$(cat ./version.json | jq -c '.')" >> $GITHUB_OUTPUT

- name: Read MOD version into output variable
  id: read_mod_version
  run: echo "version=$(cat ./version.json | jq -r '.modVersion')" >> $GITHUB_OUTPUT
```

### Fix 2 — Compact JSON (change within Fix 1 above)
`jq '.'` → `jq -c '.'` ensures the JSON value is emitted on a single line, which is the format required by `$GITHUB_OUTPUT`.

### Fix 3 — Remove gradle.properties deletion (before)
```yaml
- name: Delete gradle.properties
  run: rm ./gradle.properties

- name: Build with Gradle
  run: ./gradlew build
```

### Fix 3 — Remove gradle.properties deletion (after)
```yaml
- name: Build with Gradle
  run: ./gradlew build
```

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: Push a commit and confirm the GitHub Actions workflow completes without any `set-output` deprecation warnings, `Invalid format` errors, or `Could not get unknown property` errors. Confirm the `Build with Gradle` step succeeds and artifacts are uploaded correctly.

## Related Issues
Closes #1459

## Checklist
- [x] Code follows project conventions and Minecraft Forge best practices
- [x] Code compiles without errors (`./gradlew build` passes)
- [x] Code follows Java style guidelines
- [ ] All tests pass (`./gradlew test` passes)
- [x] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [x] No breaking changes (or documented in PR description)
- [x] Commit messages follow Conventional Commits format

## Additional Notes
`version.json` serves as the canonical version source for CI pipeline outputs (read via `jq` into `$GITHUB_OUTPUT`). `gradle.properties` serves as the canonical version source for the Gradle build itself. Both coexist without conflict — they serve different consumers.
