package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.player.PlayerUtils.getClientSidePlayerUId;
import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import org.apache.logging.log4j.Logger;

import bassebombecraft.config.VersionUtils;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ClientSideCharmedMobsRepository;
import bassebombecraft.event.duration.DefaultDurationRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.frequency.DefaultFrequencyRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.DefaultParticleRenderingRepository;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.event.rendering.CharmedInfoRenderer;
import bassebombecraft.event.rendering.DecoyRenderer;
import bassebombecraft.event.rendering.DecreaseSizeEffectRenderer;
import bassebombecraft.event.rendering.IncreaseSizeEffectRenderer;
import bassebombecraft.event.rendering.RespawnedRenderer;
import bassebombecraft.event.rendering.TargetInfoRenderer;
import bassebombecraft.event.rendering.TeamEnityRenderer;
import bassebombecraft.event.rendering.TeamInfoRenderer;
import bassebombecraft.network.NetworkChannelHelper;
import bassebombecraft.world.WorldUtils;
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
	 * Frequency repository.
	 */
	FrequencyRepository frequencyRepository;

	/**
	 * Duration repository.
	 */
	DurationRepository durationRepository;

	/**
	 * Particle rendering repository.
	 */
	ParticleRenderingRepository particleRepository;

	/**
	 * Charmed Mob repository
	 */
	CharmedMobsRepository charmedMobsRepository;

	/**
	 * Network helper.
	 */
	NetworkChannelHelper networkHelper;

	/**
	 * Constructor
	 */
	public ClientProxy() {

		// initialise frequency repository
		frequencyRepository = DefaultFrequencyRepository.getInstance();

		// initialise duration repository
		durationRepository = DefaultDurationRepository.getInstance();

		// Initialise particle rendering repository
		particleRepository = DefaultParticleRenderingRepository.getInstance();

		// Initialise charmed mobs repository
		charmedMobsRepository = ClientSideCharmedMobsRepository.getInstance();

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
			logger.error("Posting usage failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postException(Throwable e) {
		try {
			VersionUtils.postException(getUser(), e);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting exception:" + e.getMessage() + " failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postError(String msg) {
		try {
			VersionUtils.postError(getUser(), msg);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting error:" + msg + " failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postAiObservation(String type, String observation) {
		try {
			VersionUtils.postAiObservation(getUser(), type, observation);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting AI observation: failed with: " + ex.getMessage());
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
		EVENT_BUS.addListener(TeamInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(TargetInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(CharmedInfoRenderer::handleRenderWorldLastEvent);
		EVENT_BUS.addListener(TeamEnityRenderer::handleRenderLivingEvent);
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
		if (WorldUtils.isLogicalServer(world)) return networkHelper;

		// throw exception if helper is used by physical client w/ logical client.		
		throw new UnsupportedOperationException("Operation not supported by physical client w/ logical client.");
	}

	@Override
	public FrequencyRepository getFrequencyRepository() throws UnsupportedOperationException {
		return frequencyRepository;
	}

	@Override
	public DurationRepository getDurationRepository() throws UnsupportedOperationException {
		return durationRepository;
	}

	@Override
	public ParticleRenderingRepository getParticleRenderingRepository() throws UnsupportedOperationException {
		return particleRepository;
	}

	@Override
	public CharmedMobsRepository getCharmedMobsRepository() throws UnsupportedOperationException {
		return charmedMobsRepository;
	}

}
