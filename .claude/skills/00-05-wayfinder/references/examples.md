# Worked Example — Forge 1.18.2 Migration (`#1440`–`#1461`)

This is an **illustrative retrofit**, not a recorded session. The 1.18.2 migration on branch `dev-4.1` was actually run as a series of single-pass build-error → PR requirements file → implement cycles (see `.github/.requirements/pr/1440-*.md` … `1461-*.md`). The ticket and decision wording below reconstructs what that effort would have looked like as a formal map, using [map-template.md](../templates/map-template.md) and [ticket-template.md](../templates/ticket-template.md). Issue numbers and the facts they carry are real; the map structure is not historical.

## How the effort arrives at a map

Most of the migration did **not** need a map: `fmllegacy.RegistryObject` → `registries.RegistryObject` (`#1440`) or `IForgeContainerType` → `IForgeMenuType` (`#1441`) are 1:1 renames that fit one PR requirements file each. A map becomes worth it when the build errors stop being independent. Here the signals would have been:

- Rendering fixes (`#1443`, `#1447`–`#1449`, `#1452`, `#1453`) all touch the same renderer classes, so the order in which they land changes the conflicts of the next one.
- The "Registry is already frozen" startup crash (`#1455`) could only be diagnosed after the code compiled, and its fix touches `RegisteredItems.java`, which `#1440` had already edited.
- CI (`#1457`–`#1461`) depended on the version-string format chosen for `gradle.properties`, `version.json` and `build.gradle`.

## Retrofitted map — `wayfinding-1-forge-1-18-2-migration.md`

### Destination

The mod compiles, starts, and runs on Minecraft 1.18.2 / Forge 40.3.0, and the GitHub Actions build produces a correctly named jar — with no gameplay behaviour changed.

### Notes

- Target is Minecraft 1.18.2, Forge 40.3.0, Java 17 (already set on master by `#1437`).
- API migration only: replace removed or deprecated Forge/Minecraft APIs with their direct equivalents; no gameplay redesign.
- Each PR requirements file must stay within one subsystem (registries, networking, rendering, AI) so reviews and merges stay independent.
- Compile errors can only be ranked reliably from a fresh `./gradlew compileJava --info`; never plan from an old error list.

### Decisions So Far

| Ticket | Gist | Resolved |
|--------|------|----------|
| `registry-import-audit` | 31 files import `fmllegacy.RegistryObject`; single mechanical rename, one PR (`#1440`) | this session |
| `render-event-replacements` | Map each removed render event to its 1.18.2 successor: `RenderWorldLastEvent`/`RenderLevelLastEvent` → `RenderLevelStageEvent`, `RenderGameOverlayEvent` → `RenderGuiOverlayEvent`, `getMatrixStack()` → `getPoseStack()` | this session |

### Not Yet Specified

- Whether any gameplay-visible regressions appear once the mod starts (HUD placement, particle timing) — can't be phrased as a ticket until the `run-client-smoke-test` task has produced a result.

### Out of Scope

- Migrating to a newer Minecraft version than 1.18.2 — the migration targets 1.18.2 only; a later version needs its own map.
- Refactoring the `Registered*` classes beyond the import and registry-timing fixes — no behavioural change in this effort.

### Tickets

| Ticket | Type | Status | Blocked By | File |
|--------|------|--------|------------|------|
| `network-api-mapping` | research | open | none | `wayfinding-1-tickets/network-api-mapping.md` |
| `render-stage-selection` | grilling | open | `render-event-replacements` | `wayfinding-1-tickets/render-stage-selection.md` |
| `registry-freeze-root-cause` | research | open | `registry-import-audit` | `wayfinding-1-tickets/registry-freeze-root-cause.md` |
| `run-client-smoke-test` | task | open | `registry-freeze-root-cause` | `wayfinding-1-tickets/run-client-smoke-test.md` |
| `ci-version-format` | grilling | open | none | `wayfinding-1-tickets/ci-version-format.md` |

## One ticket, fully worked — `render-stage-selection.md`

```markdown
# Ticket — Which RenderLevelStageEvent stage for world-space renderers

**Type**: grilling
**Status**: open
**Blocked By**: render-event-replacements

## Question

RenderLevelLastEvent was deprecated-for-removal and replaced by RenderLevelStageEvent,
which fires once per stage. Which Stage should each world-space renderer
(Decoy, Increase/DecreaseSizeEffect, Respawned, Team*, particle) filter on so that
rendering order matches the old "last" behaviour?

## Resolution

_(unresolved — to be settled with the user: (1) a single stage such as AFTER_PARTICLES
for all renderers, (2) per-renderer stages based on whether the renderer draws
translucent geometry, (3) AFTER_WEATHER. Option 1 is the likely default; the
ticket exists because option 2 would change the PR split.)_
```

## Ticket Types → Resolution Mechanism (quick reference)

| Type | Resolved by | BasseBombeCraft equivalent |
|------|-------------|----------------------------|
| `research` | Gathering external/codebase knowledge, no user interaction needed | Spawn an `Explore` or `general-purpose` agent, or use `WebFetch`/`WebSearch`, in the background |
| `grilling` | Structured design interview | Invoke `00-02-grill-me` |
| `prototype` | A rough artifact to sharpen discussion | Scratch file or spike, throwaway |
| `task` | Manual prerequisite work | A concrete action such as running `./gradlew compileJava --info` |

`registry-freeze-root-cause` above is a good example of a `research` ticket: finding out which `DeferredRegister`/`RegistryObject` access happens before registration is answerable by reading `BassebombeCraft.java` and the `Registered*` classes — a background `Explore` agent pass, not a conversation. `run-client-smoke-test` is a `task` ticket: it needs a human to launch the client (`./gradlew runClient`) and report what they see.
