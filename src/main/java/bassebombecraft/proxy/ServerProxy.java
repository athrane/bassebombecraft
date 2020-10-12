package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startServerSession;
import static bassebombecraft.inventory.container.RegisteredContainers.CONTAINER_REGISTRY;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

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
import bassebombecraft.event.job.DefaultJobReposiory;
import bassebombecraft.event.job.JobRepository;
import bassebombecraft.network.NetworkChannelHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;

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
	 * Job repository.
	 */
	JobRepository jobRepository;
	
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

		// initialise job repository
		jobRepository = DefaultJobReposiory.getInstance();
		
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
			
			// get server
			Optional<MinecraftServer> optServer = getBassebombeCraft().getServer();
			MinecraftServer server = optServer.get(); 
					
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
			// get server
			Optional<MinecraftServer> optServer = getBassebombeCraft().getServer();
			MinecraftServer server = optServer.get(); 
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
		return "N/A(user not available in server)";
	}

	@Override
	public void setupClientSideRendering() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public NetworkChannelHelper getNetworkChannel() {
		return networkHelper;
	}

	@Override
	public FrequencyRepository getServerFrequencyRepository() {
		return frequencyRepository;
	}

	@Override
	public FrequencyRepository getClientFrequencyRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public DurationRepository getServerDurationRepository() {
		return durationRepository;
	}

	@Override
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}
	
	@Override
	public JobRepository getServerJobRepository() {
		return jobRepository;
	}

	@Override
	public ParticleRenderingRepository getClientParticleRenderingRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
	}

	@Override
	public CharmedMobsRepository getServerCharmedMobsRepository() {
		return charmedMobsRepository;
	}

	@Override
	public CharmedMobsRepository getClientCharmedMobsRepository() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by server.");
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

	@Override
	public void doDeferredRegistration(IEventBus modEventBus) {
		
		// register containers
		CONTAINER_REGISTRY.register(modEventBus);
	}
	
}
