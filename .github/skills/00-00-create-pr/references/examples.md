# Worked Examples — `00-00-create-pr` for BasseBombeCraft

Two fully worked examples of the `00-00-create-pr` skill's output, showing the expected level of detail for each section of [templates/pr-template.md](../templates/pr-template.md). Both examples use Java 17 + Forge 1.17.1 conventions, real codebase patterns, and the project's `[github-issue-number]-[slug].md` filename format.

---

## Example 1: Bug Fix PR — deprecated API replacement

**User provides:**
- Problem description: "`CompositeMagicItemScreen.java` fails to build because `RenderSystem.color4f()` and `TextureManager.bind()` are deprecated in Forge 1.17.1. Replace them with `RenderSystem.setShaderColor()` and `RenderSystem.setShaderTexture(int, ResourceLocation)`."

**Agent infers (no further questions):**
- Affected domain: `client/screen` (`src/main/java/bassebombecraft/client/screen/`)
- Type: `fix` (build failure due to deprecated API)
- Scope: `rendering` (the deprecated APIs are rendering-related)
- Affected file: [CompositeMagicItemScreen.java](src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java) (5 + 4 call sites in `renderSequenceHighlight`, `renderDecoration`, `renderBg`)
- Migration pattern: matches what was already done in [WireframeBoundingBoxRenderer.java](src/main/java/bassebombecraft/client/rendering/WireframeBoundingBoxRenderer.java) (see existing PR `.github/.requirements/pr/1418-resolve-renderingutils-import-error.md`)
- Tracking GitHub issue: assume `#1425` (slotted in after existing rendering PR chain)

**Generated PR Title:**
```
fix(rendering): Replace deprecated RenderSystem.color4f() calls
```

**Generated File:** `.github/.requirements/pr/1425-replace-deprecated-renderSystem-color4f-calls.md`

**File Contents:**

````markdown
# fix(rendering): Replace deprecated RenderSystem.color4f() calls

## Summary
Fixes compilation errors in [CompositeMagicItemScreen.java](src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java) where the deprecated method `RenderSystem.color4f()` is called. This method has been removed in newer Minecraft versions and needs to be replaced with the modern rendering API equivalent.

## Motivation
The `CompositeMagicItemScreen` class uses the deprecated `RenderSystem.color4f()` method which no longer exists in the current Minecraft rendering API. This causes build failures with multiple "cannot find symbol" errors. The method was part of the legacy OpenGL state management that has been replaced with shader-based color manipulation in modern Minecraft versions.

## Changes

### Files Deleted
_None._

