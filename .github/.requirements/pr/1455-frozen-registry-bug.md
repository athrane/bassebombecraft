# fix(items): Resolve "Registry is already frozen" crash on mod startup

## Summary
`RegisteredItems.register()` eagerly calls `splItem.get()` to derive the registry key during the class static initialiser, which instantiates every `Item` subclass before Forge's item registry is ready. This causes an `IllegalStateException: Registry is already frozen` crash that prevents the mod from loading.

## Motivation
The Forge `DeferredRegister` pattern requires that item instances are created **lazily** — only when Forge fires the `RegistryEvent` — not during `Class` initialisation. The current helper method defeats this by calling `splItem.get()` immediately to obtain the class simple-name used as the registry key. The resulting premature `Item` construction triggers `Item.<init>` → `NamespacedDefaultedWrapper.createIntrusiveHolder` → frozen-registry exception before a single item is registered.

Stack trace excerpt confirming the sequence:
```
Caused by: java.lang.IllegalStateException: Registry is already frozen
    at net.minecraftforge.registries.NamespacedHolderHelper.createIntrusiveHolder(NamespacedHolderHelper.java:177)
    at net.minecraft.world.item.Item.<init>(Item.java:58)
    at bassebombecraft.item.book.GenericRightClickedBook.<init>(GenericRightClickedBook.java:64)
    at bassebombecraft.item.baton.MobCommandersBaton.<init>(MobCommandersBaton.java:15)
    at bassebombecraft.item.RegisteredItems.register(RegisteredItems.java:334)
    at bassebombecraft.item.RegisteredItems.<clinit>(RegisteredItems.java:169)
    at bassebombecraft.BassebombeCraft.<init>(BassebombeCraft.java:122)
```

## Changes
- [ ] Change 1: Refactor `RegisteredItems.register(Supplier<Item>)` to accept `Class<T>` as the sole parameter. The registry key is derived from `itemClass.getSimpleName().toLowerCase()` and the item is instantiated lazily via reflection inside the `DeferredRegister` supplier.
- [ ] Change 2: Update every call site in `RegisteredItems` to pass the item class literal (e.g. `register(MobCommandersBaton.class)`).
- [ ] Change 3: Verify `GenericRightClickedBook` and `GenericCompositeItemsBook` constructors do **not** perform any registry-dependent work outside a `DeferredRegister` supplier context.

### Root-cause code (before fix)
File: `src/main/java/bassebombecraft/item/RegisteredItems.java`, line 334
```java
// BUG: splItem.get() instantiates the item eagerly during <clinit>
static RegistryObject<Item> register(Supplier<Item> splItem) {
    String key = splItem.get().getClass().getSimpleName().toLowerCase();
    return ITEMS_REGISTRY.register(key, splItem);
}
```

### Proposed fix (after)
```java
// FIX: derive key from the class literal — no eager instantiation
// Item is instantiated lazily via reflection inside the DeferredRegister supplier
static <T extends Item> RegistryObject<Item> register(Class<T> itemClass) {
    String key = itemClass.getSimpleName().toLowerCase();
    return ITEMS_REGISTRY.register(key, () -> {
        try {
            return itemClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate item: " + itemClass.getName(), e);
        }
    });
}
```

### Example call-site update
```java
// Before
public static final RegistryObject<Item> BATON = register(MobCommandersBaton::new);

// After
public static final RegistryObject<Item> BATON = register(MobCommandersBaton.class);
```

### Why this is safe
All item classes currently registered in `RegisteredItems` use no-arg constructor references (e.g. `MobCommandersBaton::new`), confirming they all have no-arg constructors. `getDeclaredConstructor()` with no arguments will succeed for every registered item class.

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
- [x] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: Launch the client via `runClient` and confirm the mod reaches the main menu without a frozen-registry error in `run/logs/debug.log`. Verify all items are accessible under the BasseBombeCraft creative tab.

## Related Issues
Closes #145

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

## Additional Notes

### Affected files
| File | Change required |
|------|----------------|
| `src/main/java/bassebombecraft/item/RegisteredItems.java` | Refactor `register()` signature; update all ~80+ call sites |
| `src/main/java/bassebombecraft/item/book/GenericRightClickedBook.java` | Audit constructor — no additional change expected |
| `src/main/java/bassebombecraft/item/book/GenericCompositeItemsBook.java` | Audit constructor — no additional change expected |
| `src/main/java/bassebombecraft/item/baton/MobCommandersBaton.java` | Call-site update only |

### Why the single class-literal approach is preferred
Passing `Class<T>` as the sole parameter keeps the call sites as concise as possible (one argument instead of two), matches Forge's own internal patterns, and is safe across all Forge 40.x releases targeting Minecraft 1.18.2. The supplier lambda is constructed internally using `getDeclaredConstructor().newInstance()`, which is deferred until Forge fires the registry event. Alternative approaches such as deriving the key via reflection on the supplier lambda are fragile and JVM-implementation dependent.

### Risk assessment
Low risk. The registry key produced by `itemClass.getSimpleName().toLowerCase()` is identical to the string that `splItem.get().getClass().getSimpleName().toLowerCase()` returned at run-time; therefore all existing item registry names and data-pack references remain unchanged. All currently registered item classes have confirmed no-arg constructors, so the reflection-based instantiation introduces no behavioural change.