package bassebombecraft.client.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayerUId;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.entity.RegisteredEntities.CIRCLE_PROJECTILE;
import static bassebombecraft.entity.RegisteredEntities.EGG_PROJECTILE;
import static bassebombecraft.entity.RegisteredEntities.LIGHTNING_PROJECTILE;
import static bassebombecraft.entity.RegisteredEntities.LLAMA_PROJECTILE;
import static bassebombecraft.entity.RegisteredEntities.SKULL_PROJECTILE;
import static bassebombecraft.inventory.container.RegisteredContainers.COMPOSITE_ITEM_COMTAINER;
import static net.minecraft.client.gui.ScreenManager.registerFactory;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.client.event.rendering.BuildMineBookRenderer;
import bassebombecraft.client.event.rendering.CompositeMagicItemRenderer;
import bassebombecraft.client.event.rendering.DecoyRenderer;
import bassebombecraft.client.event.rendering.DecreaseSizeEffectRenderer;
import bassebombecraft.client.event.rendering.GenericCompositeItemsBookRenderer;
import bassebombecraft.client.event.rendering.HudItemCharmedInfoRenderer;
import bassebombecraft.client.event.rendering.HudItemHighlightedBlockRenderer;
import bassebombecraft.client.event.rendering.IncreaseSizeEffectRenderer;
import bassebombecraft.client.event.rendering.RespawnedRenderer;
import bassebombecraft.client.event.rendering.effect.ClientGraphicalEffectRepository;
import bassebombecraft.client.event.rendering.effect.EffectRenderer;
import bassebombecraft.client.event.rendering.effect.GraphicalEffectRepository;
import bassebombecraft.client.event.rendering.particle.DefaultParticleRenderingRepository;
import bassebombecraft.client.event.rendering.particle.ParticleRenderer;
import bassebombecraft.client.event.rendering.particle.ParticleRenderingRepository;
import bassebombecraft.client.rendering.entity.CircleProjectileEntityRenderer;
import bassebombecraft.client.rendering.entity.EggProjectileEntityRenderer;
import bassebombecraft.client.rendering.entity.LightningProjectileEntityRenderer;
import bassebombecraft.client.rendering.entity.LlamaProjectileEntityRenderer;
import bassebombecraft.client.rendering.entity.SkullProjectileEntityRenderer;
import bassebombecraft.client.screen.CompositeMagicItemScreen;
import bassebombecraft.config.VersionUtils;
import bassebombecraft.entity.commander.DefaultMobCommanderRepository;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.block.DefaultBlockDirectiveRepository;
import bassebombecraft.event.block.temporary.DefaultTemporaryBlockRepository;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.event.duration.DefaultDurationRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.entity.target.DefaultTargetRepository;
import bassebombecraft.event.entity.target.TargetRepository;
import bassebombecraft.event.entity.team.DefaultTeamRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.frequency.DefaultFrequencyRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.job.DefaultJobReposiory;
import bassebombecraft.event.job.JobRepository;
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.proxy.Proxy;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;

/**
 * Implementation of the {@linkplain Proxy} interface for the physical client.
 * Physical side is determined by {@linkplain Dist}.
 */
public class ClientProxy implements Proxy {
	
	/**
	 * Meta data for block.
	 */
	static final int META = 0;

	/**
	 * Client frequency repository.
	 */
	FrequencyRepository clientFrequencyRepository;

	/**
	 * Server frequency repository.
	 */
	FrequencyRepository serverFrequencyRepository;

	/**
	 * Client duration repository.
	 */
	DurationRepository clientDurationRepository;

	/**
	 * Server duration repository.
	 */
	DurationRepository serverDurationRepository;

	/**
	 * Job repository.
	 */
	JobRepository jobRepository;

	/**
	 * Particle rendering repository.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Graphical effect rendering repository.
	 */
	GraphicalEffectRepository graphicalEffectRepository;

	/**
	 * Charmed Mob repository
	 */
	CharmedMobsRepository clientCharmedMobsRepository;

	/**
	 * Charmed Mob repository
	 */
	CharmedMobsRepository serverCharmedMobsRepository;

	/**
	 * Block directives repository.
	 */
	BlockDirectivesRepository blockDirectivesRepository;

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
	 * Target repository.
	 */
	TargetRepository targetRepository;

	/**
	 * Network helper.
	 */
	NetworkChannelHelper networkHelper;

	/**
	 * Constructor
	 */
	public ClientProxy() {

		// initialise frequency repositories
		clientFrequencyRepository = DefaultFrequencyRepository.getInstance();
		serverFrequencyRepository = DefaultFrequencyRepository.getInstance();

		// initialise duration repositories
		clientDurationRepository = DefaultDurationRepository.getInstance();
		serverDurationRepository = DefaultDurationRepository.getInstance();

		// initialise job repository
		jobRepository = DefaultJobReposiory.getInstance();

		// Initialise particle rendering repository
		particleRepository = DefaultParticleRenderingRepository.getInstance();

		// initialise graphicsl effect repository
		graphicalEffectRepository = ClientGraphicalEffectRepository.getInstance();

		// Initialise charmed mobs repositories
		clientCharmedMobsRepository = ClientCharmedMobsRepository.getInstance();
		serverCharmedMobsRepository = ServerCharmedMobsRepository.getInstance();

		// Initialise directives repository
		blockDirectivesRepository = DefaultBlockDirectiveRepository.getInstance();

		// Initialise temporary block repository
		tempBlockRepository = DefaultTemporaryBlockRepository.getInstance();

		// Initialise mob commander repository
		mobCommanderRepository = DefaultMobCommanderRepository.getInstance();

		// initialise team repository
		teamRepository = DefaultTeamRepository.getInstance();

		// initialise targeted entities repository
		targetRepository = DefaultTargetRepository.getInstance();

		// initialize network
		networkHelper = new NetworkChannelHelper();
	}

