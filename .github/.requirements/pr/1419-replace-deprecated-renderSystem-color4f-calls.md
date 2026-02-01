# fix(rendering): Replace deprecated RenderSystem.color4f() calls

## Summary
Fixes compilation errors in `CompositeMagicItemScreen.java` where the deprecated method `RenderSystem.color4f()` is called. This method has been removed in newer Minecraft versions and needs to be replaced with the modern rendering API equivalent.

## Motivation
The `CompositeMagicItemScreen` class uses the deprecated `RenderSystem.color4f()` method which no longer exists in the current Minecraft rendering API. This causes build failures with multiple "cannot find symbol" errors. The method was part of the legacy OpenGL state management that has been replaced with shader-based color manipulation in modern Minecraft versions.

## Changes
- [x] Replace `RenderSystem.color4f()` calls with `RenderSystem.setShaderColor()`
- [x] Update texture binding from `getMinecraft().getTextureManager().bind()` to `RenderSystem.setShaderTexture(0, ...)`
- [x] Verify CompositeMagicItemScreen.java compiles successfully
- [ ] Test rendering functionality to ensure visual consistency

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
- [x] CompositeMagicItemScreen.java compiles without errors
- [x] No compilation errors in the modified file
- [ ] Manual testing of GUI rendering functionality
- [ ] Verified color rendering matches original visual appearance

**Test coverage**: The modified file has no compilation errors. Build verification confirms the deprecated API calls have been successfully replaced. Manual GUI testing required to verify visual consistency.

## Related Issues
Related to build error: `cannot find symbol: method color4f(float,float,float,float) in class RenderSystem`

Affected file: `src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java` (lines 185, 201, 205, 210, 216)

## Checklist
- [x] Code follows project conventions
- [x] Code compiles without errors in the modified file
- [ ] All tests pass (unrelated pre-existing errors in other files remain)
- [x] No breaking changes introduced
- [ ] Commit messages follow Conventional Commits format

## Additional Notes

### Implementation Summary
Successfully replaced all deprecated rendering API calls in CompositeMagicItemScreen.java:
- 5 instances of `RenderSystem.color4f()` replaced with `RenderSystem.setShaderColor()`
- 4 instances of `getMinecraft().getTextureManager().bind()` replaced with `RenderSystem.setShaderTexture(0, ...)`

All changes are in the following methods:
- `renderSequenceHighlight()` - Lines 185-186
- `renderDecoration()` - Lines 201-202, 205-206
- `renderBg()` - Lines 210, 217

The modified file compiles successfully with no errors. Pre-existing compilation errors in other files (PlayerUtils.java, GenericCompositeProjectileEntity.java, etc.) are unrelated to this PR.

### Build Errors Found
The following errors occur in CompositeMagicItemScreen.java:
- Line 185: `RenderSystem.color4f(oscRgb, oscRgb, oscRgb, 1.0F);`
- Line 186: `getMinecraft().getTextureManager().bind(SEQUENCE_TEXTURE);`
- Line 201: `RenderSystem.color4f(1.0F, oscRgb, 1.0F, 1.0F);`
- Line 202: `getMinecraft().getTextureManager().bind(DECORATION_TEXTURE);`
- Line 205: `RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);`
- Line 206: `getMinecraft().getTextureManager().bind(DECORATION2_TEXTURE);`
- Line 210: `RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);`
- Line 217: `getMinecraft().getTextureManager().bind(BACKGROUND_TEXTURE);`

### Migration Path
In modern Minecraft versions (1.17+), the rendering API has changed:
- `RenderSystem.color4f()` → `RenderSystem.setShaderColor()` for shader-based color control
- `TextureManager.bind()` → `RenderSystem.setShaderTexture(int, ResourceLocation)` for shader texture binding

**Implementation Details:**
- All color values preserved exactly as before to maintain visual consistency
- Texture slot 0 used for all texture bindings (standard for GUI rendering)
- No changes to rendering logic, only API method replacements

### Original Build Errors (Now Fixed)
