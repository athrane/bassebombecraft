# Project patterns

Patterns used in BasseBombeCraft (Java 17, Forge 40.3.0, Minecraft 1.18.2). Check the affected package for an existing implementation before introducing anything new.

## Entry points and layout

- Package root `bassebombecraft`; all source under `src/main/java/bassebombecraft/`.
- `BassebombeCraft.java` is the mod entry point: lifecycle events and proxy dispatch.
- `ModConstants.java` / `ClientModConstants.java` hold shared constants.
- Packages: `block`, `client`, `color`, `config`, `entity`, `event`, `geom`, `inventory`, `item`, `network`, `operator`, `player`, `potion`, `projectile`, `proxy`, `sound`, `structure`, `tab`, `util`, `world`.

## Registration

- `DeferredRegister<T>` fields in `Registered*.java` (`RegisteredItems`, `RegisteredBlocks`, `RegisteredEntities`, and so on); read values with `RegistryObject<T>.get()`.
- New item: `Item` subclass, registered in `RegisteredItems` with the class-literal `register(Foo.class)` helper; add a creative-tab entry where applicable.
- New block: `Block` subclass, registered in `RegisteredBlocks`.
- New entity: register in `RegisteredEntities`; add AI goals during `FMLCommonSetupEvent`.
- **Never call a supplier's `.get()` during class initialisation.** It triggers "Registry is already frozen" (#1455).

## Event handlers

- Annotate the class `@Mod.EventBusSubscriber(modid = MODID)` and each handler method `@SubscribeEvent`.
- `MinecraftForge.EVENT_BUS` for gameplay events; the mod event bus (`FMLJavaModLoadingContext.get().getModEventBus()`) for lifecycle events.

## Operators

- `Operator2` is a functional interface composed with `Operators2.run()`.
- All input and output goes through `Ports`. Read every input at the top of `run()`, check with `Operators2.validateNotNull()`, write results with the `ports.set*()` methods.
- No raw Minecraft server calls inside an operator.

## Config

- `ForgeConfigSpec` builder in `ModConfiguration`; defaults are reflected in the generated TOML (`run/config/bassebombecraft-common.toml`).
- Add the default value as a constant in `ModConstants`.

## Network

- `SimpleChannel` packets in the `network/` package, registered through `NetworkChannelHelper`.

## Client and server

- `DistExecutor.runForDist()` selects `ClientProxy` or `ServerProxy` at runtime.
- Client-only rendering code lives in `client/`; keep it out of classes loaded on a dedicated server.

## Methods

- Guard clauses and early returns first; happy path un-nested.
- Static factories (`getInstance()`, `create()`) on utility and singleton classes.
- Javadoc on every public class, method, constructor and field.

## Tests

The project has no `src/test` directory and no JUnit or Mockito dependency in `build.gradle`. If a requirements file expects unit tests, say so and add the dependency as a separate, declared step. Once a test setup exists, mirror the source package layout under `src/test/java/bassebombecraft/` and name files `[ClassName]Test.java`.

## Maintenance

Update this file when the project moves to a new Forge or Minecraft version, introduces a new architectural pattern, or gains a test framework.
