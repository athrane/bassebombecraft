## Summary
Fix unresolvable import `net.minecraftforge.common.extensions.IForgeContainerType` in `RegisteredContainers.java` by replacing it with the correct Forge 40.x API.

## Motivation
In Minecraft Forge 40.x (1.18.2), Minecraft renamed the "container" concept to "menu" (`ContainerType` → `MenuType`, `AbstractContainer` → `AbstractContainerMenu`, etc.). Forge followed suit and renamed `IForgeContainerType` to `IForgeMenuType`. The old import `net.minecraftforge.common.extensions.IForgeContainerType` no longer exists in Forge 40.3.0, causing a compilation failure.

## Changes
- [ ] Change 1: Replace import `net.minecraftforge.common.extensions.IForgeContainerType` with `net.minecraftforge.common.extensions.IForgeMenuType` in `RegisteredContainers.java`
- [ ] Change 2: Replace usage `IForgeContainerType.create(...)` with `IForgeMenuType.create(...)` in `RegisteredContainers.java`

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

**Test coverage**: Compilation fix only — no behavioral change expected.

## Related Issues
Closes #1441

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
`fix(container): Replace IForgeContainerType with IForgeMenuType for Forge 40.x`

## Additional Notes
The rename is a direct 1:1 API replacement. `IForgeMenuType.create(CompositeMagicItemContainer::new)` is the equivalent call in Forge 40.x (1.18.2). No logic changes are required — only the import and the reference to the class need updating in `src/main/java/bassebombecraft/inventory/container/RegisteredContainers.java`.
