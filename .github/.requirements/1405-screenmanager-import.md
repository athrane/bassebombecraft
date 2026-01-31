# fix(minecraft): Resolve ScreenManager import error

## Summary
Fixes the unresolved import error for `net.minecraft.client.gui.ScreenManager` which is preventing the project from building successfully.

## Motivation
The project currently fails to build due to a missing or incorrect import statement for `ScreenManager`. This class may have been moved, renamed, or removed in the Minecraft Forge version being used (1.17.1), requiring code updates to use the correct API.

## Changes
- [ ] Investigate the correct replacement for `ScreenManager` in Minecraft 1.17.1
- [ ] Update import statements to use the correct class/package
- [ ] Update code that uses `ScreenManager` to match the new API (if changed)
- [ ] Verify all screen registration code compiles successfully

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
- [ ] Project builds successfully with Gradle (`./gradlew build`)
- [ ] No compilation errors related to ScreenManager
- [ ] Manual testing in Minecraft to verify screens still function correctly
- [ ] All existing tests passing

**Test coverage**: Build validation and runtime screen functionality

## Related Issues
Issue #123 - ScreenManager import cannot be resolved

## Checklist
- [ ] Code follows project conventions
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] All tests pass
- [ ] Build succeeds
- [ ] Documentation updated (if API usage changed)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes

### Background
The error `The import net.minecraft.client.gui.ScreenManager cannot be resolved` indicates that:
1. The `ScreenManager` class has been moved or renamed in Minecraft Forge 1.17.1
2. In Minecraft 1.14-1.16, `ScreenManager` was used to register screens
3. In Minecraft 1.17+, screen registration changed to use `MenuScreens` instead

### Expected Resolution
Replace `net.minecraft.client.gui.ScreenManager` with `net.minecraft.client.gui.screens.MenuScreens` and update the registration method calls accordingly.

### Files Likely Affected
- Client-side initialization code
- Screen/GUI registration code
- Any custom screen factories
