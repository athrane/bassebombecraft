# Worked Examples — 00-01-implement-pr

## Example 1 — API-migration fix (`1441-iforgecontainertype-import-fails.md`)

**Requirements**: replace `IForgeContainerType` with `IForgeMenuType` in `RegisteredContainers.java`.

1. Read the requirements file; scope is `inventory.container`, type `fix`.
2. Read `src/main/java/bassebombecraft/inventory/container/RegisteredContainers.java` and the "container → menu" entry in `forge-api-migration-patterns.md`.
3. Checklist: MODIFY `RegisteredContainers.java` (import + one `create(...)` call).
4. Edit the file; no new constants, no registration change.
5. `./gradlew compileJava` — if a different error now appears in the same file, fix it; if it is in another file, note it for a separate requirements file.
6. `./gradlew build`.
7. Report: no tests (no test setup), `runClient` smoke check not run → list the manual validation row from the requirements file.

## Example 2 — Runtime registration bug (`1455-frozen-registry-bug.md`)

**Requirements**: change `RegisteredItems.register(Supplier<Item>)` to `register(Class<T>)` so items are created lazily; update all call sites.

1. Read the requirements file, including the "Root-cause code" and "Proposed fix" blocks.
2. Read `RegisteredItems.java`'s `register` helper and a few call sites; confirm each registered class has a no-arg constructor (the requirements file asserts this — verify rather than trust).
3. Checklist: MODIFY `RegisteredItems.java` (helper + every call site); AUDIT `GenericRightClickedBook` / `GenericCompositeItemsBook` constructors for registry-dependent work.
4. Implement the helper first, then the call sites (so the file compiles in one step).
5. `./gradlew compileJava`, `./gradlew build`.
6. This change affects startup, so a compile pass is not enough: run `./gradlew runClient` and check that the mod reaches the main menu and `run/logs/debug.log` contains no "Registry is already frozen". If the client can't be run here, say so and hand the manual steps over.

## Example 3 — CI change (`1461-github-action-fails-to-find-jar.md`)

**Requirements**: fix `.github/workflows/build.yml` so the built jar is located.

1. Read the workflow and the relevant `build.gradle` / `gradle.properties` values (`archivesName`, `mod_version`).
2. Checklist: MODIFY `.github/workflows/build.yml` (and `build.gradle` if the artifact name must change).
3. Compare the jar name Gradle produces (`./gradlew build`, then list `build/libs/`) with the path the workflow expects.
4. Report which part could not be verified locally (the GitHub Actions run itself).

## Pattern → source lookup

| Implementation topic | Where to look |
|----------------------|---------------|
| Removed/renamed Forge or Minecraft API | [forge-api-migration-patterns.md](../../forge-upgrade-pr/references/forge-api-migration-patterns.md) |
| New item / book | `item/RegisteredItems.java`, an existing class in `item/book/` (e.g. `TeleportBook`) |
| Registrables (entities, effects, sounds, containers, particles) | the matching `Registered*.java` |
| Event handler | an existing `@Mod.EventBusSubscriber` class in `event/` or `client/event/` |
| Rendering | `client/event/rendering/` and `client/rendering/RenderingUtils.java` |
| Network packet | `network/NetworkChannelHelper.java`, `network/packet/` |
| Composable logic | `operator/Operator2.java`, `Operators2.java`, `Ports.java` |
| Config | `config/ModConfiguration.java` |
| Shared constants | `ModConstants.java`, `ClientModConstants.java` |
