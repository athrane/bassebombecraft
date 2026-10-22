# Ticket — Correct overlay event API for Forge 40.3.0

**Type**: research
**Status**: open
**Blocked By**: none

## Question

Commit `3f63534a` (`#1453`) says `RenderGameOverlayEvent` was replaced with `RenderGuiOverlayEvent`, yet `CompositeMagicItemRenderer`, `GenericCompositeItemsBookRenderer`, `ClientPorts` and `DefaultClientPorts` still import and use `net.minecraftforge.client.event.RenderGameOverlayEvent` (and a port named `getMatrixStack1`). Is `RenderGameOverlayEvent` valid but deprecated in Forge 40.3.0, or does 40.3.0 require a replacement (and which one)? Answer from the 40.3.0 sources or changelog, not from memory, and say which of the remaining uses must change.

## Resolution

_(filled in when Status becomes resolved)_
