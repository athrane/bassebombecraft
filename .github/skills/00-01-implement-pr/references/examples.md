# Worked Examples — `00-01-implement-pr` for BasseBombeCraft

Two fully worked scenarios for the `00-01-implement-pr` skill, plus a canonical-pattern lookup table for common implementation topics in the BasseBombeCraft codebase.

---

## Example 1: New Feature Implementation

**Input**: [`.github/.requirements/pr/1430-typed-potion-effect-registry.md`](../../../../.requirements/pr/1430-typed-potion-effect-registry.md) — a `feat(potion)` PR introducing a typed `RegisteredEffects` registry.

**Excerpt from the requirements file:**
```markdown
## Changes

### Files Deleted
_None._

### Files Updated
- **New: `src/main/java/bassebombecraft/potion/RegisteredEffects.java`** — Static registry...
- **New: `src/main/java/bassebombecraft/potion/EffectType.java`** — Enum of available effects...
- **New: `src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java`** — Operator...
- **Updated: `src/main/java/bassebombecraft/BassebombeCraft.java`** — Call `RegisteredEffects.bootstrap()`...
```

**Execution:**

1. **Phase 1 — Requirements Analysis**
   - Scope: `potion` (look at `src/main/java/bassebombecraft/potion/`)
   - Affected domains: `potion`, `event` (operator is wired into the operator graph)
   - Pattern siblings: `RegisteredItems` (look at how it self-bootstraps from `BassebombeCraft.setup()`)
2. **Phase 2 — Implementation Planning**
   - CREATE order:
     1. `src/main/java/bassebombecraft/potion/EffectType.java` (enum)
     2. `src/main/java/bassebombecraft/potion/AmplifierCurve.java` (functional interface + 3 default impls)
     3. `src/main/java/bassebombecraft/potion/RegisteredEffects.java` (registry + `EffectSpec` record)
     4. `src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java` (operator)
   - MODIFY: `src/main/java/bassebombecraft/BassebombeCraft.java` (add `RegisteredEffects.bootstrap()` near `RegisteredItems.bootstrap()`)
3. **Phase 3 — Implementation Execution**
   1. Create `EffectType.java` — Java 17 enum with three values
   2. Create `AmplifierCurve.java` — functional interface with `LINEAR`, `QUADRATIC`, `FIXED(int)` static factories
   3. Create `RegisteredEffects.java` — record `EffectSpec(MobEffect, int, AmplifierCurve)`; `Map<EffectType, EffectSpec>`; `bootstrap()` populates the map; `get(EffectType)`, `all()`, `apply(Entity, EffectType)` accessors with `TypeUtils.validate(...)`
   4. Create `ApplyEffectOperator.java` — implements `Operator`; wraps `RegisteredEffects.apply(...)`
   5. Modify `BassebombeCraft.java` — call `RegisteredEffects.bootstrap()` from `setup()`
4. **Phase 4 — Validation & Refinement**
   - `./gradlew compileJava` — passes
   - `./gradlew build` — passes (note: pre-existing failures in `PlayerUtils.java`, `GenericCompositeProjectileEntity.java` are out of scope)
   - `./gradlew test` — skipped; this domain has no `test/` source set
   - Manual in-game verification: launch dev client, apply each effect via debug command, confirm duration + amplifier

**Output (summary emitted to chat):**

```markdown
## Implementation Complete

### Files Created
- `src/main/java/bassebombecraft/potion/EffectType.java` — Enum of `MOB_PRIMING`, `INVISIBILITY_AURA`, `FLIGHT_BURST`
- `src/main/java/bassebombecraft/potion/AmplifierCurve.java` — Functional interface + `LINEAR`/`QUADRATIC`/`FIXED` impls
- `src/main/java/bassebombecraft/potion/RegisteredEffects.java` — Registry + `EffectSpec` record + bootstrap
- `src/main/java/bassebombecraft/potion/operator/ApplyEffectOperator.java` — Operator wrapping `RegisteredEffects.apply(...)`

### Files Modified
- `src/main/java/bassebombecraft/BassebombeCraft.java` — Added `RegisteredEffects.bootstrap()` call near `RegisteredItems.bootstrap()`

### Registration / Wiring
- Registered three effects via `RegisteredEffects.bootstrap()` from the mod entrypoint

### Javadoc Added
- `EffectType` (class-level + per-value)
- `AmplifierCurve` (interface + each impl)
- `RegisteredEffects` (class-level + every public method)
- `ApplyEffectOperator` (class-level + `execute()`)

### Validation Results
✅ `./gradlew compileJava` succeeded
✅ `./gradlew build` succeeded
✅ `./gradlew test` skipped — no `test/` source set for the `potion/` domain

### Conformance Verification
✅ Java 17 + Forge 1.17.1 patterns only
✅ Static factory methods on `AmplifierCurve` (each impl has a `create()` or static factory)
✅ `TypeUtils`-style validation in `RegisteredEffects.apply(...)` for `Entity` and `EffectType` args
✅ Javadoc on every public class and method
✅ `RegisteredEffects.bootstrap()` called from `BassebombeCraft.setup()` alongside `RegisteredItems.bootstrap()`

### Ready for Commit
Commit message: `feat(potion): add typed potion-effect registry`

### Notes
- Pre-existing build errors in `PlayerUtils.java` and `GenericCompositeProjectileEntity.java` were observed but not touched — they are out of scope for this PR.
- Manual in-game verification confirmed all three effects apply correctly.
```

