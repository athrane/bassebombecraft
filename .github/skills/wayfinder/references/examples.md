# Wayfinder Examples — Worked

The original Matt Pocock write-up uses a "diplomatic-relationship-state" example (`#1300`–`#1305`). Below is a fully worked BasseBombeCraft-flavoured example so you can see the artifacts in context.

## Scenario

The mod is mid-migration from legacy OpenGL rendering to PoseStack-based rendering (cf. the chain of `.github/.requirements/pr/14XX-*-rendering*.md` files). A natural next slice is a **renderer pipeline refactor**: replace direct `glBegin/glEnd` patterns with a single `IRenderer` interface, then make each existing renderer (`SolidBoundingBoxRenderer`, `HitByRayTraceBoundingBoxRenderer`, `WireframeBoundingBoxRenderer`, `DefaultTargetEntityRenderer`) implement it.

The work is too big for one PR (four call sites + a new interface + tests) and has too many open questions (error handling on draw-failure? render-thread affinity? what about deprecated classes like `GenericEggProjectile`?) for a single planning pass.

## The Map

File: `.github/.requirements/wayfinding/wayfinding-1-renderer-pipeline-refactor.md`

```markdown
# Wayfinding Map 1: Renderer pipeline refactor

## Destination

Replace legacy direct-OpenGL rendering in BasseBombeCraft with a single `IRenderer` interface, then migrate the four existing renderer implementations to it. Result: one render pattern across the codebase, no direct OpenGL state calls in feature code, all four existing renderer classes compile cleanly under modern Forge.

## Notes

- Java 17 + Forge 1.17.1 only. No legacy OpenGL patterns. `PoseStack` is the only accepted render API.
- Match existing convention: static factory methods, `TypeUtils` validation, `RegisteredItems`/`RegisteredEntities` registration patterns.
- Ticket resolutions must cite the source: file paths, Forge docs URLs, or grilling answers.
- This map supersedes the one-off rendering PRs under `.github/.requirements/pr/1416`, `1417`, `1418`, `1419`. They resolve their own scope; this map is for the unified pipeline.

## Decisions So Far

| Ticket | Gist | Resolved |
|--------|------|----------|
| _none yet_ | | |

## Not Yet Specified

- How does this interact with the deprecated `GenericEggProjectile` class? (Probably out of scope — but unclear until other tickets resolve.)
- Should the new `IRenderer` live in `client.rendering` or be split between `client.rendering.api` and `client.rendering.impl`? (Currently too vague — promote once we know what methods the interface needs.)

## Out of Scope

- _none yet_

## Tickets

| Ticket | Type | Status | Blocked By | File |
|--------|------|--------|------------|------|
| renderer-interface-method-shape | research | open | — | [tickets/renderer-interface-method-shape.md](./wayfinding-1-tickets/renderer-interface-method-shape.md) |
| renderer-error-handling | grilling | open | renderer-interface-method-shape | [tickets/renderer-error-handling.md](./wayfinding-1-tickets/renderer-error-handling.md) |
| render-thread-affinity | research | open | — | [tickets/render-thread-affinity.md](./wayfinding-1-tickets/render-thread-affinity.md) |
| renderer-migration-order | grilling | open | renderer-interface-method-shape, render-thread-affinity | [tickets/renderer-migration-order.md](./wayfinding-1-tickets/renderer-migration-order.md) |
```

## A Sample Ticket

File: `.github/.requirements/wayfinding/wayfinding-1-tickets/renderer-interface-method-shape.md`

```markdown
# Ticket: Renderer interface method shape

| Field | Value |
|-------|-------|
| Wayfinding Map | [1-renderer-pipeline-refactor](../wayfinding-1-renderer-pipeline-refactor.md) |
| Type | research |
| Status | resolved |
| Blocked By | — |
| GitHub Issue | — |

## Question

What methods must `IRenderer` expose so that `SolidBoundingBoxRenderer`, `HitByRayTraceBoundingBoxRenderer`, `WireframeBoundingBoxRenderer`, and `DefaultTargetEntityRenderer` can each implement it without per-class divergence?

## Resolution

Explored the four renderer files (Explore subagent). Common surface across all four:

- `render(PoseStack, MultiBufferSource, int light, int overlay)`
- `getBoundingBox()` (used by event-driven callers to pick the right renderer)
- `shouldRender(Entity)` (entity filter; not used by WireframeBoundingBoxRenderer but required by the other three)

Two convenience hooks (`prepareForRender` / `completeRender`) are used by the solid/wireframe classes but not the others — propose them as default methods on the interface. Recorded the findings in `bin/spike/IRenderer-shape.md` for reference (throwaway, not production).
```

## Graduating the Slice

Once `renderer-interface-method-shape`, `renderer-error-handling`, `render-thread-affinity`, and `renderer-migration-order` are all resolved, the slice becomes:

> Introduce `IRenderer` interface in `client/rendering/` with three methods (`render`, `getBoundingBox`, `shouldRender`) and two default hooks (`prepareForRender`, `completeRender`). Migrate `SolidBoundingBoxRenderer`, `HitByRayTraceBoundingBoxRenderer`, `WireframeBoundingBoxRenderer`, `DefaultTargetEntityRenderer` to implement it.

Following [pr-graduation.md](./pr-graduation.md), the implementer creates:

```
.github/.requirements/pr/1426-introduce-irenderer-interface.md
```

— produced by invoking [`00-00-create-pr`](../../00-00-create-pr/SKILL.md), with `Related Issues` linking back to the four resolved tickets. The map's Decisions So Far gains one row, the Tickets table empties (or shrinks to the leftover unrelated questions), and the work moves from planning into execution.

## Anti-Patterns to Notice

- **The map did not write any production code.** It produced *decisions* recorded in ticket files.
- **The map did not invent its own PR format.** It handed off to the existing PR template.
- **Not Yet Specified was honest.** Two items live there because they couldn't yet be ticket-questions — instead of forcing tickets out of fog.
- **Tickets cite sources.** `renderer-interface-method-shape` cites the Explore subagent's output and the throwaway spike path. Plausible answers without provenance would be an anti-pattern.