# fix(rendering): Add missing RenderType import for entityCutout method

## Summary
Fixes a build error in `GenericCompositeProjectileEntityRenderer` where the `entityCutout()` method is undefined due to a missing import statement for `RenderType`.

## Motivation
The class calls `RenderType.entityCutout()` indirectly but lacks the necessary static import. This causes a compilation error: "The method entityCutout(ResourceLocation) is undefined for the type GenericCompositeProjectileEntityRenderer<T>". This prevents the project from building successfully.

## Changes
- [x] Add missing import for `RenderType.entityCutout()` to `GenericCompositeProjectileEntityRenderer`

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [x] Build succeeds after adding the missing import
- [x] Manual testing performed

**Test coverage**: Resolves compilation error; existing runtime behavior unchanged.

## Related Issues
N/A - Build error fix

## Checklist
- [x] Code follows project conventions
- [x] Code compiles without errors
- [x] No breaking changes
- [x] Minimal change to fix build error

## Additional Notes
This is a straightforward import fix to resolve a build error. The `entityCutout()` method from `RenderType` is used to create a render buffer for entity rendering with cutout transparency, which is required for the projectile rendering implementation.

**Error message**: 
```
The method entityCutout(ResourceLocation) is undefined for the type GenericCompositeProjectileEntityRenderer<T>
```

**Fix**: Add static import for `net.minecraft.client.renderer.RenderType.entityCutout` or use fully qualified method call `RenderType.entityCutout()`.
