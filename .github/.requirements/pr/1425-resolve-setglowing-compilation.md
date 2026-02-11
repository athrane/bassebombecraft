# fix(potion): Resolve setGlowing method compilation error in MobPrimingEffect

## Summary
Fixes a Java compilation error where the method `setGlowing(boolean)` is undefined for the type `LivingEntity` in the MobPrimingEffect class. This error prevents the project from building successfully.

## Motivation
The build is failing with a compilation error in [MobPrimingEffect.java](src/main/java/bassebombecraft/potion/effect/MobPrimingEffect.java#L87). The `setGlowing(boolean)` method call is not recognized for the `LivingEntity` type, likely due to an API change in the Minecraft/Forge version being used. This needs to be resolved to restore the build.

## Changes
- [ ] Fix the `setGlowing()` method call in MobPrimingEffect.java
- [ ] Update implementation to use the correct API for setting entity glowing effect
- [ ] Verify compatibility with current Minecraft/Forge version

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
- [ ] Manual testing performed
- [ ] All tests passing (`./gradlew test`)
- [ ] Build succeeds (`./gradlew build`)

**Test coverage**: N/A - Compilation fix

## Related Issues
Related to build compilation error: `The method setGlowing(boolean) is undefined for the type LivingEntity`

## Checklist
- [ ] Code follows project conventions
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] All tests pass (`./gradlew test` passes)
- [ ] Code compiles without errors
- [ ] No breaking changes (or documented in PR description)
- [ ] Updated documentation (if applicable)

## Additional Notes
**Error Details**:
- File: `src/main/java/bassebombecraft/potion/effect/MobPrimingEffect.java`
- Line: 87
- Error: `The method setGlowing(boolean) is undefined for the type LivingEntity`

The `setGlowing()` method may have been moved to a different API or replaced with an alternative approach in the current Minecraft/Forge version. The fix will need to use the appropriate API method for setting the glowing effect on entities.
