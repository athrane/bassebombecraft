# chore(docker): Update Docker image to use Minecraft Forge 1.17.1 and Java 17

## Summary
Updates the Docker container base image from Java 8 to Java 17 to support Minecraft Forge 1.17.1, enabling compatibility with newer Minecraft versions and modern Java features.

## Motivation
Minecraft Forge 1.17.1 requires Java 17 as the minimum runtime version. The current Docker image uses Java 8, which is incompatible with Forge 1.17.1 and prevents the mod from running in containerized environments on newer Minecraft versions. This update ensures the Docker container can support modern Minecraft Forge versions.

## Changes
- [x] Change 1: Update base Docker image from `itzg/minecraft-server:java8` to `itzg/minecraft-server:java17`
- [x] Change 2: Verify compatibility with Minecraft Forge 1.17.1
- [x] Change 3: Ensure build arguments and environment variables remain compatible

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [x] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement
- [x] Dependency/tooling update (chore)

## Testing
Describe how the changes were tested:
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [x] Manual testing performed - Docker container built and tested with Forge 1.17.1
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: Docker container builds successfully and runs Minecraft server with Forge 1.17.1 and the mod loaded correctly.

## Related Issues
Related to modernization efforts and Minecraft version updates.

## Checklist
- [x] Code follows project conventions and Minecraft Forge best practices
- [x] Code compiles without errors (`./gradlew build` passes)
- [x] Code follows Java style guidelines
- [ ] All tests pass (`./gradlew test` passes)
- [x] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs (N/A - Docker configuration only)
- [x] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [x] Commit messages follow Conventional Commits format

## Additional Notes
This is an infrastructure update that focuses solely on the Docker container configuration. No Java code changes are required for this update. The change ensures that the containerized version of BasseBombeCraft can run on the same Java version as the standalone mod, maintaining consistency across deployment methods.

**Breaking Change Note**: Users running the old Docker image will need to pull the new image. Any existing containers based on Java 8 should be rebuilt using the updated Dockerfile.
