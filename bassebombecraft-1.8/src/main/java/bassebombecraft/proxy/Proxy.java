package bassebombecraft.proxy;

import javax.naming.OperationNotSupportedException;

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
	public void postException(Exception e);
	
	/**
	 * Get user.
	 * 
	 * @return mod user.
	 * 
	 * @throws OperationNotSupportedException if operation isn't supported.
	 */
	public String getUser() throws OperationNotSupportedException;

}
