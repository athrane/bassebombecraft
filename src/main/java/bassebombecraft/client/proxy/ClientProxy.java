package bassebombecraft.client.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.client.player.ClientPlayerUtils.getClientSidePlayerUId;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.world.WorldUtils.isLogicalServer;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.Logger;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.client.event.particle.DefaultParticleRenderingRepository;
import bassebombecraft.client.event.particle.ParticleRenderingRepository;
import bassebombecraft.client.event.rendering.CharmedInfoRenderer;
import bassebombecraft.client.event.rendering.DecoyRenderer;
import bassebombecraft.client.event.rendering.DecreaseSizeEffectRenderer;
import bassebombecraft.client.event.rendering.IncreaseSizeEffectRenderer;
import bassebombecraft.client.event.rendering.RenderingEventHandler;
import bassebombecraft.client.event.rendering.RespawnedRenderer;
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
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.proxy.Proxy;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;

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
	 * Particle rendering repository.
	 */
	ParticleRenderingRepository particleRepository;

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

		// Initialise particle rendering repository
		particleRepository = DefaultParticleRenderingRepository.getInstance();

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
		// register debug renderer classes
		// EVENT_BUS.addListener(DebugRenderer_MobLines::render);
		// EVENT_BUS.addListener(DebugRenderer_EntityText_v3::render);
		// MinecraftForge.EVENT_BUS.addListener(DebugRenderer_WorldLastEventText::render);
		// EVENT_BUS.addListener(DebugRenderer_Highlightblock::render);
		// EVENT_BUS.addListener(DebugRenderer_StrangeSize::render);
		// EVENT_BUS.addListener(DebugRenderer_2DEntities::renderPre);
		// EVENT_BUS.addListener(DebugRenderer_2DEntities::renderPost);

		// register renderer classes
		EVENT_BUS.addListener(RenderingEventHandler::handleRenderGameOverlayEvent);
		EVENT_BUS.addListener(RenderingEventHandler::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(RenderingEventHandler::handleHighlightBlock);
		// EVENT_BUS.addListener(TeamInfoRenderer::handleRenderWorldLastEvent);
		// EVENT_BUS.addListener(TargetInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(CharmedInfoRenderer::handleRenderWorldLastEvent);
		// EVENT_BUS.addListener(TeamEnityRenderer::handleRenderLivingEvent);
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(IncreaseSizeEffectRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(DecoyRenderer::handleRenderLivingEventPost);
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPre);
		EVENT_BUS.addListener(RespawnedRenderer::handleRenderLivingEventPost);
	}

	@Override
	public NetworkChannelHelper getNetworkChannel(World world) throws UnsupportedOperationException {
		if (isLogicalServer(world))
			return networkHelper;

		// throw exception if helper is used by physical client w/ logical client.
		throw new UnsupportedOperationException("Operation not supported by physical client w/ logical client.");
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
	public DurationRepository getServerDurationRepository() throws UnsupportedOperationException {
		return clientDurationRepository;
	}

	@Override
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException {
		return serverDurationRepository;
	}

	@Override
	public ParticleRenderingRepository getClientParticleRenderingRepository() throws UnsupportedOperationException {
		return particleRepository;
	}

	@Override
	public CharmedMobsRepository getServerCharmedMobsRepository() throws UnsupportedOperationException {
		return serverCharmedMobsRepository;
	}

	@Override
	public CharmedMobsRepository getClientCharmedMobsRepository() throws UnsupportedOperationException {
		return clientCharmedMobsRepository;
	}

	@Override
	public BlockDirectivesRepository getServerBlockDirectivesRepository() throws UnsupportedOperationException {
		return blockDirectivesRepository;
	}

	@Override
	public TemporaryBlockRepository getServerTemporaryBlockRepository() throws UnsupportedOperationException {
		return tempBlockRepository;
	}

	@Override
	public MobCommanderRepository getServerMobCommanderRepository() throws UnsupportedOperationException {
		return mobCommanderRepository;
	}

	@Override
	public TeamRepository getServerTeamRepository() throws UnsupportedOperationException {
		return teamRepository;
	}

	@Override
	public TargetRepository getServerTargetRepository() throws UnsupportedOperationException {
		return targetRepository;
	}

}
