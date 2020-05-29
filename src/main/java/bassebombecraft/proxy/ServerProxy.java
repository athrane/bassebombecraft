package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startServerSession;

import org.apache.logging.log4j.Logger;

import bassebombecraft.client.event.particle.ParticleRenderingRepository;
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
	 * Targeted entities repository.
	 */
	TargetRepository targetRepository;
	
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
		charmedMobsRepository = ServerCharmedMobsRepository.getInstance();

		// Initialise directives repository
		blockDirectivesRepository = DefaultBlockDirectiveRepository.getInstance();

		// Initialise temporary block repository
		tempBlockRepository = DefaultTemporaryBlockRepository.getInstance();

		// Initialise mob commander repository
		mobCommanderRepository = DefaultMobCommanderRepository.getInstance();

		// initialise team repository
		teamRepository = DefaultTeamRepository.getInstance();

		// initialise target repository
		targetRepository = DefaultTargetRepository.getInstance();
		
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
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public void setupClientSideRendering() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public NetworkChannelHelper getNetworkChannel(World world) throws UnsupportedOperationException {
		return networkHelper;
	}
	
	@Override
	public FrequencyRepository getServerFrequencyRepository() throws UnsupportedOperationException {
		return frequencyRepository;
	}

	@Override
	public FrequencyRepository getClientFrequencyRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}
	
	@Override
	public DurationRepository getServerDurationRepository() throws UnsupportedOperationException {
		return durationRepository;
	}

	@Override
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public ParticleRenderingRepository getClientParticleRenderingRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}
	
	@Override
	public CharmedMobsRepository getServerCharmedMobsRepository() throws UnsupportedOperationException {
		return charmedMobsRepository;
	}

	@Override
	public CharmedMobsRepository getClientCharmedMobsRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
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
