package bassebombecraft;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.client.particles.RegisteredParticles.PARTICLE_REGISTRY;
import static bassebombecraft.config.ModConfiguration.loadConfig;
import static bassebombecraft.config.VersionUtils.validateVersion;
import static bassebombecraft.inventory.container.RegisteredContainers.CONTAINER_REGISTRY;
import static bassebombecraft.item.RegisteredItems.COMPOSITE;
import static bassebombecraft.item.RegisteredItems.ITEMS_REGISTRY;
import static bassebombecraft.potion.RegisteredPotions.POTION_REGISTRY;
import static bassebombecraft.potion.effect.RegisteredEffects.EFFECT_REGISTRY;
import static bassebombecraft.sound.RegisteredSounds.SOUNDS_REGISTRY;
import static bassebombecraft.tab.ItemGroupFactory.createItemGroup;
import static java.util.Optional.ofNullable;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;
import static net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext.get;
import static bassebombecraft.entity.attribute.RegisteredAttributes.ATTRIBUTE_REGISTRY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import bassebombecraft.client.proxy.ClientProxy;
import bassebombecraft.config.ModConfiguration;
import bassebombecraft.proxy.Proxy;
import bassebombecraft.proxy.ServerProxy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;

@Mod(MODID)
public class BassebombeCraft {

	/**
	 * Log4j logger.
	 */
	static Logger logger = LogManager.getLogger();

	/**
	 * Mod instance.
	 */
	static BassebombeCraft instance;

	/**
	 * Distributed executor for execution of client and server side code.
	 */
	static Proxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

	/**
	 * {@linkplain ItemGroup} which implements creative tab.
	 */
	static final ItemGroup MOD_ITEMGROUP = createItemGroup(TAB_NAME);

	/**
	 * Minecraft server.
	 */
	MinecraftServer server;

	/**
	 * Random generator
	 */
	static Random random = new Random();

	/**
	 * Consumer for registration of client rendering.
	 */
	Consumer<FMLClientSetupEvent> clientSetupEventHandler = t -> {
		try {
			setupClient(t);
		} catch (Exception e) {
			reportAndLogException(e);
		}
	};

	/**
	 * BassebombeCraft constructor.
	 */
	public BassebombeCraft() {

		// store mod instance
		instance = this;

		try {

			// Register ourselves for forge events
			EVENT_BUS.register(this);

			// load configuration
			loadConfig();

			// get event bus
			IEventBus modEventBus = get().getModEventBus();

			// Register event handler for FMLClientSetupEvent event on the mod event bus
			// The event handler initialises the client renders
			modEventBus.addListener(clientSetupEventHandler);

			// do deferred registration of objects
			PARTICLE_REGISTRY.register(modEventBus);
			CONTAINER_REGISTRY.register(modEventBus);
			ATTRIBUTE_REGISTRY.register(modEventBus);			
			ITEMS_REGISTRY.register(modEventBus);
			SOUNDS_REGISTRY.register(modEventBus);
			EFFECT_REGISTRY.register(modEventBus);
			POTION_REGISTRY.register(modEventBus);

		} catch (ExceptionInInitializerError e) {
			reportAndLogException(e);
			throw e;
		} catch (Exception e) {
			reportAndLogException(e);
			throw e;
		}
	}

	@SubscribeEvent
	void serverAboutTostart(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}

	@SubscribeEvent
	void serverStarted(FMLServerStartedEvent event) {
		proxy.startAnalyticsSession();
	}

	@SubscribeEvent
	void serverStopped(FMLServerStoppedEvent event) {
		proxy.endAnalyticsSession();
		server = null;
	}

	@SubscribeEvent
	void playerLoggedIn(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		validateVersion(player);
	}

	@SubscribeEvent
	void setupClient(FMLClientSetupEvent event) throws Exception {
		proxy.setupClientSideRendering();
	}

	/**
	 * Get sided proxy.
	 * 
	 * @return sided proxy.
	 */
	public static Proxy getProxy() {
		return proxy;
	}

	/**
	 * Get mod configuration from TOML file.
	 * 
	 * @return mod configuration from TOML file.
	 */
	public UnmodifiableConfig getTomlConfiguration() {
		return ModConfiguration.COMMON_CONFIG.getValues();
	}

	/**
	 * Get item for rendering as creative tab icon.
	 * 
	 * @return item for creative tab icon.
	 */
	public Item getCreativeTabItem() {
		return COMPOSITE.get();
	}

	/**
	 * Get mod instance.
	 * 
	 * @return mod instance.
	 */
	public static BassebombeCraft getBassebombeCraft() {
		return instance;
	}

	/**
	 * Get item group (which implements the creative tab).
	 * 
	 * @return item group.
	 */
	public static ItemGroup getItemGroup() {
		return MOD_ITEMGROUP;
	}

	/**
	 * Get logger.
	 * 
	 * @return logger.
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Get user.
	 * 
	 * @return user.
	 */
	public String getUser() {
		try {
			return proxy.getUser();
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get server.
	 * 
	 * @return server. Can be null in physical client, if an error happens before
	 *         the logical server is started.
	 */
	public Optional<MinecraftServer> getServer() {
		return ofNullable(server);
	}

	/**
	 * Get random generator.
	 * 
	 * @return random generator.
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Exception reporting facility.
	 * 
	 * @param e exception to report and log.
	 */
	public void reportAndLogException(Throwable e) {
		Optional<String> nullableString = ofNullable(e.getMessage());
		nullableString.ifPresent(s -> logger.error(s));

		// get and log stack trace
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktraceString = sw.toString();
		logger.error(stacktraceString);

		reportException(e);
	}

	/**
	 * Exception reporting facility.
	 * 
	 * @param e exception to report.
	 */
	public void reportException(Throwable e) {
		proxy.postException(e);
	}

	/**
	 * Exception reporting facility.
	 * 
	 * @param msg error to report.
	 */
	public void reportAndLogError(String msg) {
		proxy.postError(msg);
		logger.error(msg);
	}

	/**
	 * Log stack trace as debug.
	 */
	public void logStacktraceAsDebug() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		Arrays.asList(ste).forEach(logger::debug);
	}

}
