package bassebombecraft.proxy;

import javax.naming.OperationNotSupportedException;

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
	 * @throws OperationNotSupportedException if operation isn't supported.
	 */
	public String getUser() throws OperationNotSupportedException;

	/**
	 * Setup client side rendering.
	 * 
	 * @throws OperationNotSupportedException if operation isn't supported.
	 */
	public void setupClientSideRendering() throws OperationNotSupportedException;

	/**
	 * Get networking channel helper.
	 * 
	 * This helper is available only on the SERVER side. That is, only the
	 * server side proxy implementation support this method.
	 * 
	 * @return network channel helper
	 * 
	 * @throws OperationNotSupportedException if invoked on client side.
	 */
	public NetworkChannelHelper getNetworkChannel() throws OperationNotSupportedException;
	
	/**
	 * Get frequency repository.
	 * 
	 * This repository is available on both CLIENT and SERVER side. That is, both
	 * proxy implementations support this method.
	 * 
	 * @return frequency repository
	 * 
	 * @throws OperationNotSupportedException this exception is never thrown.
	 */
	public FrequencyRepository getFrequencyRepository() throws OperationNotSupportedException;

	/**
	 * Get duration repository.
	 * 
	 * This repository is available only on the SERVER side. That is, only the
	 * server side proxy implementation support this method.
	 * 
	 * @return duration repository
	 * 
	 * @throws OperationNotSupportedException if invoked on client side.
	 */
	public DurationRepository getDurationRepository() throws OperationNotSupportedException;

	/**
	 * Particle rendering repository.
	 * 
	 * This repository is available only on the CLIENT side. That is, only the
	 * client side proxy implementation support this method.
	 * 
	 * @return particle rendering repository.
	 * 
	 * @throws OperationNotSupportedException if invoked on server side.
	 */
	public ParticleRenderingRepository getParticleRenderingRepository() throws OperationNotSupportedException;

}
