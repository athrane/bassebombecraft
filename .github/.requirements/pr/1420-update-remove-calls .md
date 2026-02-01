# fix(entity): Update remove() calls to include RemovalReason parameter

## Summary
Fixed compilation error in `GenericCompositeProjectileEntity` where `remove()` method calls were missing the required `Entity.RemovalReason` parameter, which is now mandatory in the updated Minecraft/Forge API.

## Motivation
The project failed to compile due to API changes in Minecraft/Forge where the `Entity.remove()` method signature changed from `remove()` to `remove(Entity.RemovalReason)`. This breaking change requires all entity removal calls to specify a removal reason.

**Build Error:**
```
The method remove(Entity.RemovalReason) in the type Entity is not applicable for the arguments ()
```

## Changes
- [x] Updated `remove()` call in `cRemovalCallback` consumer (line 186) to use `Entity.RemovalReason.DISCARDED`
- [x] Updated `remove()` call in `handleImpact()` method (line 371) to use `Entity.RemovalReason.DISCARDED`

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [x] Manual testing performed - verified project compiles successfully
- [x] All tests passing
- [ ] Unit tests added/updated (not required for API signature fix)
- [ ] Integration tests added/updated (not required for API signature fix)

**Test coverage**: No new tests required - this is an API compatibility fix.

## Related Issues
Fixes build compilation error in GenericCompositeProjectileEntity.java

## Checklist
- [x] Code follows project conventions
- [x] Code compiles without errors
- [x] No breaking changes to existing functionality
- [x] Appropriate removal reason used (`DISCARDED` for projectile cleanup)

## Additional Notes
The `Entity.RemovalReason.DISCARDED` reason was chosen as it's appropriate for projectiles that are being removed due to expiration or impact. This is consistent with how Minecraft handles projectile removal in similar contexts.

**Affected locations in GenericCompositeProjectileEntity.java:**
1. Line 186: Duration callback when projectile expires - updated in `cRemovalCallback` consumer
2. Line 371: Impact handler when projectile hits target - updated in `handleImpact()` method
