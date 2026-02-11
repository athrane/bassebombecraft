# fix(player): Fix Player.inventory visibility error in PlayerUtils

## Summary
Fixes Java build error where `Player.inventory` field is not accessible in `PlayerUtils.isItemInHotbar()` method due to visibility restrictions in the Player class.

## Motivation
The current code attempts to directly access the `Player.inventory` field at line 279, but this field is not visible (private/protected) in the Minecraft Player class. This causes a compilation error: "The field Player.inventory is not visible". The code needs to use the appropriate public accessor method instead.

## Changes
- [ ] Replace direct `player.inventory` field access with public accessor method
- [ ] Update `isItemInHotbar()` method in PlayerUtils.java to use proper API
- [ ] Ensure backward compatibility with existing functionality

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
- [ ] Manual testing performed with Minecraft client
- [ ] All tests passing (`./gradlew test`)
- [ ] Build succeeds without errors (`./gradlew build`)
- [ ] Verified hotbar item detection works correctly in-game

**Test coverage**: Existing method functionality preserved, no new test coverage required.

## Related Issues
Fixes build error: "The field Player.inventory is not visible" at [PlayerUtils.java:279](src/main/java/bassebombecraft/player/PlayerUtils.java#L279)

## Checklist
- [ ] Code follows project conventions
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] All tests pass (`./gradlew test` passes)
- [ ] Javadoc comments updated (if applicable)
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Functionality verified in Minecraft runtime environment

## Additional Notes
**Error Location**: [PlayerUtils.java:279](src/main/java/bassebombecraft/player/PlayerUtils.java#L279)

**Current Code**:
```java
ItemStack itemStackHotbar = player.inventory.getItem(i);
```

**Expected Fix**: Replace with proper public accessor method (e.g., `player.getInventory().getItem(i)` or equivalent Minecraft API method).

This appears to be an API compatibility issue, possibly due to Minecraft/Forge version update where the inventory field visibility was changed.
