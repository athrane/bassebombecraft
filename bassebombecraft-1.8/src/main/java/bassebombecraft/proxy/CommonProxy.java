package bassebombecraft.proxy;

import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startServerSession;

import org.apache.logging.log4j.Logger;

import bassebombecraft.BassebombeCraft;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;

/**
 * Base class for proxies.
 */
public class CommonProxy {

	/**
	 * Register block for rendering.
	 * 
	 * @param block     block to be registered.
	 * @param blockName block name.
	 */
	public void registerItemForRendering(Block block, String blockName) {
		// NO-OP
	}

	/**
	 * Initialize analytics.
	 * 
	 * @param logger
	 */
	public void startAnalyticsSession(Logger logger) {

		try {
			MinecraftServer server = BassebombeCraft.getBassebombeCraft().getServer();
			String hostname = server.getServerHostname();
			startServerSession(hostname);
		} catch (Exception ex) {
			// NO-OP
		}
	}

	/**
	 * Shutdown analytics.
	 */
	public void endAnalyticsSession() {

		try {
			MinecraftServer server = BassebombeCraft.getBassebombeCraft().getServer();
			String hostname = server.getServerHostname();
			endServerSession(hostname);
		} catch (Exception ex) {
			// NO-OP
		}
	}

	/**
	 * Post item usage.
	 * 
	 * @param itemName item to register usage of.
	 * @param user     user using the item.
	 */
	public void postItemUsage(String itemName, String user) {

		try {
			postItemUsageEvent(user, itemName);

		} catch (Exception ex) {
			// NO-OP
		}
	}

	/**
	 * Get user.
	 * 
	 * @return mod user.
	 */
	public String getUser() {
		return "not defined";
	}
}
