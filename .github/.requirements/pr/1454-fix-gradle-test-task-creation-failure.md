## Summary
Fix the Gradle build configuration so that `./gradlew build` can create the `:test` task without throwing a `Type T not present` exception. Currently `./gradlew build` aborts at task-graph resolution with a `DefaultTestTaskReports` / `DefaultReportContainer` instantiation failure, preventing the standard release build from ever completing.

## Motivation
Running `./gradlew build` fails at the configuration phase with:

```
FAILURE: Build failed with an exception.
* What went wrong:
Could not determine the dependencies of task ':check'.
> Could not create task ':test'.
   > Could not create task of type 'Test'.
      > Could not create an instance of type org.gradle.api.internal.tasks.testing.DefaultTestTaskReports.
         > Could not create an instance of type org.gradle.api.reporting.internal.DefaultReportContainer.
            > Type T not present
```

This error is caused by a Gradle API incompatibility introduced when the JVM or Gradle wrapper version was upgraded. `DefaultReportContainer` uses a generic type parameter `T` that is resolved via reflection at runtime; newer JVM versions and certain Gradle 8.x releases changed how generics are resolved in the Reporting API, making the `Test` task fail to instantiate. The `compileJava` task continues to work because it does not touch the reporting infrastructure, so compilation succeeds but no CI or release build can finish.

## Changes
- [ ] Change 1: `build.gradle` — add an explicit `test` task configuration block that sets `reports.html.required = false` and `reports.junitXml.required = false` (or equivalent), OR set `test.enabled = false` if no tests are present, to bypass the broken report container
- [ ] Change 2: `build.gradle` — upgrade the `gradleApiDependency` / verify `gradle-wrapper.properties` pins a Gradle version that is compatible with the JVM in use (currently Java 25, Gradle 8.11.1 — check compatibility matrix)
- [ ] Change 3: `gradle/wrapper/gradle-wrapper.properties` — if the Gradle wrapper version needs to change, update `distributionUrl` to the compatible Gradle release
- [ ] Change 4: `build.gradle` — if the project has no tests, consider removing the `check` lifecycle dependency on `test` to unblock the `build` task without changing the Gradle version

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

**Test coverage**: After the fix, run `./gradlew build` and confirm it reaches `BUILD SUCCESSFUL` without a `Type T not present` exception. Verify that `./gradlew compileJava` and `./gradlew jar` still succeed. If tests are disabled or removed, document the decision in a comment in `build.gradle`.

## Related Issues
Closes #1452

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
`fix(build): resolve Gradle test task Type-T-not-present failure to restore full build`

## Additional Notes
Affected files:
- `build.gradle` — primary fix location
- `gradle/wrapper/gradle-wrapper.properties` — may need distribution URL update

The error is isolated to build configuration and does not affect the compiled mod JAR. Root cause investigation steps:
1. Check `gradle/wrapper/gradle-wrapper.properties` for the current `distributionUrl` and compare against the [Gradle compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html) for Java 25.
2. Run `./gradlew dependencies --configuration compileClasspath` to confirm no classpath conflict with Gradle internals.
3. If Gradle 8.11.1 is not officially compatible with Java 25, the simplest fix may be upgrading the wrapper to Gradle 8.12+ or adding `--add-opens` JVM arguments in `gradle.properties`.
4. As a fallback, adding `test.enabled = false` in `build.gradle` unblocks the build immediately if tests are not required for this mod.
