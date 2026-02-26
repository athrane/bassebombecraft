## Summary
Replace all calls to the removed `getMatrixStack()` method on `RenderLivingEvent.Pre`, `RenderLivingEvent.Post`, and `RenderNameTagEvent.Post` with the renamed `getPoseStack()` method. This affects seven renderer classes in the client rendering pipeline.

## Motivation
Forge/NeoForge renamed `RenderLivingEvent.Pre#getMatrixStack()` and the equivalent method on `Post` to `getPoseStack()` in line with Minecraft's own PoseStack migration. The old method no longer exists in the event API, causing a `cannot find symbol` compilation error in every renderer that accesses the pose stack through the event parameter.

## Changes
- [ ] Change 1: `DecoyRenderer.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on lines 49 and 85
- [ ] Change 2: `DecreaseSizeEffectRenderer.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on lines 41 and 63
- [ ] Change 3: `IncreaseSizeEffectRenderer.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on lines 41 and 63
- [ ] Change 4: `RespawnedRenderer.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on line 110
- [ ] Change 5: `TeamEnityRenderer.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on line 62
- [ ] Change 6: `DebugRenderer_MobLines.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on line 20
- [ ] Change 7: `DebugRenderer_StrangeSize.java` — replace `event.getMatrixStack()` with `event.getPoseStack()` on line 15

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

**Test coverage**: Verify in-game that all affected renderers (decoy, size-effect, respawned, team, and debug renderers) display without visual artefacts or rendering errors. Run `./gradlew build` to confirm zero compilation errors.

## Related Issues
Closes #1446

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
`fix(rendering): replace removed getMatrixStack() with getPoseStack() in render-living event handlers`

## Additional Notes
Affected files (7):
- `src/main/java/bassebombecraft/client/event/rendering/DecoyRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/DecreaseSizeEffectRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/IncreaseSizeEffectRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/RespawnedRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/TeamEnityRenderer.java`
- `src/main/java/bassebombecraft/client/rendering/DebugRenderer_MobLines.java`
- `src/main/java/bassebombecraft/client/rendering/DebugRenderer_StrangeSize.java`

The rename is a pure 1:1 substitution — no import changes are required. Verify at runtime that the `PoseStack` obtained from `getPoseStack()` is the same object used by the parent rendering pass to avoid matrix-stack corruption.
