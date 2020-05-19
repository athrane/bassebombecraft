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
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.network.NetworkChannelHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;

/**
 * Interface for mod proxy.
 * 
 * Used to separate code that need to run either on the physical server or
 * client. Physical side is determined by {@linkplain Dist}.
 */
public interface Proxy {

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
	 * @return mod user.
	 * 
	 * @throws UnsupportedOperationException if operation isn't supported.
	 */
	public String getUser() throws UnsupportedOperationException;

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
	 * @param world world for verification of legal configuration.
	 * 
	 * @return network channel helper
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public NetworkChannelHelper getNetworkChannel(World world) throws UnsupportedOperationException;

	/**
	 * Get frequency repository for the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configuration: physical client w/ logical server (integrated server).
	 * 
	 * @return frequency repository for logical client.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public FrequencyRepository getServerFrequencyRepository() throws UnsupportedOperationException;
	
	/**
	 * Get frequency repository for the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return frequency repository for logical client.
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
	 * @return frequency repository for logical client.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public DurationRepository getServerDurationRepository() throws UnsupportedOperationException;
	
	/**
	 * Get duration repository for the logical CLIENT.
	 * 
	 * The server proxy implementation doesn't support this method.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return frequency repository for logical client.
	 * 
	 * @throws UnsupportedOperationException if invoked on server proxy.
	 */
	public DurationRepository getClientDurationRepository() throws UnsupportedOperationException;
	
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
	public ParticleRenderingRepository getParticleRenderingRepository() throws UnsupportedOperationException;

	/**
	 * Get charmed mobs repository.
	 * 
	 * This repository is available at the logical SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 
	 * 
	 * Both proxies will return {@linkplain ServerCharmedMobsRepository}.
	 * 
	 * @param world world for resolution of repository implementation.
	 * 
	 * @return charmed mobs repository.
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public CharmedMobsRepository getServerCharmedMobsRepository() throws UnsupportedOperationException;

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
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public BlockDirectivesRepository getBlockDirectivesRepository(World world) throws UnsupportedOperationException;

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
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public TemporaryBlockRepository getTemporaryBlockRepository(World world) throws UnsupportedOperationException;

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
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public MobCommanderRepository getMobCommanderRepository(World world) throws UnsupportedOperationException;

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
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public TeamRepository getTeamRepository(World world) throws UnsupportedOperationException;

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
	 * 
	 * @throws UnsupportedOperationException if invoked in configuration: physical
	 *                                       client w/ logical client.
	 */
	public TargetRepository getTargetRepository(World world) throws UnsupportedOperationException;

}
