package bassebombecraft.proxy;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ClientSideCharmedMobsRepository;
import bassebombecraft.event.charm.ServerSideCharmedMobsRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRenderingRepository;
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
	 * Get frequency repository.
	 * 
	 * This repository is available at both the logical CLIENT and SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return frequency repository
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public FrequencyRepository getFrequencyRepository() throws UnsupportedOperationException;

	/**
	 * Get duration repository.
	 * 
	 * This repository is available at both the logical CLIENT and SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * @return duration repository
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public DurationRepository getDurationRepository() throws UnsupportedOperationException;

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
	 * This repository is available at both the logical CLIENT and SERVER.
	 * 
	 * The server proxy implementation support this method to support the
	 * configuration: physical server w/ logical server (dedicated server).
	 * 
	 * The server proxy will return {@linkplain ServerSideCharmedMobsRepository}.
	 * 
	 * The client proxy implementation support this method to support the
	 * configurations: 1) physical client w/ logical server (integrated server). 2)
	 * physical client w/ logical client.
	 * 
	 * The client proxy will return {@linkplain ServerSideCharmedMobsRepository} for
	 * configuration 1). And return {@linkplain ClientSideCharmedMobsRepository} for
	 * configuration 2).
	 * 
	 * @param world world for resolution of repository implementation.
	 * 
	 * @return charmed mobs repository.
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public CharmedMobsRepository getCharmedMobsRepository(World world) throws UnsupportedOperationException;

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

	
}