---

## Example 2: Bug Fix Implementation

**Input**: [`.github/.requirements/pr/1425-replace-deprecated-renderSystem-color4f-calls.md`](../../../../.requirements/pr/1425-replace-deprecated-renderSystem-color4f-calls.md) — a `fix(rendering)` PR replacing deprecated `RenderSystem.color4f()` and `TextureManager.bind()` calls.

**Excerpt from the requirements file:**
```markdown
## Changes

### Files Deleted
_None._

### Files Updated
- **`src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java`** — Replace 5× `RenderSystem.color4f(...)` with `RenderSystem.setShaderColor(...)`; replace 4× `getMinecraft().getTextureManager().bind(...)` with `RenderSystem.setShaderTexture(0, ...)`.
```

**Execution:**

1. **Phase 1 — Requirements Analysis**
   - Scope: `client/screen` / `rendering`
   - Affected file: `src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java`
   - Pattern sibling: existing migration in `WireframeBoundingBoxRenderer.java` (PR #1418) — same migration, applied to a different file
3. **Phase 2 — Implementation Planning**
   - MODIFY: `src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java`
   - CREATE: none
   - DELETE: none
4. **Phase 3 — Implementation Execution**
   1. Open `CompositeMagicItemScreen.java` at the cited lines (185–217)
   2. Replace each `RenderSystem.color4f(r, g, b, a)` with `RenderSystem.setShaderColor(r, g, b, a)` — arguments identical
   3. Replace each `getMinecraft().getTextureManager().bind(texture)` with `RenderSystem.setShaderTexture(0, texture)`
   4. Verify `git diff` shows only the targeted file changed
5. **Phase 4 — Validation & Refinement**
   - `./gradlew compileJava` — passes (the specific build error is fixed; pre-existing failures in other files remain)
   - `./gradlew build` — passes
   - `./gradlew test` — skipped; this file has no associated test
   - Manual in-game verification: launch dev client, open `CompositeMagicItemScreen`, confirm visual output matches pre-migration

**Output (summary emitted to chat):**

```markdown
## Implementation Complete

### Files Created
_None._

### Files Modified
- `src/main/java/bassebombecraft/client/screen/CompositeMagicItemScreen.java` — Replaced 5× `RenderSystem.color4f(...)` and 4× `getMinecraft().getTextureManager().bind(...)` with the modern equivalents.

### Registration / Wiring
_N/A — no registration changes._

### Javadoc Added
_None — Javadoc unchanged._

### Validation Results
✅ `./gradlew compileJava` succeeded
✅ `./gradlew build` succeeded
✅ `./gradlew test` skipped — no `test/` source set for this file

### Conformance Verification
✅ Java 17 + Forge 1.17.1 patterns only (using `RenderSystem.setShaderColor` / `setShaderTexture`)
✅ Javadoc preserved (no changes)
✅ File scope matches the PR spec's Changes section (only `CompositeMagicItemScreen.java` touched)

### Ready for Commit
Commit message: `fix(rendering): replace deprecated RenderSystem.color4f() calls`

### Notes
- Pre-existing build errors in `PlayerUtils.java` and `GenericCompositeProjectileEntity.java` were observed but not touched — they are out of scope for this PR.
- Manual in-game verification confirmed the magic-item composite screen renders identically to the pre-migration look.
```

---

## Canonical Pattern Lookup

Pattern reminders are defined authoritatively in the codebase — look at these files rather than relying on summaries:

| Topic | Canonical Pattern Source |
|-------|--------------------------|
| **Item registration** | `src/main/java/bassebombecraft/item/RegisteredItems.java` — `register(...)` static method + `bootstrap()` |
| **Entity registration** | `src/main/java/bassebombecraft/entity/RegisteredEntities.java` — same shape as `RegisteredItems` |
| **Operator pattern** | `src/main/java/bassebombecraft/operator/Operator.java` (interface) + siblings in `src/main/java/bassebombecraft/operator/` |
| **Event-driven side effects** | Look at any handler in `src/main/java/bassebombecraft/event/[domain]/` for the registration pattern |
| **TypeUtils-style validation** | Search for `TypeUtils.validate(...)` calls in any existing class for the exact import + call shape |
| **Static factory methods** | Search for `public static Foo create(` or `public static Foo of(` to find the project's preferred factory name |
| **Configuration** | `src/main/java/bassebombecraft/config/ModConfiguration.java` + `bassebombecraft-common.toml` |
| **Network packets** | `src/main/java/bassebombecraft/network/NetworkChannelHelper.java` + `packet/` siblings |

When in doubt, **read 2–3 sibling files** in the same package to confirm the shape before adding a new one.