# fix(inventory): Fix deprecated writeNBT/readNBT capability methods

## Summary
Fixes build error caused by deprecated `writeNBT` and `readNBT` methods in Minecraft Forge's Capability API that are no longer available in `Capability<IItemHandler>`.

## Motivation
The project fails to compile with the error:
```
The method writeNBT(CompositeMagicItemItemStackHandler, Direction) is undefined for the type Capability<IItemHandler>
```

This is blocking builds and indicates that the Forge API has changed, requiring updates to how NBT serialization/deserialization is handled for the `CompositeMagicItemCapabilityProvider`.

## Changes
- [ ] Update `serializeNBT()` method in `CompositeMagicItemCapabilityProvider` to use current Forge API
- [ ] Update `deserializeNBT()` method in `CompositeMagicItemCapabilityProvider` to use current Forge API
- [ ] Replace deprecated `Capability.writeNBT()` and `Capability.readNBT()` calls with appropriate alternative

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

**Test coverage**: Existing functionality should continue to work - composite magic item NBT serialization/deserialization.

## Related Issues
Closes #999

## Checklist
- [ ] Code follows project conventions
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] All tests pass (`./gradlew test` passes)
- [ ] JavaDoc comments added/updated for modified methods
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes to save data format
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
**Affected File**: `src/main/java/bassebombecraft/inventory/container/CompositeMagicItemCapabilityProvider.java`

**Error Location**: Line 53 in `serializeNBT()` method

The Forge API has evolved and the `writeNBT`/`readNBT` methods are no longer available on the `Capability` class. The fix needs to:
1. Determine the current Forge API version being used
2. Implement the correct serialization approach (likely direct `IItemHandler` serialization)
3. Ensure backward compatibility with existing saved composite magic item data
