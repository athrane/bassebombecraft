package bassebombecraft.proxy;

import javax.naming.OperationNotSupportedException;

import bassebombecraft.event.frequency.FrequencyRepository;

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
	 * @param e exception for report
	 */
	public void postException(Throwable e);

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
	 * Get frequency repository.
	 * 
	 * This repository is available on both CLIENT and SERVER side. 
	 * That is, both proxy implementations support this method.
	 * 
	 * @return frequency repository
	 */	
	public FrequencyRepository getFrequencyRepository() throws OperationNotSupportedException;
	
}
