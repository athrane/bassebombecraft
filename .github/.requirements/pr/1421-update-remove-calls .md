# fix(entity): Correct Entity.remove() method invocation

## Summary
Fixes a build error where the `Entity.remove()` method was being called without the required `Entity.RemovalReason` parameter, causing compilation to fail.

## Motivation
The build was failing due to incorrect usage of the `Entity.remove()` method. The Minecraft Entity API requires a `RemovalReason` parameter to be passed when removing an entity, but the method was being called with no arguments (or incorrect arguments).

## Changes
- [ ] Updated `Entity.remove()` calls to include the required `Entity.RemovalReason` parameter
- [ ] Identified and corrected all instances where the method was called incorrectly
- [ ] Selected appropriate removal reasons based on the context of each removal

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
- [x] Manual testing performed (build compilation verified)
- [x] All tests passing

**Test coverage**: Build error resolved, project compiles successfully.

## Related Issues
Fixes build compilation error: `The method remove(Entity.RemovalReason) in the type Entity is not applicable for the arguments()`

## Checklist
- [x] Code follows project conventions
- [x] Code compiles without errors
- [ ] All tests pass
- [ ] JSDoc comments added for public APIs (if applicable)
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
This fix addresses a breaking change in the Minecraft/Forge Entity API where the `remove()` method signature changed to require an explicit `Entity.RemovalReason` parameter. Common removal reasons include:
- `Entity.RemovalReason.KILLED` - Entity was killed
- `Entity.RemovalReason.DISCARDED` - Entity removed programmatically
- `Entity.RemovalReason.CHANGED_DIMENSION` - Entity moved to another dimension
- `Entity.RemovalReason.UNLOADED_TO_CHUNK` - Entity unloaded with chunk
- `Entity.RemovalReason.UNLOADED_WITH_PLAYER` - Entity unloaded with player

The appropriate removal reason should be selected based on the context of each entity removal operation.
