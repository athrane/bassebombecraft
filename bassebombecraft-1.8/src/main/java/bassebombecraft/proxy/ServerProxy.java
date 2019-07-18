package bassebombecraft.proxy;

import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.startServerSession;

import javax.naming.OperationNotSupportedException;

import org.apache.logging.log4j.Logger;

import static bassebombecraft.BassebombeCraft.*;

import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;

/**
 * Implementation of the {@linkplain Proxy} interface.
 * 
 * Forge client side proxy implementation.
 */
public class ServerProxy implements Proxy {

	@Override
	public void startAnalyticsSession() {
		try {
			MinecraftServer server = getBassebombeCraft().getServer();
			String hostname = server.getServerHostname();
			startServerSession(hostname);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void endAnalyticsSession() {
		try {
			MinecraftServer server = getBassebombeCraft().getServer();
			String hostname = server.getServerHostname();
			endServerSession(hostname);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Ending usage session failed with: " + ex.getMessage());
		}		
	}
		
	@Override
	public String getUser() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Only invoke this method client side.");
	}

	@Override
	public void registerItemForRendering(Block block, String blockName)throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Only invoke this method client side.");
	}
	
}
