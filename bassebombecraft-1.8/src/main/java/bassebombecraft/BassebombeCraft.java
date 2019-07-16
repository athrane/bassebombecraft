package bassebombecraft;

import static bassebombecraft.ModConstants.DOWNLOAD_URL;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.NAME;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.config.VersionUtils.validateVersion;
import static bassebombecraft.tab.ItemGroupFactory.*;

import java.io.File;
import java.util.Random;

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
import bassebombecraft.proxy.CommonProxy;
import bassebombecraft.world.RandomModStructuresGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@net.minecraftforge.fml.common.Mod(name = NAME, modid = MODID, version = VERSION)
public class BassebombeCraft {

	/**
	 * Logger.
	 */
	// static Logger logger = LogManager.getLogger(BassebombeCraft.class);
	Logger logger = null;

	/**
	 * Mod singleton class. This is the object reference to your class that Forge
	 * uses. Make sure that the argument is the modid in @Mod. Otherwise, it'll
	 * default to the empty string, and cause problems with any mod that also does
	 * that.
	 */
	@Instance(value = MODID)
	public static BassebombeCraft instance;

	/**
	 * Minecraft uses a client and server setup, even on single player. The server
	 * side does all the work maintaining the world's state while the client renders
	 * the world. The thing is though, that all code runs on both the client and
	 * server side unless specified otherwise. There are two annotations for
	 * specifying code be ran on only one side. The annotation @SidedProxy is used
	 * when you want the server to call the constructor of one class and the client
	 * another. Both classes need to be the same type or subtype of the field, and
	 * the names of the classes are passed as Strings.
	 */
	@SidedProxy(clientSide = "bassebombecraft.proxy.ClientProxy", serverSide = "bassebombecraft.proxy.CommonProxy")
	static CommonProxy proxy;

	/**
	 * {@linkplain ItemGroup} which implements creative tab.
	 */
	static ItemGroup modItemGroup = createItemGroup(TAB_NAME);
	
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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);

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

		initializeModMetadata(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Starting to initialize BasseBombeCraft");
		proxy.init(event);

		validateVersion(logger);
		ProjectileInitializer.getInstance().initialize(this, modTab);
		BlockInitializer.getInstance().initialize(modTab);
		initializeWorldGenerators();
		logger.info("Initialized BasseBombeCraft " + VERSION);
	}

	@EventHandler
	public void serverAboutTostart(FMLServerAboutToStartEvent event) {
		server = event.getServer();
	}

	@EventHandler
	public void serverStarted(FMLServerStartedEvent event) {
		proxy.startAnalyticsSession(logger);
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		proxy.endAnalyticsSession();
		server = null;
	}

	/**
	 * Initialize mod meta data.
	 * 
	 * @param event
	 *            initialization event.
	 */
	void initializeModMetadata(FMLPreInitializationEvent event) {
		ModMetadata m = event.getModMetadata();
		m.autogenerated = false;
		m.modId = NAME;
		m.version = VERSION;
		m.name = NAME;
		m.description = "A collection of 80+ magical books, idols and blocks for adventuring and construction. The magic needed by BasseBombe when he plays Minecraft.";
		m.authorList.add("einheriii@gmail.com");
		m.logoFile = "assets/bassebombecraft/logo/logo.png";
		m.url = DOWNLOAD_URL;
		m.credits = "Allan & Andreas Thrane Andersen";
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
	public static CommonProxy getProxy() {
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
		return proxy.getUser();
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
