package bassebombecraft;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.config.VersionUtils.validateVersion;
import static bassebombecraft.tab.ItemGroupFactory.createItemGroup;

import java.io.File;
import java.util.Random;

import javax.naming.OperationNotSupportedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.typesafe.config.Config;

import bassebombecraft.block.BlockInitializer;
import bassebombecraft.config.ConfigUtils;
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
import bassebombecraft.projectile.ProjectileInitializer;
import bassebombecraft.proxy.ClientProxy;
import bassebombecraft.proxy.ClientProxyOLD;
import bassebombecraft.proxy.CommonProxy;
import bassebombecraft.proxy.Proxy;
import bassebombecraft.proxy.ServerProxy;
import bassebombecraft.world.RandomModStructuresGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
	public static BassebombeCraft instance;

	/**
	 * Distributed executor for execution of client and server side code.
	 */
	static Proxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

	/**
	 * {@linkplain ItemGroup} which implements creative tab.
	 */
	static final ItemGroup modItemGroup = createItemGroup(TAB_NAME);

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
	 * Mod configuration.
	 */
	Config config;

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
		super();

		// store mod instance
		instance = this;

		// Register the setup method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		// Register the doClientStuff method for mod loading
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	void setup(final FMLCommonSetupEvent event) {
		// some preinit code

		// load configuration file
		File configDirectory = event.getModConfigurationDirectory();
		config = ConfigUtils.loadConfig(configDirectory, logger);

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

		logger.info("Starting to initialize BasseBombeCraft");
		
		validateVersion(logger);
		
		// initialize MC entities
		ProjectileInitializer.getInstance().initialize(this, modTab);
		BlockInitializer.getInstance().initialize(modTab);
		initializeWorldGenerators();
		
		logger.info("Initialized BasseBombeCraft " + VERSION);
	}

	void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
	}

	@SubscribeEvent
	public void serverAboutTostart(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}

	@SubscribeEvent
	public void serverStarted(FMLServerStartedEvent event) {
		proxy.startAnalyticsSession();
	}

	@SubscribeEvent
	public void serverStopped(FMLServerStoppedEvent event) {
		proxy.endAnalyticsSession();
		server = null;
	}

	/**
	 * Initialize world generators.
	 */
	void initializeWorldGenerators() {
		GameRegistry.registerWorldGenerator(new RandomModStructuresGenerator(),
				ModConstants.MOD_STRUCUTRE_GENERATOR_WEIGHT);
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
	 * Get mod configuration.
	 * 
	 * @return mod configuration.
	 */
	public Config getConfiguration() {
		return config;
	}

	/**
	 * Get array of book items.
	 * 
	 * @return array of book items.
	 */
	public Item[] getBookItems() {
		return ItemRegistryEventHandler.getBookItems();
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
		return modItemGroup;
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
	 * Get frequency repository.
	 * 
	 * @return frequency repository
	 */
	public FrequencyRepository getFrequencyRepository() {
		return frequencyRepository;
	}

}
