## Summary
Replace the removed `getMatrix()` accessor on `RenderHighlightEvent.HighlightBlock` with the renamed `getPoseStack()` method in the two renderers that handle block-outline highlight events. This restores compilation for the book and highlighted-block HUD renderers.

## Motivation
Forge renamed `RenderHighlightEvent.HighlightBlock#getMatrix()` to `getPoseStack()` as part of the broader Minecraft 1.20.x PoseStack standardisation. The old accessor no longer exists in the event class, producing a `cannot find symbol: method getMatrix()` compile error in every class that calls it on a `HighlightBlock` event instance.

## Changes
- [ ] Change 1: `BuildMineBookRenderer.java` — replace `event.getMatrix()` with `event.getPoseStack()` on line 127
- [ ] Change 2: `HudItemHighlightedBlockRenderer.java` — replace `event.getMatrix()` with `event.getPoseStack()` on line 115

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [x] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: Verify in-game that the build-mine book outline and the highlighted-block HUD overlay both render correctly. Run `./gradlew build` to confirm zero compilation errors in these two files.

## Related Issues
Closes #1447

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
`fix(rendering): replace removed HighlightBlock.getMatrix() with getPoseStack()`

## Additional Notes
Affected files (2):
- `src/main/java/bassebombecraft/client/event/rendering/BuildMineBookRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/HudItemHighlightedBlockRenderer.java`

Both call sites pass the returned `PoseStack` directly to a `ports.setMatrixStack1()` helper. No import changes or call-site logic changes are required — the fix is a pure accessor rename. Confirm the `PoseStack` type is unchanged in the downstream `setMatrixStack1` parameter signature before or after this change.
