# fix(ci): Fix GitHub Actions failing to find JAR artifact after build

## Summary
The GitHub Actions workflow in `.github/workflows/build.yml` fails to upload the build artifact because the JAR filename produced by Gradle does not match the filename the workflow expects. The `upload-artifact` step warns that no file is found at `./build/libs/BasseBombeCraft-1.18.2-4.1.jar` and no artifact is uploaded.

## Motivation
The following warning was observed in the GitHub Actions run:

```
##[warning]No files were found with the provided path: ./build/libs/BasseBombeCraft-1.18.2-4.1.jar. No artifacts will be uploaded.
```

**Root cause — archive name mismatch:**

The workflow step `read_jar_name` constructs the expected filename as:

```yaml
- name: Read jar name into output variable
  id: read_jar_name
  run: echo "jar_file=BasseBombeCraft-${{ steps.read_mc_version.outputs.version }}-${{ steps.read_mod_version.outputs.version }}.jar" >> $GITHUB_OUTPUT
```

This produces `BasseBombeCraft-1.18.2-4.1.jar` (capital B, capital C).

However, `build.gradle` sets the archive base name to `mod_id`:

```groovy
base {
    archivesName = mod_id
}
```

With `mod_id=bassebombecraft` in `gradle.properties`, Gradle produces `bassebombecraft-1.18.2-4.1.jar` (all lowercase). The `upload-artifact` step looks for `BasseBombeCraft-1.18.2-4.1.jar`, which does not exist, causing the warning and skipping artifact upload entirely. The same mismatch affects the `*-server.jar` artifact upload step.

## Changes

- [x] Change 1: In `build.gradle`, change `archivesName = mod_id` to `archivesName = 'BasseBombeCraft'` so the JAR filename produced by Gradle matches the filename the workflow expects.

### Fix — Correct archive base name (before)
```groovy
base {
    archivesName = mod_id
}
```

### Fix — Correct archive base name (after)
```groovy
base {
    archivesName = 'BasseBombeCraft'
}
```

With this change, Gradle produces:
- `BasseBombeCraft-1.18.2-4.1.jar` — regular reobfuscated mod jar
- `BasseBombeCraft-1.18.2-4.1-server.jar` — shadow jar with bundled HTTP dependencies

Both filenames now match the paths used in the `upload-artifact` steps.

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

**Test coverage**: Push a commit and confirm the GitHub Actions workflow completes without the `No files were found` warning. Confirm both `Upload jar artifact` and `Upload server jar artifact` steps succeed and the artifacts (`BasseBombeCraft-1.18.2-4.1.jar` and `BasseBombeCraft-1.18.2-4.1-server.jar`) are visible in the Actions run summary.

## Related Issues
Closes #1461

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
`mod_id` (`bassebombecraft`, all lowercase) is the correct Minecraft mod identifier and must remain lowercase. `archivesName` is the Gradle archive base name used purely for the output JAR filename and is independent of the mod ID. Setting it to `'BasseBombeCraft'` aligns the build output with the convention already assumed by the CI workflow.
