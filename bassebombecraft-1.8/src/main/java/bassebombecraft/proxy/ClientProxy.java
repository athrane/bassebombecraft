package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.*;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.player.PlayerUtils.getClientSidePlayerUId;

import org.apache.logging.log4j.Logger;

import bassebombecraft.config.VersionUtils;

/**
 * Implementation of the {@linkplain Proxy} interface.
 * 
 * Forge client side proxy implementation.
 */
public class ClientProxy implements Proxy {

	/**
	 * Meta data for block.
	 */
	static final int META = 0;

	@Override
	public void startAnalyticsSession() {
		try {
			startSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());			
		}
	}

	@Override
	public void endAnalyticsSession() {
		try {
			endSession(getUser());

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Initiating usage session failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postItemUsage(String itemName, String user) {
		try {
			postItemUsageEvent(itemName, user);

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting usage failed with: " + ex.getMessage());
		}
	}
	
	@Override
	public void postException(Exception e) {
		try {
			VersionUtils.postException(getUser(), e);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting exception:" + e.getMessage() + " failed with: " + ex.getMessage());
		}
	}
	
	@Override
	public void postAiObservation(String type, String observation) {
		try {
			VersionUtils.postAiObservation(getUser(), type, observation);
		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting AI observation: failed with: " + ex.getMessage());
		}		
	}

	/**
	 * Get player UID at client side.
	 * 
	 * @return player UID.
	 */
	@Override
	public String getUser() {
		return getClientSidePlayerUId();
	}

}
