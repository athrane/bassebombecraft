package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startServerSession;

import org.apache.logging.log4j.Logger;

import bassebombecraft.config.VersionUtils;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ServerSideCharmedMobsRepository;
import bassebombecraft.event.duration.DefaultDurationRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.frequency.DefaultFrequencyRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.network.NetworkChannelHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;

/**
 * Implementation of the {@linkplain Proxy} interface for the physical server.
 * Physical side is determined by {@linkplain Dist}.
 */
public class ServerProxy implements Proxy {

	/**
	 * Frequency repository.
	 */
	FrequencyRepository frequencyRepository;

	/**
	 * Duration repository.
	 */
	DurationRepository durationRepository;

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
	public ServerProxy() {

		// initialise frequency repository
		frequencyRepository = DefaultFrequencyRepository.getInstance();

		// initialise duration repository
		durationRepository = DefaultDurationRepository.getInstance();

		// Initialise charmed mobs repository
		charmedMobsRepository = ServerSideCharmedMobsRepository.getInstance();

		// initialize network
		networkHelper = new NetworkChannelHelper();
	}

	@Override
	public void startAnalyticsSession() {
		try {
			MinecraftServer server = getBassebombeCraft().getServer();

			// define server host
			String host = server.getServerHostname();
			if ((host == null) || (host.isEmpty()))
				host = "*";

			// define server port
			String port = Integer.toString(server.getServerPort());

			// define server owner
			String owner = server.getServerOwner();
			if ((owner == null) || (owner.isEmpty()))
				owner = "N/A";

			String name = new StringBuilder().append(host).append(":").append(port).append(";").append(owner)
					.append(";").append(server.getMOTD()).toString();

			startServerSession(name);
			
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void endAnalyticsSession() {
		try {
			MinecraftServer server = getBassebombeCraft().getServer();
			String hostname = server.getServerHostname();
			endServerSession(hostname);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Ending usage session failed with: " + ex.getMessage());
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
	public String getUser() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by physical client.");
	}

	@Override
	public void setupClientSideRendering() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by physical client.");
	}

	@Override
	public NetworkChannelHelper getNetworkChannel(World world) throws UnsupportedOperationException {
		// NB: World isn't used by server proxy.
		return networkHelper;
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
		throw new UnsupportedOperationException("Operation not supported by physical client.");
	}

	@Override
	public CharmedMobsRepository getCharmedMobsRepository(World world) throws UnsupportedOperationException {
		// NB: World isn't used by server proxy.		
		return charmedMobsRepository;
	}

}
