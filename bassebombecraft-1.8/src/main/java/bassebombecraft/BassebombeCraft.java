package bassebombecraft;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.NAME;
import static bassebombecraft.ModConstants.TAB_NAME;
import static bassebombecraft.ModConstants.VERSION;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bassebombecraft.block.BlockInitializer;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.block.DefaultBlockDirectiveRepository;
import bassebombecraft.event.block.ProcessBlockDirectivesEventListener;
import bassebombecraft.event.block.temporary.DefaultTemporaryBlockRepository;
import bassebombecraft.event.block.temporary.TemporaryBlockEventListener;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.event.charm.CharmedMobEventListener;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.DefaultCharmedMobsRepository;
import bassebombecraft.event.particle.DefaultParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRenderingEventListener;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.item.ItemInitializer;
import bassebombecraft.projectile.ProjectileInitializer;
import bassebombecraft.server.CommonProxy;
import bassebombecraft.tab.CreativeTabFactory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@net.minecraftforge.fml.common.Mod(name = NAME, modid = MODID, version = VERSION)
public class BassebombeCraft {

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Mod singleton class. This is the object reference to your class that
	 * Forge uses. Make sure that the argument is the modid in @Mod. Otherwise,
	 * it'll default to the empty string, and cause problems with any mod that
	 * also does that.
	 */
	@Instance(value = MODID)
	public static BassebombeCraft instance;

	@SidedProxy(clientSide = "bassebombecraft.client.ClientProxy", serverSide = "bassebombecraft.server.CommonProxy")
	public static CommonProxy proxy;

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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		// Initialise charmed mobs repository
		charmedMobsRepository = DefaultCharmedMobsRepository.getInstance();

		// Initialise directives repository
		blockDirectivesRepository = DefaultBlockDirectiveRepository.getInstance();

		// Initialise temporary block repository
		tempBlockRepository = DefaultTemporaryBlockRepository.getInstance();

		// Initialise particle rendering repository
		particleRepository = DefaultParticleRenderingRepository.getInstance();

		initializeModMetadata(event);
		initializeCreativeTab();
		logger.info("Pre-initialized BassebombeCraft");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		logger.info("Staring to initialize BasseBombeCraft");

		ItemInitializer.getInstance().initialize(modTab);
		ProjectileInitializer.getInstance().initialize(this, modTab);
		BlockInitializer.getInstance().initialize(modTab);
		initializeEventListeners();
		logger.info("Initialized BasseBombeCraft");
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
		m.description = "A collection of 40+ magical books, idols and blocks for adventuring and construction. The magic needed by BasseBombe when he plays Minecraft.";
		m.authorList.add("einheriii@gmail.com");
		m.logoFile = "assets/bassebombecraft/logo/logo.png";
		m.url = "http://minecraft.curseforge.com/projects/bassebombecraft";
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
		FMLCommonHandler.instance().bus().register(mobsEventListener);

		// Initialise process block directives event listener
		ProcessBlockDirectivesEventListener directivesEventListener = new ProcessBlockDirectivesEventListener(
				blockDirectivesRepository, particleRepository);
		MinecraftForge.EVENT_BUS.register(directivesEventListener);
		FMLCommonHandler.instance().bus().register(directivesEventListener);

		// Initialise temporary block event listener
		TemporaryBlockEventListener tempBlockEventListener = new TemporaryBlockEventListener(tempBlockRepository,
				blockDirectivesRepository);
		MinecraftForge.EVENT_BUS.register(tempBlockEventListener);
		FMLCommonHandler.instance().bus().register(tempBlockEventListener);

		// Initialise particle rendering event listener
		ParticleRenderingEventListener particleEventListener = new ParticleRenderingEventListener(particleRepository);
		MinecraftForge.EVENT_BUS.register(particleEventListener);
		FMLCommonHandler.instance().bus().register(particleEventListener);

		proxy.registerRenderers();
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
	 * Get mod instance.
	 * 
	 * @return mod instance.
	 */
	public static BassebombeCraft getBassebombeCraft() {
		return instance;
	}

}
