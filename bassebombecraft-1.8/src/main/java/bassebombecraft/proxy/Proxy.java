package bassebombecraft.proxy;

import javax.naming.OperationNotSupportedException;

import net.minecraft.block.Block;

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
	 * @param user user who used item.
	 */
	public void postItemUsage(String itemName, String user);
	
	/**
	 * Get user.
	 * 
	 * @return mod user.
	 * 
	 * @throws OperationNotSupportedException if operation isn't supported.
	 */
	public String getUser() throws OperationNotSupportedException;

	/**
	 * Register block for rendering (at client side).
	 * 
	 * @param block     block to be registered.
	 * @param blockName block name.
	 * 
	 * @throws OperationNotSupportedException if operation isn't supported.
	 */
	public void registerItemForRendering(Block block, String blockName) throws OperationNotSupportedException;
	
}
