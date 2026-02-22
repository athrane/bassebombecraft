# Forge API Migration Patterns

Catalogue of removed, renamed, or access-restricted APIs encountered during Minecraft Forge mod upgrades. Use this reference to identify the correct replacement when the compiler reports `cannot find symbol` or `has private access`.

---

## Forge 40.x (Minecraft 1.18.2)

### Rendering — World/Level Last Event

| Removed | Replacement | Notes |
|---------|-------------|-------|
| `net.minecraftforge.client.event.RenderWorldLastEvent` | `net.minecraftforge.client.event.RenderLevelLastEvent` | 1:1 class rename; method surface identical (`getPoseStack()`, `getPartialTick()`) |

### Rendering — Entity / Player Layer Events

| Removed method | Replacement | Notes |
|----------------|-------------|-------|
| `event.getMatrixStack()` on `RenderPlayerEvent.Pre` / `Post` | `event.getPoseStack()` | Method renamed in line with Mojang mappings rename of `MatrixStack` → `PoseStack` |
| `event.getLight()` on entity render events | `event.getPackedLight()` | Method renamed |

### Networking — Legacy Shim Removed

| Removed | Replacement | Notes |
|---------|-------------|-------|
| `net.minecraftforge.fmllegacy.network.NetworkRegistry.newSimpleChannel` | `net.minecraftforge.network.NetworkRegistry.newSimpleChannel` | Drop `fmllegacy` segment |
| `net.minecraftforge.fmllegacy.network.NetworkHooks.openGui` | `net.minecraftforge.network.NetworkHooks.openScreen` | Also renamed: `openGui` → `openScreen` |
| `net.minecraftforge.fmllegacy.network.NetworkHooks.getEntitySpawningPacket` | `net.minecraftforge.network.NetworkHooks.getEntitySpawningPacket` | Drop `fmllegacy` segment; or use `IEntityAdditionalSpawnData` |
| `net.minecraftforge.fmllegacy.network.simple.SimpleChannel` | `net.minecraftforge.network.simple.SimpleChannel` | Drop `fmllegacy` segment |
| `Context` from `fmllegacy` network package | `net.minecraftforge.network.NetworkEvent.Context` | Fully qualified inner class |

### Networking — Server Lifecycle Events

| Removed | Replacement | Package |
|---------|-------------|---------|
| `FMLServerAboutToStartEvent` | `ServerAboutToStartEvent` | `net.minecraftforge.event.server` |
| `FMLServerStartedEvent` | `ServerStartedEvent` | `net.minecraftforge.event.server` |
| `FMLServerStoppedEvent` | `ServerStoppedEvent` | `net.minecraftforge.event.server` |

### AI — GoalSelector Access

| Error | Cause | Fix |
|-------|-------|-----|
| `availableGoals has private access in GoalSelector` | Field changed to private | Read: `selector.getAvailableGoals()` returns unmodifiable view. Write: `selector.addGoal(priority, goal)` and `selector.removeGoal(goal)` |

### Registry

| Removed | Replacement | Notes |
|---------|-------------|-------|
| `RegistryObject` from `net.minecraftforge.fmllegacy.RegistryObject` | `net.minecraftforge.registries.RegistryObject` | Drop `fmllegacy` segment |
| `IForgeContainerType` | `IForgeMenuType` | `MenuType` replaced `ContainerType` in 1.18 |

---

## Forge 36.x–38.x (Minecraft 1.17.x)

### Rendering — BufferBuilder

| Removed | Replacement | Notes |
|---------|-------------|-------|
| `BufferBuilder.begin(int mode, VertexFormat format)` | `BufferBuilder.begin(VertexFormat.Mode mode, VertexFormat format)` | Mode is now an enum, not an `int`; e.g. `GL11.GL_QUADS` → `VertexFormat.Mode.QUADS` |

### Rendering — RenderSystem

| Removed call | Replacement |
|--------------|-------------|
| `RenderSystem.color4f(r, g, b, a)` | `RenderSystem.setShaderColor(r, g, b, a)` |
| `RenderSystem.enableAlphaTest()` / `disableAlphaTest()` | Removed — handled by shader; delete the call |
| `RenderSystem.enableBlend()` | Still present but moved to `com.mojang.blaze3d.systems.RenderSystem` |

### NBT — Deprecated Capability Methods

| Deprecated | Replacement |
|------------|-------------|
| `CompoundNBT` | `CompoundTag` (Mojang mapping rename) |
| `INBT` | `Tag` |
| `ListNBT` | `ListTag` |
| `StringNBT` | `StringTag` |

---

## Forge 41.x+ / NeoForge 20.x (Minecraft 1.20+)

### Rendering — Level/World

| Removed | Replacement |
|---------|-------------|
| `RenderLevelLastEvent` | `RenderLevelStageEvent` with stage `AFTER_LEVEL` |

### Networking — Payload API (NeoForge)

| Old pattern | New pattern |
|-------------|-------------|
| `SimpleChannel` + manual codec | `IPayload` record + `registerPlayPayload` |

---

## General Ranking Guidance

When multiple errors appear, use this subsystem priority table:

| Priority | Subsystem | Typical Blast Radius |
|----------|-----------|----------------------|
| 1 | Networking (`SimpleChannel`, `NetworkEvent.Context`) | 5–10 packet classes + helper |
| 2 | Rendering pipeline (level/world events, `PoseStack`) | 4–8 renderer classes |
| 3 | Entity AI (`GoalSelector`) | 1–2 utility classes, high functional impact |
| 4 | Registry (`RegistryObject`, `DeferredRegister`) | Multiple registration classes |
| 5 | Item/Block/Container API renames | 1–3 classes per symbol |
| 6 | NBT rename (`CompoundNBT` → `CompoundTag`) | Many files, but purely mechanical |

---

## How to Find Replacements Not Listed Here

1. Search the [Forge changelog](https://github.com/MinecraftForge/MinecraftForge/blob/1.18.x/Changelog.md) for the affected class name.
2. Use the Forge decompiled sources — search `src/main/java` for the removed symbol to confirm removal.
3. Check [Forge community PRs](https://github.com/MinecraftForge/MinecraftForge/pulls) with `migration` label.
4. For NeoForge: see [NeoForge migration guide](https://docs.neoforged.net/docs/migrating).
