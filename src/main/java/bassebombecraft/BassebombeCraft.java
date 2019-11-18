package bassebombecraft;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.config.ModConfiguration.loadConfig;
import static bassebombecraft.config.VersionUtils.validateVersion;
import static bassebombecraft.tab.ItemGroupFactory.createItemGroup;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.UnmodifiableConfig;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.commander.DefaultMobCommanderRepository;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.block.DefaultBlockDirectiveRepository;
import bassebombecraft.event.block.temporary.DefaultTemporaryBlockRepository;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.DefaultCharmedMobsRepository;
import bassebombecraft.event.entity.target.DefaultTargetedEntitiesRepository;
import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.event.entity.team.DefaultTeamRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.frequency.DefaultFrequencyRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.item.ItemRegistryEventHandler;
import bassebombecraft.event.particle.DefaultParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.proxy.ClientProxy;
import bassebombecraft.proxy.Proxy;
import bassebombecraft.proxy.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
	static Proxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	/**
	 * {@linkplain ItemGroup} which implements creative tab.
	 */
	static final ItemGroup MOD_ITEMGROUP = createItemGroup(TAB_NAME);

	/**
	 * Charmed Mob repository
	 */
	CharmedMobsRepository charmedMobsRepository;

	/**
	 * Block directives repository. Server side.
	 */
	BlockDirectivesRepository blockDirectivesRepository;

	/**
	 * Particle rendering repository. Client side.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Temporary block repository.
	 */
	TemporaryBlockRepository tempBlockRepository;

	/**
	 * Mob commander repository.
	 */
	MobCommanderRepository mobCommanderRepository;

	/**
	 * Team repository.
	 */
	TeamRepository teamRepository;

	/**
	 * Targeted entities repository.
	 */
	TargetedEntitiesRepository targetedEntitiesRepository;

	/**
	 * Frequency repository.
	 */
	FrequencyRepository frequencyRepository;

	/**
	 * Minecraft server.
	 */
	MinecraftServer server;

	/**
	 * Random generator
	 */
	static Random random = new Random();

	/**
	 * BassebombeCraft constructor.
	 */
	public BassebombeCraft() {

		// store mod instance
		instance = this;

		// Register ourselves for forge events
		MinecraftForge.EVENT_BUS.register(this);

		try {

			// load configuration
			loadConfig();

			// initialise frequency repository
			frequencyRepository = DefaultFrequencyRepository.getInstance();

			// Initialise charmed mobs repository
			charmedMobsRepository = DefaultCharmedMobsRepository.getInstance();

			// Initialise directives repository
			blockDirectivesRepository = DefaultBlockDirectiveRepository.getInstance();

			// Initialise temporary block repository
			tempBlockRepository = DefaultTemporaryBlockRepository.getInstance();

			// Initialise particle rendering repository
			particleRepository = DefaultParticleRenderingRepository.getInstance();

			// Initialise mob commander repository
			mobCommanderRepository = DefaultMobCommanderRepository.getInstance();

			// initialise team repository
			teamRepository = DefaultTeamRepository.getInstance();

			// initialise targeted entities repository
			targetedEntitiesRepository = DefaultTargetedEntitiesRepository.getInstance();

		} catch (ExceptionInInitializerError e) {
			reportAndLogException(e);
			throw e;
		} catch (Exception e) {
			reportAndLogException(e);
			throw e;
		}

		// register event handler for FMLCommonSetupEvent event on the mod event bus
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}

	@SubscribeEvent
	void setup(FMLCommonSetupEvent event) {
		validateVersion();
		// initializeWorldGenerators();
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

	/**
	 * Initialize world generators.
	 */
	void initializeWorldGenerators() {
		/*
		 * GameRegistry.registerWorldGenerator(new RandomModStructuresGenerator(),
		 * ModConstants.MOD_STRUCUTRE_GENERATOR_WEIGHT);
		 */
	}

	/**
	 * Get proxy.
	 * 
	 * @return proxy.
	 */
	public static Proxy getProxy() {
		return proxy;
	}

	/**
	 * Get charmed mobs repository.
	 * 
	 * @return charmed mobs repository.
	 */
	public CharmedMobsRepository getCharmedMobsRepository() {
		return charmedMobsRepository;
	}

	/**
	 * Get block directives repository.
	 * 
	 * @return block directives repository.
	 */
	public BlockDirectivesRepository getBlockDirectivesRepository() {
		return blockDirectivesRepository;
	}

	/**
	 * Get temporary block repository.
	 * 
	 * @return temporary block repository.
	 */
	public TemporaryBlockRepository getTemporaryBlockRepository() {
		return tempBlockRepository;
	}

	/**
	 * Particle rendering repository.
	 * 
	 * @return particle rendering repository.
	 */
	public ParticleRenderingRepository getParticleRenderingRepository() {
		return particleRepository;
	}

	/**
	 * Get mob commander repository.
	 * 
	 * @return mob commander repository.
	 */
	public MobCommanderRepository getMobCommanderRepository() {
		return mobCommanderRepository;
	}

	/**
	 * Get team repository.
	 * 
	 * @return team repository.
	 */
	public TeamRepository getTeamRepository() {
		return teamRepository;
	}

	/**
	 * Get targeted entities repository.
	 * 
	 * @return targeted entities repository.
	 */
	public TargetedEntitiesRepository getTargetedEntitiesRepository() {
		return targetedEntitiesRepository;
	}

	/**
	 * Get frequency repository.
	 * 
	 * @return frequency repository
	 */
	public FrequencyRepository getFrequencyRepository() {
		return frequencyRepository;
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
	 * Get array of inventory items.
	 * 
	 * @return array of inventory items.
	 */
	public Item[] getInventoryItems() {
		return ItemRegistryEventHandler.getInventoryItems();
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
		} catch (OperationNotSupportedException e) {
			getBassebombeCraft().reportAndLogException(e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get server.
	 * 
	 * @return server.
	 */
	public MinecraftServer getServer() {
		return server;
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
	 * Get Minecraft client.
	 * 
	 * @return Minecraft client
	 */
	public static Minecraft getMincraft() {
		return Minecraft.getInstance();
	}

	/**
	 * Exception reporting facility.
	 * 
	 * @param e exception to report and log.
	 */
	public void reportAndLogException(Throwable e) {
		Optional<String> nullableString = Optional.ofNullable(e.getMessage());
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

}
