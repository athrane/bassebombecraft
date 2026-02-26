## Summary
Replace all usages of the deprecated-for-removal `RenderLevelLastEvent` class from `net.minecraftforge.client.event` with the Forge 1.18.2 successor event. Seven source files across the world-space rendering pipeline currently import and use this class, producing compiler warnings with `[removal]` indicating it will be removed in a subsequent Forge version.

## Motivation
`net.minecraftforge.client.event.RenderLevelLastEvent` was deprecated and flagged `@Deprecated(forRemoval=true)` in Forge 1.18.2-40.x as part of a rendering-event modernisation. The replacement event in the same Forge line is `net.minecraftforge.client.event.RenderLevelStageEvent`, which fires for each distinct render stage (including `Level.AFTER_ENTITIES`, `Level.AFTER_SKY`, etc.) and provides access to the `PoseStack` and partial tick via `getPoseStack()` and `getPartialTick()`. Leaving the code on the deprecated class will become a compilation error when the removal is executed in a future Forge upgrade.

## Changes
- [ ] Change 1: `EffectRenderer.java` — replace `import net.minecraftforge.client.event.RenderLevelLastEvent` with `import net.minecraftforge.client.event.RenderLevelStageEvent`; update handler parameter type and add stage guard for `RenderLevelStageEvent.Stage.AFTER_ENTITIES`
- [ ] Change 2: `HudItemCharmedInfoRenderer.java` — replace `RenderLevelLastEvent` import and parameter type; add stage guard
- [ ] Change 3: `ParticleRenderer.java` — replace `RenderLevelLastEvent` import and parameter type; add stage guard
- [ ] Change 4: `TargetInfoRenderer.java` — replace `RenderLevelLastEvent` import and parameter type; add stage guard
- [ ] Change 5: `TeamInfoRenderer.java` — replace `RenderLevelLastEvent` import and parameter type; add stage guard
- [ ] Change 6: `RenderTextBillboard2.java` — replace `RenderLevelLastEvent` import and any method signatures that reference the event type
- [ ] Change 7: `RenderingUtils.java` — replace `RenderLevelLastEvent` import and any utility method signatures that reference the event type

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

**Test coverage**: Verify in-game that all world-space visual effects, particles, HUD charmed-item info, target-info overlay, team-info overlay, and billboard text continue to render correctly after the change. Run `./gradlew compileJava` to confirm zero `[removal]` warnings for these files. Confirm the stage guard fires at the expected point in the render loop.

## Related Issues
Closes #1450

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
`fix(rendering): replace deprecated-for-removal RenderLevelLastEvent with RenderLevelStageEvent in world-space renderers`

## Additional Notes
Affected files (7):
- `src/main/java/bassebombecraft/client/event/rendering/effect/EffectRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/HudItemCharmedInfoRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/particle/ParticleRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/TargetInfoRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/TeamInfoRenderer.java`
- `src/main/java/bassebombecraft/client/operator/rendering/RenderTextBillboard2.java`
- `src/main/java/bassebombecraft/client/rendering/RenderingUtils.java`

`RenderLevelStageEvent` provides `getPoseStack()` (a `PoseStack`) and `getPartialTick()` (a `float`) — both equivalent to the fields available on `RenderLevelLastEvent`. A stage guard (e.g. `if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;`) must be added to preserve the original firing-order semantics. Verify the correct stage constant at runtime to avoid rendering artefacts. All five handler method signatures named `handleRenderWorldLastEvent` may also be renamed for clarity.
