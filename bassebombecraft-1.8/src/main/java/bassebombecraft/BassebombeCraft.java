package bassebombecraft;

import static bassebombecraft.ModConstants.DOWNLOAD_URL;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.NAME;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.ModConstants.VERSION;
import static bassebombecraft.config.VersionUtils.validateVersion;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.typesafe.config.Config;

import bassebombecraft.block.BlockInitializer;
import bassebombecraft.config.ConfigUtils;
import bassebombecraft.entity.commander.DefaultMobCommanderRepository;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.block.DefaultBlockDirectiveRepository;
import bassebombecraft.event.block.ProcessBlockDirectivesEventListener;
import bassebombecraft.event.block.temporary.DefaultTemporaryBlockRepository;
import bassebombecraft.event.block.temporary.TemporaryBlockEventListener;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.event.charm.CharmedMobEventListener;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.DefaultCharmedMobsRepository;
import bassebombecraft.event.entity.team.DefaultTeamRepository;
import bassebombecraft.event.entity.team.EntityTeamMembershipEventListener;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.particle.DefaultParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRenderingEventListener;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.item.ItemInitializer;
import bassebombecraft.player.pvp.DefaultPvpRepository;
import bassebombecraft.player.pvp.PvpEventListener;
import bassebombecraft.player.pvp.PvpRepository;
import bassebombecraft.projectile.ProjectileInitializer;
import bassebombecraft.proxy.CommonProxy;
import bassebombecraft.tab.CreativeTabFactory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

@net.minecraftforge.fml.common.Mod(name = NAME, modid = MODID, version = VERSION)
public class BassebombeCraft {

	/**
	 * Logger.
	 */
	// static Logger logger = LogManager.getLogger(BassebombeCraft.class);
	Logger logger = null;

	/**
	 * Mod singleton class. This is the object reference to your class that
	 * Forge uses. Make sure that the argument is the modid in @Mod. Otherwise,
	 * it'll default to the empty string, and cause problems with any mod that
	 * also does that.
	 */
	@Instance(value = MODID)
	public static BassebombeCraft instance;

	/**
	 * Minecraft uses a client and server setup, even on single player. The
	 * server side does all the work maintaining the world's state while the
	 * client renders the world. The thing is though, that all code runs on both
	 * the client and server side unless specified otherwise. There are two
	 * annotations for specifying code be ran on only one side. The
	 * annotation @SidedProxy is used when you want the server to call the
	 * constructor of one class and the client another. Both classes need to be
	 * the same type or subtype of the field, and the names of the classes are
	 * passed as Strings.
	 */
	@SidedProxy(clientSide = "bassebombecraft.proxy.ClientProxy", serverSide = "bassebombecraft.proxy.CommonProxy")
	static CommonProxy proxy;

	public static CreativeTabs modTab;

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
	 * PVP repository.
	 */
	PvpRepository pvpRepository;

	/**
	 * Mob commander repository.
	 */
	MobCommanderRepository mobCommanderRepository;

	/**
	 * Team repository.
	 */
	TeamRepository teamRepository;

	/**
	 * Mod configuration.
	 */
	Config config;

	/**
	 * Item initializer.
	 */
	ItemInitializer itemInitializer;

	/**
	 * Book item list.
	 */
	List<Item> bookItemList;

	/**
	 * Inventory item list.
	 */
	List<Item> inventoryItemList;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		proxy.preInit(event);

		// load configuration file
		File configDirectory = event.getModConfigurationDirectory();
		config = ConfigUtils.loadConfig(configDirectory, logger);

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

		// Initialise PVP repository
		pvpRepository = DefaultPvpRepository.getInstance();

		initializeModMetadata(event);
		initializeCreativeTab();
		logger.info("Pre-initialized BassebombeCraft");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Starting to initialize BasseBombeCraft");
		proxy.init(event);

		proxy.startAnalyticsSession(logger);
		validateVersion(logger);
		itemInitializer = ItemInitializer.getInstance();
		itemInitializer.initializeBasicItems(modTab);
		bookItemList = itemInitializer.initializeBooks(modTab);
		inventoryItemList = itemInitializer.initializeInventoryItems(modTab);
		itemInitializer.initializeBatons(modTab);		
		ProjectileInitializer.getInstance().initialize(this, modTab);
		BlockInitializer.getInstance().initialize(modTab);
		initializeEventListeners();
		logger.info("Initialized BasseBombeCraft " + VERSION);
	}

	@EventHandler
	public void shutdown(FMLServerStoppingEvent event) {
		proxy.endAnalyticsSession(logger);
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
		m.description = "A collection of 64+ magical books, idols and blocks for adventuring and construction. The magic needed by BasseBombe when he plays Minecraft.";
		m.authorList.add("einheriii@gmail.com");
		m.logoFile = "assets/bassebombecraft/logo/logo.png";
		m.url = DOWNLOAD_URL;
		m.credits = "Allan & Andreas Thrane Andersen";
	}

	/**
	 * Initialize creative tab.
	 */
	void initializeCreativeTab() {
		modTab = CreativeTabFactory.createCreativeTab(TAB_NAME);
	}

	/**
	 * Initialize event listeners.
	 */
	void initializeEventListeners() {

		// Initialise charmed mobs event listener
		CharmedMobEventListener mobsEventListener = new CharmedMobEventListener(charmedMobsRepository,
				particleRepository);
		MinecraftForge.EVENT_BUS.register(mobsEventListener);

		// Initialise process block directives event listener
		ProcessBlockDirectivesEventListener directivesEventListener = new ProcessBlockDirectivesEventListener(
				blockDirectivesRepository, particleRepository);
		MinecraftForge.EVENT_BUS.register(directivesEventListener);

		// Initialise temporary block event listener
		TemporaryBlockEventListener tempBlockEventListener = new TemporaryBlockEventListener(tempBlockRepository,
				blockDirectivesRepository);
		MinecraftForge.EVENT_BUS.register(tempBlockEventListener);

		// Initialise particle rendering event listener
		ParticleRenderingEventListener particleEventListener = new ParticleRenderingEventListener(particleRepository);
		MinecraftForge.EVENT_BUS.register(particleEventListener);

		// Initialise entity team membership event listener
		EntityTeamMembershipEventListener teamEventListener = new EntityTeamMembershipEventListener(teamRepository);
		MinecraftForge.EVENT_BUS.register(teamEventListener);

		// Initialise PVP event listener
		PvpEventListener pvpEventListener = new PvpEventListener(pvpRepository);
		MinecraftForge.EVENT_BUS.register(pvpEventListener);
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
	 * Particle PVP repository.
	 * 
	 * @return PVP repository.
	 */
	public PvpRepository getPvpRepository() {
		return pvpRepository;
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
	 * Get mod configuration.
	 * 
	 * @return mod configuration.
	 */
	public Config getConfiguration() {
		return config;
	}

	/**
	 * Get book item list.
	 * 
	 * @return book item list.
	 */
	public List<Item> getBookItems() {
		return bookItemList;
	}

	/**
	 * Get inventory item list.
	 * 
	 * @return inventory item list.
	 */
	public List<Item> getInventoryItems() {
		return inventoryItemList;
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
	 * @return logger.
	 */
	public String getUser() {
		return proxy.getUser();
	}
	
}
