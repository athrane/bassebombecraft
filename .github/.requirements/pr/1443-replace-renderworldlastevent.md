## Summary
Replace the removed `RenderWorldLastEvent` class from `net.minecraftforge.client.event` with the current `RenderLevelLastEvent` equivalent across all affected rendering files.

## Motivation
Minecraft Forge 41.x/42.x removed `RenderWorldLastEvent` from `net.minecraftforge.client.event`. The replacement is `net.minecraftforge.client.event.RenderLevelLastEvent` (or the NeoForge equivalent). Seven source files currently import and reference the removed class, causing a compilation failure in every build. Rendering systems for effects, particles, HUD, targets, teams, and text billboards are all broken until this is resolved.

## Changes
- [ ] Change 1: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` with `import net.minecraftforge.client.event.RenderLevelLastEvent` in `EffectRenderer.java`
- [ ] Change 2: Update handler parameter type `RenderWorldLastEvent` → `RenderLevelLastEvent` in `EffectRenderer.handleRenderWorldLastEvent()`
- [ ] Change 3: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` with `import net.minecraftforge.client.event.RenderLevelLastEvent` in `HudItemCharmedInfoRenderer.java`
- [ ] Change 4: Update handler parameter type `RenderWorldLastEvent` → `RenderLevelLastEvent` in `HudItemCharmedInfoRenderer.handleRenderWorldLastEvent()`
- [ ] Change 5: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` with `import net.minecraftforge.client.event.RenderLevelLastEvent` in `ParticleRenderer.java`
- [ ] Change 6: Update handler parameter type `RenderWorldLastEvent` → `RenderLevelLastEvent` in `ParticleRenderer.handleRenderWorldLastEvent()`
- [ ] Change 7: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` in `RenderingUtils.java` and update any referencing methods
- [ ] Change 8: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` with `import net.minecraftforge.client.event.RenderLevelLastEvent` in `TargetInfoRenderer.java`
- [ ] Change 9: Update handler parameter type `RenderWorldLastEvent` → `RenderLevelLastEvent` in `TargetInfoRenderer.handleRenderWorldLastEvent()`
- [ ] Change 10: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` with `import net.minecraftforge.client.event.RenderLevelLastEvent` in `TeamInfoRenderer.java`
- [ ] Change 11: Update handler parameter type `RenderWorldLastEvent` → `RenderLevelLastEvent` in `TeamInfoRenderer.handleRenderWorldLastEvent()`
- [ ] Change 12: Replace `import net.minecraftforge.client.event.RenderWorldLastEvent` in `RenderTextBillboard2.java` and update any referencing methods

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

**Test coverage**: Compilation fix — `RenderWorldLastEvent` and `RenderLevelLastEvent` expose the same rendering callback surface (`getPoseStack()`, `getPartialTick()`, etc.) so all handler logic remains valid after the type rename. Manual in-game verification that effect, particle, HUD, target, team, and billboard rendering still functions correctly.

## Related Issues
Closes #1443

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
`fix(rendering): Replace removed RenderWorldLastEvent with RenderLevelLastEvent across all rendering handlers`

## Additional Notes
Affected files (all under `src/main/java/bassebombecraft/client/`):
- `event/rendering/effect/EffectRenderer.java`
- `event/rendering/HudItemCharmedInfoRenderer.java`
- `event/rendering/particle/ParticleRenderer.java`
- `rendering/RenderingUtils.java`
- `event/rendering/TargetInfoRenderer.java`
- `event/rendering/TeamInfoRenderer.java`
- `operator/rendering/RenderTextBillboard2.java`

All changes are 1:1 class renames. Verify that the `RenderLevelLastEvent` API provides all methods used by each handler (e.g. `getPoseStack()`, `getPartialTick()`). If the target Forge version uses a different replacement name, adjust accordingly.
