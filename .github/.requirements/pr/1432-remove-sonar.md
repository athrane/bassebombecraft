# chore: Remove SonarQube integration from project

## Summary
This PR removes SonarQube/SonarCloud integration from the BasseBombeCraft project, including the build workflow, Gradle configuration, and SonarCloud badges from README.

## Motivation
SonarQube analysis is no longer needed for this project. Removing it simplifies the build pipeline, reduces external dependencies, and eliminates the need to maintain SonarCloud credentials and configuration.

## Changes
- [ ] Remove `sonar_job` from GitHub Actions workflow (`.github/workflows/build.yml`)
- [ ] Remove SonarQube plugin from `build.gradle`
- [ ] Remove SonarQube configuration block from `build.gradle`
- [ ] Remove SonarCloud badges from `README.md` (Quality Gate Status, Maintainability Rating, Lines of Code)

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [x] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
Describe how the changes were tested:
- [ ] Unit tests added/updated (N/A - no code changes)
- [ ] Integration tests added/updated (N/A - no code changes)
- [ ] Manual testing performed in-game (N/A - build configuration only)
- [x] All tests passing (`./gradlew test`)

**Test coverage**: No changes to test coverage; this is a build configuration change only.

## Related Issues
Closes #1432

## Checklist
- [x] Code follows project conventions and Minecraft Forge best practices
- [x] Code compiles without errors (`./gradlew build` passes)
- [x] Code follows Java style guidelines
- [x] All tests pass (`./gradlew test` passes)
- [x] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs (N/A - no code changes)
- [x] Updated documentation (if applicable)
- [x] No breaking changes (or documented in PR description)
- [x] Commit messages follow Conventional Commits format

## Additional Notes
This change only affects the build pipeline and documentation. The actual application code remains unchanged. The removal of SonarCloud integration:
- Reduces build time by eliminating the Sonar analysis job
- Removes dependency on `SONAR_TOKEN` secret in GitHub Actions
- Simplifies the Gradle build script
- Updates README to remove outdated quality metrics badges

After this PR is merged, the `SONAR_TOKEN` secret can be safely removed from the repository settings.
