## Summary
Replace the deprecated `net.minecraftforge.client.event.RenderGameOverlayEvent` class and its removed `getMatrixStack()` method with the current Forge HUD-overlay API across all affected HUD rendering files. Eight source files are affected, spanning event handlers, operator implementations, port infrastructure, and the proxy registration layer.

## Motivation
`RenderGameOverlayEvent` and its `getMatrixStack()` method have been deprecated and marked for removal in the Forge 1.18.2-40.x line as part of the GUI/overlay rendering system overhaul. The intended replacement is `net.minecraftforge.client.event.RenderGuiOverlayEvent` (for layer-specific overlay callbacks) with `GuiGraphics` / `PoseStack` access through `getGuiGraphics().pose()` (Forge 1.19+) or `getPoseStack()` (Forge 1.18.2 deprecation bridge). Calls to `event.getMatrixStack()` on `RenderGameOverlayEvent` are already emitting deprecation warnings and will become compilation errors once the method is removed. Because `DefaultClientPorts` wraps and propagates the event object through the operator pipeline, the fix cascades across all consumers of `getFnRenderGameOverlayEvent1()` and `bcSetRenderGameOverlayEvent1`.

## Changes
- [ ] Change 1: `GenericCompositeItemsBookRenderer.java` — replace `RenderGameOverlayEvent.Pre` parameter type with `RenderGuiOverlayEvent.Pre` (or `RenderGuiOverlayEvent`); update import
- [ ] Change 2: `CompositeMagicItemRenderer.java` — replace `RenderGameOverlayEvent.Pre` parameter type with `RenderGuiOverlayEvent.Pre`; update import
- [ ] Change 3: `ClientProxy.java` — update `addListener` registrations to match the new event type signature (lines 304–305)
- [ ] Change 4: `DefaultClientPorts.java` — replace `RenderGameOverlayEvent` field, getter (`getRenderGameOverlayEvent1()`), setter (`setRenderGameOverlayEvent1()`), and the two static function/biconsumer lambdas with `RenderGuiOverlayEvent`; update import
- [ ] Change 5: `ClientPorts.java` (interface) — update `getRenderGameOverlayEvent1()` and `setRenderGameOverlayEvent1()` signatures to use `RenderGuiOverlayEvent`
- [ ] Change 6: `RenderOverlayText2.java` — replace `RenderGameOverlayEvent` field and local variable types; replace `event.getMatrixStack()` with `event.getPoseStack()` (or `event.getGuiGraphics().pose()`); update import
- [ ] Change 7: `RenderItemStack2.java` — replace `RenderGameOverlayEvent` field and local variable types; replace `event.getMatrixStack()` with `event.getPoseStack()`; update import
- [ ] Change 8: `CalculateRightBottomTextAnchor.java` — replace `RenderGameOverlayEvent` field / local variable types and import; update any `getWindow()` or size access to use the equivalent method on `RenderGuiOverlayEvent`

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

**Test coverage**: Verify in-game that all HUD overlays — composite magic item icons, book item icon display, charmed-item info text, target-info text, and team-info text — render at the correct screen position and with correct colours. Run `./gradlew compileJava` and confirm no deprecation warnings remain for `RenderGameOverlayEvent` or `getMatrixStack()`. Pay special attention to `CalculateRightBottomTextAnchor` to confirm window-dimension access produces the same pixel coordinates as before.

## Related Issues
Closes #1451

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
`fix(rendering): replace deprecated RenderGameOverlayEvent and getMatrixStack() with RenderGuiOverlayEvent across HUD pipeline`

## Additional Notes
Affected files (8):
- `src/main/java/bassebombecraft/client/event/rendering/GenericCompositeItemsBookRenderer.java`
- `src/main/java/bassebombecraft/client/event/rendering/CompositeMagicItemRenderer.java`
- `src/main/java/bassebombecraft/client/proxy/ClientProxy.java`
- `src/main/java/bassebombecraft/client/operator/DefaultClientPorts.java`
- `src/main/java/bassebombecraft/client/operator/ClientPorts.java` (interface — verify exact path)
- `src/main/java/bassebombecraft/client/operator/rendering/RenderOverlayText2.java`
- `src/main/java/bassebombecraft/client/operator/rendering/RenderItemStack2.java`
- `src/main/java/bassebombecraft/client/operator/rendering/CalculateRightBottomTextAnchor.java`

The `DefaultClientPorts` class mediates all event access in the operator pipeline; if the event type stored there is changed, every operator that reads `getRenderGameOverlayEvent1()` automatically benefits — so no additional operator files need changing. Verify that `RenderGuiOverlayEvent` (or `RenderGuiOverlayEvent.Pre`) is available in the Forge version on classpath before committing; if still on 1.18.2-40.x, consult the Forge changelog for the exact class name and inheritance hierarchy.