### Files Updated
- **[CompositeMagicItemScreen.java](src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java)** — Replace 5× `RenderSystem.color4f(...)` calls with `RenderSystem.setShaderColor(...)`; replace 4× `getMinecraft().getTextureManager().bind(...)` calls with `RenderSystem.setShaderTexture(0, ...)`. Call sites: `renderSequenceHighlight()` (lines 185–186), `renderDecoration()` (lines 201–202, 205–206), `renderBg()` (lines 210, 217).

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking feature which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Color replacement
1. Open [CompositeMagicItemScreen.java](src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java#L185)
2. Replace each `RenderSystem.color4f(r, g, b, a)` with `RenderSystem.setShaderColor(r, g, b, a)` — arguments identical, only the call signature differs
3. Preserve all colour values exactly to maintain visual consistency

### Phase 2 — Texture binding replacement
1. Replace each `getMinecraft().getTextureManager().bind(texture)` with `RenderSystem.setShaderTexture(0, texture)` — slot `0` is the standard GUI binding slot
2. Drop the now-unused `getMinecraft().getTextureManager()` chain if it has no other callers in the file (verify with grep first)

### Phase 3 — Build verification
1. Run `./gradlew compileJava` — must complete without "cannot find symbol" errors
2. Run `./gradlew build` — must succeed
3. Confirm only the targeted file changed (`git diff --stat`)

## Testing

### Java build & tests

- [ ] `./gradlew compileJava` succeeds
- [ ] `./gradlew build` succeeds
- [ ] No new compile errors in other files (pre-existing errors in unrelated files are out of scope for this PR)

**Test coverage**: This is a build-error fix; there are no unit tests to add. The fix is validated by `./gradlew build` succeeding and a manual visual check that GUI rendering is unchanged.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | Magic-item composite screen renders identically | Launch dev client, open a composite magic item screen; confirm colors match the pre-migration look (white highlights, oscillating magenta decoration) |
| 2 | No `LEGACY_PIPELINE` warning in F3 debug | Check F3 debug log on screen open — must be absent |
| 3 | No OpenGL errors on screen open | Check log for `GL_INVALID_OPERATION` — must be absent |

## Documentation Plan
_None._ No documentation changes required for this build-error fix.

## Related Issues
Closes #1425

Related to `.github/.requirements/pr/1418-resolve-renderingutils-import-error.md` — same migration (OpenGL → shader-based rendering) is being applied across the rendering classes.

## Checklist
- [x] Code follows project conventions (shader-based rendering, `RenderSystem.setShaderColor`/`setShaderTexture` pattern)
- [x] Code compiles without errors (`./gradlew compileJava` passes)
- [x] Code follows Java style guidelines (one top-level class per file, Javadoc preserved)
- [ ] All tests pass (`./gradlew test` passes for the affected module — pre-existing failures in unrelated files are out of scope)
- [x] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added/modified — N/A for this fix
- [ ] Updated documentation — N/A
- [x] No breaking changes
- [x] Commit messages follow Conventional Commits format

## Additional Notes

### Implementation Summary

| Method | Line(s) | Before | After |
|--------|---------|--------|-------|
| `renderSequenceHighlight()` | 185 | `RenderSystem.color4f(oscRgb, oscRgb, oscRgb, 1.0F)` | `RenderSystem.setShaderColor(oscRgb, oscRgb, oscRgb, 1.0F)` |
| `renderSequenceHighlight()` | 186 | `getMinecraft().getTextureManager().bind(SEQUENCE_TEXTURE)` | `RenderSystem.setShaderTexture(0, SEQUENCE_TEXTURE)` |
| `renderDecoration()` | 201, 205 | `RenderSystem.color4f(...)` ×2 | `RenderSystem.setShaderColor(...)` ×2 |
| `renderDecoration()` | 202, 206 | `getMinecraft().getTextureManager().bind(DECORATION_TEXTURE/DECORATION2_TEXTURE)` | `RenderSystem.setShaderTexture(0, ...)` ×2 |
| `renderBg()` | 210 | `RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F)` | `RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)` |
| `renderBg()` | 217 | `getMinecraft().getTextureManager().bind(BACKGROUND_TEXTURE)` | `RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE)` |

### Migration Path

In modern Minecraft versions (1.17+), the rendering API has changed:
- `RenderSystem.color4f()` → `RenderSystem.setShaderColor()` for shader-based color control
- `TextureManager.bind()` → `RenderSystem.setShaderTexture(int, ResourceLocation)` for shader texture binding

**Pattern source**: This PR mirrors the migration already applied to renderer classes (see PR #1418 for the wireframe renderer, which used the same migration).
````

---

## Example 2: New Feature PR — typed potion-effect registry

**User provides:**
- Problem description: "Potion effects are currently registered ad-hoc via string keys scattered through `RegisteredItems` and event handlers. We need a typed registry that maps an `EffectType` enum to its `MobEffect` instance, its duration in ticks, and the amplifier curve, so we can list and apply effects uniformly."

**Agent infers (no further questions):**
- Affected domain: `potion` (existing files in [src/main/java/bassebombecraft/potion/](src/main/java/bassebombecraft/potion/))
- Type: `feat` (new capability)
- Scope: `potion`
- Relevant existing files: `potion/RegisteredEffects.java` (or equivalent — to be confirmed via grep), `potion/effect/MobPrimingEffect.java`, `RegisteredItems.java`, `BassebombeCraft.java` (mod entrypoint)
- Architecture patterns: `RegisteredItems`-style static registry; `TypeUtils`-style validation; event-driven application
- Tracking GitHub issue: assume `#1430`

**Generated PR Title:**
```
feat(potion): add typed potion-effect registry
```

**Generated File:** `.github/.requirements/pr/1430-typed-potion-effect-registry.md`

**File Contents:**

````markdown
# feat(potion): Add typed potion-effect registry

## Summary
Adds a typed registry that maps an `EffectType` enum to its `MobEffect`, duration in ticks, and amplifier curve. Effects are registered in `RegisteredEffects` (mirroring the existing `RegisteredItems` pattern) and applied through a single `EffectRegistry.apply(Entity, EffectType)` operator instead of ad-hoc string-key dispatch.

## Motivation
Potion effects are currently registered ad-hoc via string keys scattered across `RegisteredItems`, event handlers, and composite item logic. This makes it impossible to:
- Enumerate available effects for a GUI / wiki / debug command.
- Apply the same effect from different call paths consistently (different durations, different amplifiers).
- Refactor safely — there is no single source of truth for which `MobEffect` instance backs each `EffectType`.

The new registry replaces the string-key dispatch with an enum + `MobEffect` mapping, keeping the registration pattern consistent with the rest of the codebase.

## Changes

### Files Deleted
_None._

### Files Updated
- **New: [RegisteredEffects.java](src/main/java/bassebombecraft/potion/RegisteredEffects.java)** — Static registry of `(EffectType, MobEffect, Duration, AmplifierCurve)` tuples. Provides `get(EffectType)`, `all()`, `apply(Entity, EffectType)` methods.
- **New: [EffectType.java](src/main/java/bassebombecraft/potion/EffectType.java)** — Enum of available effects: `MOB_PRIMING`, `INVISIBILITY_AURA`, `FLIGHT_BURST`.
- **New: [AmplifierCurve.java](src/main/java/bassebombecraft/potion/AmplifierCurve.java)** — Functional interface: `(int stackDepth) → int amplifier`, with default linear `LINEAR`, quadratic `QUADRATIC`, and `FIXED(int)` implementations.
- **New: [ApplyEffectOperator.java](src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java)** — Operator implementing the project's [Operator pattern](src/main/java/bassebombecraft/operator/Operator.java). Wraps `RegisteredEffects.apply(...)`.
- **Updated: [BassebombeCraft.java](src/main/java/bassebombecraft/BassebombeCraft.java)** — Call `RegisteredEffects.bootstrap()` from the mod entrypoint alongside the existing `RegisteredItems.bootstrap()` call.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking feature which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Data model
1. Create [EffectType.java](src/main/java/bassebombecraft/potion/EffectType.java) — enum with the three effect variants listed above
2. Create [AmplifierCurve.java](src/main/java/bassebombecraft/potion/AmplifierCurve.java) — single-method functional interface + three static `LINEAR`, `QUADRATIC`, `FIXED` implementations
3. Define a record `EffectSpec(MobEffect effect, int durationTicks, AmplifierCurve amplifierCurve)` inside [RegisteredEffects.java](src/main/java/bassebombecraft/potion/RegisteredEffects.java) — use Java 17 records

### Phase 2 — Registry
1. Create [RegisteredEffects.java](src/main/java/bassebombecraft/potion/RegisteredEffects.java):
   - Private static `Map<EffectType, EffectSpec>` populated by `bootstrap()`
   - `bootstrap()` populates the map with the three effects + their default `MobEffect` instances + `Duration` (in ticks) + `AmplifierCurve`
   - Public `get(EffectType)`, `all()`, `apply(Entity, EffectType)` accessors
   - Use `TypeUtils.validate(...)` to assert non-null inputs

### Phase 3 — Operator
1. Create [ApplyEffectOperator.java](src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java):
   - Implements [Operator.java](src/main/java/bassebombecraft/operator/Operator.java)
   - Constructor takes `Entity` + `EffectType`
   - `execute()` delegates to `RegisteredEffects.apply(...)`
   - Uses the project's existing operator wrappers (`Ports`, `LazyInitOp2` — see how other operators in the same package are structured)

### Phase 4 — Wiring
1. Update [BassebombeCraft.java](src/main/java/bassebombecraft/BassebombeCraft.java) — call `RegisteredEffects.bootstrap()` from the existing `setup()` method, near `RegisteredItems.bootstrap()`

## Testing

### Java build & tests

- [ ] `./gradlew compileJava` succeeds (no new errors in any file)
- [ ] `./gradlew build` succeeds
- [ ] Existing PR-chain errors in unrelated files (e.g. `PlayerUtils`, `GenericCompositeProjectileEntity`) remain unchanged — this PR does not touch them

**Test coverage**: No unit tests in this mod. Validation is by `./gradlew build` + manual in-game verification that the three effects (`MOB_PRIMING`, `INVISIBILITY_AURA`, `FLIGHT_BURST`) all apply correctly with the registered durations and amplifier curves.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | Mod loads, `RegisteredEffects.bootstrap()` runs | Launch dev client, check `latest.log` for absence of bootstrap exceptions |
| 2 | `MOB_PRIMING` applies as before | Apply via existing composite item; verify duration + amplifier match pre-PR behaviour |
| 3 | `INVISIBILITY_AURA` applies via `ApplyEffectOperator` | Spawn a test entity, call the operator via a `/bassebombecraft test-effect INVISIBILITY_AURA` debug command (or temporarily wire from an existing item); confirm effect is applied |
| 4 | `FLIGHT_BURST` applies with quadratic amplifier curve | Same as #3; observe amplifier scaling on repeated application |
| 5 | `RegisteredEffects.all()` returns three entries | Call from a debug command; assert `all().size() == 3` |

## Documentation Plan

| File | Changes |
|------|---------|
| [RegisteredEffects.java](src/main/java/bassebombecraft/potion/RegisteredEffects.java) | Javadoc on every public method (`bootstrap`, `get`, `all`, `apply`) — class-level Javadoc explains the registry's role and the parallel to `RegisteredItems` |
| [EffectType.java](src/main/java/bassebombecraft/potion/EffectType.java) | One-line Javadoc on each enum value describing the effect's gameplay purpose |
| [AmplifierCurve.java](src/main/java/bassebombecraft/potion/AmplifierCurve.java) | Javadoc on the interface + each of the three default implementations |
| [ApplyEffectOperator.java](src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java) | Javadoc on the constructor and `execute()` |
| `README.md` | Note in the "Configuration / Gameplay" section that effects are now centrally registered; link to the wiki page |
| Wiki: "Potion effects" page (if exists) | New sub-section documenting `EffectType` enum values + default durations + amplifier curves |

## Related Issues
Closes #1430

Related to PR #1418, #1425 — both renderer migration work that this PR's entrypoint call site sits beside.

## Checklist
- [x] Code follows project conventions (operator pattern, `RegisteredItems`-style registry, `TypeUtils` validation)
- [x] Code compiles without errors (`./gradlew compileJava` passes)
- [x] Code follows Java style guidelines (one top-level class per file, Javadoc on public APIs)
- [ ] All tests pass (`./gradlew test` passes — pre-existing unrelated failures are out of scope)
- [x] Build succeeds (`./gradlew build` passes)
- [x] Javadoc comments added for public APIs
- [x] Updated documentation (`README.md`, wiki)
- [x] No breaking changes (existing effects continue to work via the operator wrapper)
- [x] Commit messages follow Conventional Commits format

## Additional Notes

### Why a record?

`EffectSpec` is a pure data tuple — no identity, no inheritance. Java 17 records give us `equals`, `hashCode`, `toString`, and accessor methods for free, matching the project's preference for compact data carriers.

### Why an `Operator` wrapper?

The existing `potion/` package already uses the operator pattern (see other operators in the same package). Wrapping `RegisteredEffects.apply(...)` in an operator keeps the call site consistent with the rest of the codebase and lets the effect application participate in any future operator composition / porting infrastructure.

### Migration Path

This PR adds new code only — it does not delete existing ad-hoc effect application paths. A follow-up PR can migrate the existing string-key dispatch sites to use the new operator; that migration is intentionally out of scope here to keep this PR reviewable.

### Conformance Notes

- Static factory methods used: `AmplifierCurve.LINEAR`, `AmplifierCurve.QUADRATIC`, `AmplifierCurve.FIXED(int)` — single-arg factories with `@Contract(pure = true)` semantics.
- Runtime validation: `TypeUtils.validateNotNull(entity)`, `TypeUtils.validateNotNull(effectType)` in `RegisteredEffects.apply`.
- ES module / Forge import order: imports grouped Java standard library → Minecraft → Forge → local `bassebombecraft.*`, no unused imports.
- No `Math.random()`-style usage — `RandomComponent` analogue does not exist in this mod, but if any randomness is needed for amplifier curves it should source from a `RandomSource` (Minecraft 1.17.1+ API) rather than `Math.random()`.
````

---

## Patterns Common to Both Examples

Both examples demonstrate:

1. **Conventional Commits title** matching the project's existing style (`fix(rendering):`, `feat(potion):`).
2. **Bracketed markdown file links** for every cited source file, including `L<n>` line ranges where useful.
3. **Java 17 + Forge 1.17.1 conventions** — no Java 8 patterns, no pre-1.17 OpenGL, no deprecated Forge APIs.
4. **Phase-ordered Implementation Plan** — data model → registry → wiring → tests (dependency-ordered, each phase small enough to be a single reviewable commit).
5. **Manual-validation table** rather than a unit-test suite (matches the mod's lack of a `test/` source set).
6. **Documentation Plan** populated with both code-level Javadoc and user-facing `README.md` / wiki entries.
7. **Related Issues** linking the tracking GitHub issue AND cross-referencing sibling PRs in the same chain.
8. **Additional Notes** as the catch-all for root-cause analyses, migration tables, conformance notes — anything a reviewer needs that doesn't fit elsewhere.
9. **Filename** `[github-issue-number]-[slug].md`, **never** random 3-digit.

## Anti-Patterns to Avoid

- **Random 3-digit filenames.** The repo convention is GitHub issue numbers. Match it.
- **Bare path citations** like `src/main/java/.../Foo.java` — use bracketed markdown links so reviewers can navigate.
- **Java 8 patterns** like `new Thread(() -> {...})` raw or anonymous inner classes where lambdas are clearer.
- **Pre-1.17 OpenGL** like `GlStateManager.pushMatrix()` — use `PoseStack`.
- **`npm run` validation commands.** This is a Java/Gradle project.
- **`Math.random()`** — this isn't an ECS framework, but if any randomness is needed, source from `RandomSource` (Minecraft 1.17.1+) rather than `Math.random()`.
- **Inline clarifying questions.** If the problem is too vague, use [`wayfinder`](../../wayfinder/SKILL.md) — don't interview the user inline.
- **Inventing a new PR spec format.** Use [templates/pr-template.md](../templates/pr-template.md) exactly.