	@Override
	public void startAnalyticsSession() {
		try {
			startSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void endAnalyticsSession() {
		try {
			endSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postItemUsage(String itemName, String user) {
		try {
			postItemUsageEvent(user, itemName);

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting usage failed with: " + ex);

			// get stack trace for positing exception
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("Stack trace for posting exception:" + sw);
		}
	}

	@Override
	public void postException(Throwable e) {
		try {
			VersionUtils.postException(getUser(), e);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();

			logger.error("Posting exception:" + e + " failed with: " + ex);

			// get stack trace for original exception
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			logger.error("Stack trace for original exception:" + sw);

			// get stack trace for positing exception
			sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("Stack trace for posting exception:" + sw);
		}
	}

	@Override
	public void postError(String msg) {
		try {
			VersionUtils.postError(getUser(), msg);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting error:" + msg + " failed with: " + ex);

			// get stack trace for positing exception
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("Stack trace for posting exception:" + sw);

		}
	}

	@Override
	public void postAiObservation(String type, String observation) {
		try {
			VersionUtils.postAiObservation(getUser(), type, observation);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting AI observation: failed with: " + ex);

			// get stack trace for positing exception
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			logger.error("Stack trace for posting exception:" + sw);
		}
	}

	@Override
	public String getUser() {
		return getClientSidePlayerUId();
	}

	@Override
	public void setupClientSideRendering() throws UnsupportedOperationException {

		// register renderer classes for items
		// EVENT_BUS.addListener(TeamInfoRenderer::handleRenderWorldLastEvent);
		// EVENT_BUS.addListener(TargetInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(HudItemCharmedInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(HudItemHighlightedBlockRenderer::handleHighlightBlockEvent);
		EVENT_BUS.addListener(BuildMineBookRenderer::handleHighlightBlockEvent);
		EVENT_BUS.addListener(CompositeMagicItemRenderer::handleRenderGameOverlayEvent);
		EVENT_BUS.addListener(GenericCompositeItemsBookRenderer::handleRenderGameOverlayEvent);		
		// EVENT_BUS.addListener(TeamEnityRenderer::handleRenderLivingEvent);

		// register renderer classes for effects
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(ParticleRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(EffectRenderer::handleRenderWorldLastEvent);

		// register entity rendering
		EntityRenderers.register(EGG_PROJECTILE.get(), EggProjectileEntityRenderer::new);
		EntityRenderers.register(LLAMA_PROJECTILE.get(), LlamaProjectileEntityRenderer::new);
		EntityRenderers.register(LIGHTNING_PROJECTILE.get(), LightningProjectileEntityRenderer::new);
		EntityRenderers.register(CIRCLE_PROJECTILE.get(), CircleProjectileEntityRenderer::new);
		EntityRenderers.register(SKULL_PROJECTILE.get(), SkullProjectileEntityRenderer::new);

		// register the factory used client side to generate a screen corresponding to
		// the container
		register(COMPOSITE_ITEM_COMTAINER.get(), CompositeMagicItemScreen::new);
	}

	@Override
	public NetworkChannelHelper getNetworkChannel() {
		return networkHelper;
	}

	@Override
	public FrequencyRepository getServerFrequencyRepository() throws UnsupportedOperationException {
		return serverFrequencyRepository;
	}

	@Override
	public FrequencyRepository getClientFrequencyRepository() throws UnsupportedOperationException {
		return clientFrequencyRepository;
	}

	@Override
	public DurationRepository getServerDurationRepository() {
		return serverDurationRepository;
	}

	@Override
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException {
		return clientDurationRepository;
	}

	@Override
	public JobRepository getServerJobRepository() {
		return jobRepository;
	}

	@Override
	public ParticleRenderingRepository getClientParticleRenderingRepository() throws UnsupportedOperationException {
		return particleRepository;
	}

	@Override
	public GraphicalEffectRepository getClientGraphicalEffectRepository() throws UnsupportedOperationException {
		return graphicalEffectRepository;
	}

	@Override
	public CharmedMobsRepository getServerCharmedMobsRepository() {
		return serverCharmedMobsRepository;
	}

	@Override
	public CharmedMobsRepository getClientCharmedMobsRepository() throws UnsupportedOperationException {
		return clientCharmedMobsRepository;
	}

	@Override
	public BlockDirectivesRepository getServerBlockDirectivesRepository() {
		return blockDirectivesRepository;
	}

	@Override
	public TemporaryBlockRepository getServerTemporaryBlockRepository() {
		return tempBlockRepository;
	}

	@Override
	public MobCommanderRepository getServerMobCommanderRepository() {
		return mobCommanderRepository;
	}

	@Override
	public TeamRepository getServerTeamRepository() {
		return teamRepository;
	}

	@Override
	public TargetRepository getServerTargetRepository() {
		return targetRepository;
	}

}
