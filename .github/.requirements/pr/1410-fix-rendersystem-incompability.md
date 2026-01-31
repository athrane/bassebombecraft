# fix(render): Fix RenderSystem.rotatef method signature incompatibility

## Summary
Resolves build error caused by incompatible method signature for `RenderSystem.rotatef()`. The method signature has changed in the current Minecraft/Forge version, requiring migration from the deprecated 4-parameter integer method to the current API.

## Motivation
The build fails with the error: "The method rotatef(int, int, int, int) is undefined for the type RenderSystem". This indicates that the RenderSystem API has been updated and the old method signature is no longer available, preventing successful compilation of the mod.

## Changes
- [ ] Update `RenderSystem.rotatef()` method calls to use the correct signature
- [ ] Replace deprecated integer parameters with float parameters where required
- [ ] Migrate to alternative rendering methods if `rotatef()` has been removed entirely
- [ ] Update any related rendering code to match current API conventions

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
- [ ] Build completes successfully (`./gradlew build`)
- [ ] Manual testing performed in-game to verify rendering behavior
- [ ] No visual regressions in affected rendering code
- [ ] Client runs without crashes

**Test coverage**: Build verification and runtime testing of rendering functionality

## Related Issues
Closes #[issue-number]

## Checklist
- [ ] Code follows project conventions
- [ ] Java code compiles without errors
- [ ] No deprecation warnings for the fixed method calls
- [ ] Rendering functionality works as expected in-game
- [ ] No breaking changes to existing features
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
This fix addresses a Minecraft/Forge API compatibility issue. The `RenderSystem` class has undergone changes in recent versions, and the old `rotatef(int, int, int, int)` method signature is no longer supported. The fix ensures compatibility with the current API version while maintaining the same rendering behavior.

### Technical Details
- **Error**: `The method rotatef(int, int, int, int) is undefined for the type RenderSystem`
- **Likely cause**: Minecraft/Forge version update changed RenderSystem API
- **Solution**: Update to current RenderSystem rendering methods (e.g., using PoseStack transformations or updated method signatures)
