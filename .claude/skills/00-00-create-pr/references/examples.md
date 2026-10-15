# Worked Examples — 00-00-create-pr

Two examples showing the expected level of detail. Example 1 reproduces the shape of the real `.github/.requirements/pr/1441-iforgecontainertype-import-fails.md` file, extended with the Implementation Plan and Manual validation sections this skill's template adds. Example 2 is **illustrative**: the book and its class names are placeholders, not an existing feature.

---

## Example 1 — API-migration fix

**Problem description from the user**: "Build fails with: `The import net.minecraftforge.common.extensions.IForgeContainerType cannot be resolved`."

**Inferred**: package `inventory.container`, type `fix`, scope `container`, one file affected, 1:1 rename (Forge 40.x renamed container → menu). Issue number given by the user: 1441. File: `.github/.requirements/pr/1441-iforgecontainertype-import-fails.md`.

```markdown
# fix(container): Replace IForgeContainerType with IForgeMenuType for Forge 40.x

## Summary
Fix the unresolvable import `net.minecraftforge.common.extensions.IForgeContainerType` in `RegisteredContainers.java` by replacing it with the Forge 40.x equivalent.

## Motivation
In Minecraft 1.18.2 `ContainerType` was renamed `MenuType`, and Forge followed by renaming `IForgeContainerType` to `IForgeMenuType`. The old class does not exist in Forge 40.3.0, so compilation fails with
`The import net.minecraftforge.common.extensions.IForgeContainerType cannot be resolved`.

## Changes

### Files Updated

- **`src/main/java/bassebombecraft/inventory/container/RegisteredContainers.java`** — Replace the import with `net.minecraftforge.common.extensions.IForgeMenuType` and replace the `IForgeContainerType.create(...)` call with `IForgeMenuType.create(...)`.

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Replace the type

Change the import and the single `create(...)` call in `RegisteredContainers.java`. The constructor reference passed to `create` is unchanged.

### Phase 2 — Validate

Run `./gradlew compileJava`; fix any follow-on errors in the same file (e.g. a changed `create` lambda signature) before running `./gradlew build`.

## Testing

No unit tests are planned: the project has no `src/test` directory, and this is a 1:1 rename with no behavioural change.

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: Compilation fix only.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | Composite magic item GUI still opens | Launch client, right-click a composite item; the container screen appears |

## Related Issues
Closes #1441

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
Direct 1:1 API replacement; no logic changes.
```

---

## Example 2 — New feature (illustrative)

**Problem description from the user**: "Add a book that applies a levitation-style effect to targeted mobs, configurable like the other books."

**Inferred**: package `item.book` plus `config`, type `feat`, scope `item`. Books here are small subclasses of `GenericCompositeItemsBook`, composed from existing registered items and given an item config from `ModConfiguration` (see `TeleportBook`: `super(teleportBook, splComposites)`). No issue number given, so the next number after the highest existing prefix is used (here assumed 1462). The book and its component items are placeholders.

Key sections of `.github/.requirements/pr/1462-levitation-book.md` (the rest of the template is filled in the same way as Example 1):

```markdown
# feat(item): Add levitation book

## Summary
Adds `LevitationBook`, a composite book registered next to the existing books, with its own item config in `ModConfiguration`.

## Changes

### Files Updated

- **`src/main/java/bassebombecraft/item/book/LevitationBook.java`** — New book class modelled on `TeleportBook`: extends `GenericCompositeItemsBook`, passes the `levitationBook` config and a `Supplier<Stream<RegistryObject<Item>>>` of component items.
- **`src/main/java/bassebombecraft/item/RegisteredItems.java`** — Register with `LEVITATION_BOOK = register(LevitationBook.class)` alongside the other books.
- **`src/main/java/bassebombecraft/config/ModConfiguration.java`** — New `levitationBook` item config, declared and loaded the same way as `teleportBook`.

## Implementation Plan

### Phase 1 — Config
Declare the `levitationBook` config in `ModConfiguration` following `teleportBook` (builder section, defaults, field).

### Phase 2 — Item
Create `LevitationBook` (one public type per file, Javadoc on the class, `ITEM_NAME` constant like the other books).

### Phase 3 — Registration
Register through `RegisteredItems` using the class-literal `register(...)` helper so the item is instantiated lazily (never call `.get()` during class initialisation).

### Phase 4 — Validate
`./gradlew compileJava`, `./gradlew build`, then the in-game checks below.

## Testing

No unit tests planned: no `src/test` directory or JUnit dependency exists. Verified in-game instead.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | Mod reaches main menu | `./gradlew runClient`; no frozen-registry error in `run/logs/debug.log` |
| 2 | Book appears in the creative tab | Open the BasseBombeCraft tab |
| 3 | Book works | Right-click with the book; the composed effect fires |
| 4 | Config respected | Change the `levitationBook` values in the generated TOML, restart, repeat check 3 |
```

## What good looks like

- The Motivation quotes the exact error text for a build failure and names the Forge/Minecraft version responsible.
- Every file in Changes is a real path (verified by search) or starts with `New ...`.
- Implementation phases are ordered so each one compiles on its own.
- The Testing section states plainly whether unit tests exist, rather than listing tests the project cannot run.
