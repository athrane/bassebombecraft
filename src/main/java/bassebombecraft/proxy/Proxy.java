package bassebombecraft.proxy;

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.client.event.particle.ParticleRenderingRepository;
import bassebombecraft.entity.commander.MobCommanderRepository;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.entity.target.TargetRepository;
import bassebombecraft.event.entity.team.TeamRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.job.JobRepository;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.network.NetworkChannelHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Interface for mod proxy.
 * 
 * Used to separate code that need to run either on the physical server or
 * client. Physical side is determined by {@linkplain Dist}.
 */
public interface Proxy {

	/**
	 * Perform deferred registration.
	 */
	public void doDeferredRegistration(IEventBus modEventBus);

	/**
	 * Initialize analytics.
	 */
	public void startAnalyticsSession();

	/**
	 * Shutdown analytics.
	 */
	public void endAnalyticsSession();

	/**
	 * Post item usage.
	 * 
	 * @param itemName used item.
	 * @param user     user who used item.
	 */
	public void postItemUsage(String itemName, String user);

	/**
	 * Report exception.
	 * 
	 * @param e exception to report
	 */
	public void postException(Throwable e);

	/**
	 * Report error.
	 * 
	 * @param msg error to report
	 */
	public void postError(String msg);

	/**
	 * Post AI observation .
	 * 
	 * @param type        observation type.
	 * @param observation observation data.
	 */
	public void postAiObservation(String type, String observation);

	/**
	 * Get user.
	 * 
	 * If method is invoked in the server proxy then "N/A(user not available in
	 * server)" is returned.
	 * 
	 * @return mod user.
	 */
	public String getUser();

	/**
	 * Setup client side rendering.
	 * 
	 * @throws UnsupportedOperationException if operation isn't supported.
	 */
	public void setupClientSideRendering() throws UnsupportedOperationException;

	/**
	 * Get networking channel helper.
	 * 
	 * This helper is available at the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return network channel helper
	 */
	public NetworkChannelHelper getNetworkChannel();

	/**
	 * Get frequency repository for the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return frequency repository.
	 */
	public FrequencyRepository getServerFrequencyRepository();

	/**
	 * Get frequency repository for the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return frequency repository.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public FrequencyRepository getClientFrequencyRepository() throws UnsupportedOperationException;

	/**
	 * Get duration repository for the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return duration repository.
	 */
	public DurationRepository getServerDurationRepository();

	/**
	 * Get duration repository for the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return duration repository.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException;

	/**
	 * Get job repository for the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return job repository.
	 */
	public JobRepository getServerJobRepository();

	/**
	 * Get particle rendering repository.
	 * 
	 * This repository is available at the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical client.
	 * 
	 * @return particle rendering repository.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public ParticleRenderingRepository getClientParticleRenderingRepository() throws UnsupportedOperationException;

	/**
	 * Get charmed mobs repository.
	 * 
	 * This repository is available at the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server). * The
	 * client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server).
	 * 
	 * Both proxies will return {@linkplain ServerCharmedMobsRepository}.
	 * 
	 * @return charmed mobs repository.
	 */
	public CharmedMobsRepository getServerCharmedMobsRepository();

	/**
	 * Get charmed mobs repository.
	 * 
	 * This repository is available at the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * The client proxy will return {@linkplain ClientCharmedMobsRepository}.
	 * 
	 * @param world world for resolution of repository implementation.
	 * 
	 * @return charmed mobs repository.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public CharmedMobsRepository getClientCharmedMobsRepository() throws UnsupportedOperationException;

	/**
	 * Get block directives repository.
	 * 
	 * This repository is available at the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return block directives repository.
	 */
	public BlockDirectivesRepository getServerBlockDirectivesRepository();

	/**
	 * Get temporary block directives repository.
	 * 
	 * This repository is available at the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return temporary block directives repository.
	 */
	public TemporaryBlockRepository getServerTemporaryBlockRepository();

	/**
	 * Get mob commander repository.
	 * 
	 * This repository is available at the logical SERVER. TODO: Rendering in
	 * {@linkplain HudItem} not supported.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return mob commander repository.
	 */
	public MobCommanderRepository getServerMobCommanderRepository();

	/**
	 * Get team repository.
	 * 
	 * This repository is available at the logical SERVER. TODO: Rendering in
	 * {@linkplain HudItem} not supported.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return mob commander repository.
	 */
	public TeamRepository getServerTeamRepository();

	/**
	 * Get target repository.
	 * 
	 * This repository is available at the logical SERVER. TODO: Rendering in
	 * {@linkplain HudItem} not supported.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return mob commander repository.
	 */
	public TargetRepository getServerTargetRepository();

}
