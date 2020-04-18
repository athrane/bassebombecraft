package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.VersionUtils.endServerSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startServerSession;

import javax.naming.OperationNotSupportedException;

import org.apache.logging.log4j.Logger;

import bassebombecraft.config.VersionUtils;
import bassebombecraft.event.frequency.DefaultFrequencyRepository;
import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.server.MinecraftServer;

/**
 * Implementation of the {@linkplain Proxy} interface.
 * 
 * Forge client side proxy implementation.
 */
public class ServerProxy implements Proxy {

	/**
	 * Frequency repository.
	 */
	FrequencyRepository frequencyRepository;

	/**
	 * Constructor 
	 */
	public ServerProxy() {

		// initialise frequency repository
		frequencyRepository = DefaultFrequencyRepository.getInstance();
		
	}
	
	@Override
	public void startAnalyticsSession() {
		try {
			MinecraftServer server = getBassebombeCraft().getServer();

			// define server host
			String host = server.getServerHostname();
			if ((host == null) || (host.isEmpty()))
				host = "*";

			// define server port
			String port = Integer.toString(server.getServerPort());

			// define server owner
			String owner = server.getServerOwner();
			if ((owner == null) || (owner.isEmpty()))
				owner = "N/A";

			String name = new StringBuilder().append(host).append(":").append(port).append(";").append(owner)
					.append(";").append(server.getMOTD()).toString();

			startServerSession(name);
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
	public void postItemUsage(String itemName, String user) {
		try {
			postItemUsageEvent(user, itemName);

		} catch (Exception ex) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Posting usage failed with: " + ex.getMessage());
		}
	}

	@Override
	public void postException(Throwable e) {
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

	@Override
	public String getUser() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Only invoke this method client side.");
	}

	@Override
	public void setupClientSideRendering() throws OperationNotSupportedException {
		throw new OperationNotSupportedException("Only invoke this method client side.");
	}

	@Override
	public FrequencyRepository getFrequencyRepository() throws OperationNotSupportedException {
		return frequencyRepository;
	}
	
}
