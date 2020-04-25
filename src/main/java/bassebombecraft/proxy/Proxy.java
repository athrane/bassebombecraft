package bassebombecraft.proxy;

import bassebombecraft.event.charm.CharmedMobsRepository;
import bassebombecraft.event.charm.ClientSideCharmedMobsRepository;
import bassebombecraft.event.charm.ServerSideCharmedMobsRepository;
import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import bassebombecraft.event.particle.ParticleRenderingRepository;
import bassebombecraft.network.NetworkChannelHelper;

/**
 * Interface for mod proxy.
 * 
 * Used to separate code that need to run server side and client side.
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
	 * This helper is available only on the SERVER side. That is, only the server
	 * side proxy implementation support this method.
	 * 
	 * @return network channel helper
	 * 
	 * @throws UnsupportedOperationException if invoked on client side.
	 */
	public NetworkChannelHelper getNetworkChannel() throws UnsupportedOperationException;

	/**
	 * Get frequency repository.
	 * 
	 * This repository is available on both CLIENT and SERVER side. That is, both
	 * proxy implementations support this method.
	 * 
	 * @return frequency repository
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public FrequencyRepository getFrequencyRepository() throws UnsupportedOperationException;

	/**
	 * Get duration repository.
	 * 
	 * This repository is available only on the SERVER side. That is, only the
	 * server side proxy implementation support this method.
	 * 
	 * @return duration repository
	 * 
	 * @throws UnsupportedOperationException if invoked on client side.
	 */
	public DurationRepository getDurationRepository() throws UnsupportedOperationException;

	/**
	 * Get particle rendering repository.
	 * 
	 * This repository is available only on the CLIENT side. That is, only the
	 * client side proxy implementation support this method.
	 * 
	 * @return particle rendering repository.
	 * 
	 * @throws UnsupportedOperationException if invoked on server side.
	 */
	public ParticleRenderingRepository getParticleRenderingRepository() throws UnsupportedOperationException;

	/**
	 * Get charmed mobs repository.
	 * 
	 * This repository is available on both CLIENT and SERVER side. That is, both
	 * proxy implementations support this method. Each proxy will return a different
	 * implementation. The client side proxy will return
	 * {@linkplain ClientSideCharmedMobsRepository} and the server side proxy will
	 * return {@linkplain ServerSideCharmedMobsRepository}.
	 * 
	 * @return charmed mobs repository.
	 * 
	 * @throws UnsupportedOperationException this exception is never thrown.
	 */
	public CharmedMobsRepository getCharmedMobsRepository() throws UnsupportedOperationException;

}
