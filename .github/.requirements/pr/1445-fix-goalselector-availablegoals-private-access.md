## Summary
Replace four direct accesses to the private `GoalSelector.availableGoals` field in `AiUtils.java` with the public API, eliminating the "availableGoals has private access in GoalSelector" compilation error.

## Motivation
In Minecraft 1.18.2 (Forge 40.x), the `availableGoals` field in `net.minecraft.world.entity.ai.goal.GoalSelector` was changed from package-private/accessible to explicitly private. The `AiUtils` class reads and writes this field directly in four places (lines ~100, ~130, ~150, ~170) to enumerate, copy, and modify goal sets. This causes a compilation error. A public accessor was introduced — `GoalSelector.getAvailableGoals()` returns an unmodifiable view — so the read paths can be updated. For write paths (adding goals), the standard `GoalSelector.addGoal(int, Goal)` method must be used instead of direct field mutation.

## Changes
- [ ] Change 1: Replace `selector.availableGoals` read access (~line 100 in `AiUtils.java`) with `selector.getAvailableGoals()` or the correct public accessor
- [ ] Change 2: Replace `Set<WrappedGoal> goals = selector.availableGoals` (~line 130 in `AiUtils.java`) with `Set<WrappedGoal> goals = new HashSet<>(selector.getAvailableGoals())`
- [ ] Change 3: Replace `selector.availableGoals.addAll(goals)` (~line 150 in `AiUtils.java`) with individual `selector.addGoal(goal.getPriority(), goal.getGoal())` calls over `goals`
- [ ] Change 4: Replace `selector.availableGoals.addAll(goals)` (~line 170 in `AiUtils.java`) with individual `selector.addGoal(goal.getPriority(), goal.getGoal())` calls over `goals`
- [ ] Change 5: Add `import java.util.HashSet` to `AiUtils.java` if not already present (needed for defensive copy in Change 2)

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

**Test coverage**: The AI utility functions affected control how goals are read from/written to a `GoalSelector`. Manual in-game testing of mob AI behavior (particularly any spell or item that manipulates mob goals) is required to confirm goal management still works correctly after switching to the public API. Any existing unit tests covering `AiUtils` should be run.

## Related Issues
Closes #1445

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
`fix(ai): Replace private GoalSelector.availableGoals field access with public API in AiUtils`

## Additional Notes
Affected file: `src/main/java/bassebombecraft/entity/ai/AiUtils.java` (4 occurrences at approximately lines 100, 130, 150, 170).

The `WrappedGoal.getPriority()` and `WrappedGoal.getGoal()` accessors should be available in Forge 40.x and allow reconstructing the `addGoal` calls. If `getAvailableGoals()` returns an `ImmutableSet`-backed view, a defensive copy is needed before iterating while modifying. Verify that goal priority is preserved correctly after the migration.
