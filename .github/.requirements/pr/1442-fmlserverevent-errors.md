## Summary
Fix unresolvable imports from the non-existent `net.minecraftforge.fmlserverevents` package in `BassebombeCraft.java` by replacing them with the correct Forge 40.x server lifecycle event API.

## Motivation
In Minecraft Forge 40.x (1.18.2), the FML server lifecycle events were restructured. The package `net.minecraftforge.fmlserverevents` does not exist in Forge 40.3.0. The three server events (`FMLServerAboutToStartEvent`, `FMLServerStartedEvent`, `FMLServerStoppedEvent`) have been replaced by `ServerAboutToStartEvent`, `ServerStartedEvent`, and `ServerStoppedEvent` located in the `net.minecraftforge.event.server` package. The stale imports cause a compilation failure when building the mod.

## Changes
- [ ] Change 1: Replace import `net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent` with `net.minecraftforge.event.server.ServerAboutToStartEvent` in `BassebombeCraft.java`
- [ ] Change 2: Replace import `net.minecraftforge.fmlserverevents.FMLServerStartedEvent` with `net.minecraftforge.event.server.ServerStartedEvent` in `BassebombeCraft.java`
- [ ] Change 3: Replace import `net.minecraftforge.fmlserverevents.FMLServerStoppedEvent` with `net.minecraftforge.event.server.ServerStoppedEvent` in `BassebombeCraft.java`
- [ ] Change 4: Update event handler parameter type in `serverAboutTostart()` from `FMLServerAboutToStartEvent` to `ServerAboutToStartEvent` in `BassebombeCraft.java`
- [ ] Change 5: Update event handler parameter type in `serverStarted()` from `FMLServerStartedEvent` to `ServerStartedEvent` in `BassebombeCraft.java`
- [ ] Change 6: Update event handler parameter type in `serverStopped()` from `FMLServerStoppedEvent` to `ServerStoppedEvent` in `BassebombeCraft.java`

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
Describe how the changes were tested:
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: Compilation fix only — no behavioral change expected. The server lifecycle event callbacks (`serverAboutTostart`, `serverStarted`, `serverStopped`) retain identical logic after the rename.

## Related Issues
Closes #1442

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
`fix(event): Replace fmlserverevents imports with Forge 40.x net.minecraftforge.event.server API`

## Additional Notes
All three changes are 1:1 renames — the method signatures on the event objects (`getServer()` etc.) remain the same. Only the imports and the parameter type declarations in `src/main/java/bassebombecraft/BassebombeCraft.java` need updating. No logic changes are required.
