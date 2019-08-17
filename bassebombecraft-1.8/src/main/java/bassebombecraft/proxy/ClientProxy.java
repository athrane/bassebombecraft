package bassebombecraft.proxy;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.player.PlayerUtils.getClientSidePlayerUId;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;

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

	/**
	 * Get player UID at client side.
	 * 
	 * @return player UID.
	 */
	@Override
	public String getUser() {
		return getClientSidePlayerUId();
	}

	@Override
	public void registerItemForRendering(Block block, String blockName) {
		ItemModelMesher mesher = Minecraft.getInstance().getRenderItem().getItemModelMesher();
		ModelResourceLocation location = new ModelResourceLocation(MODID + ":" + blockName, "inventory");
		mesher.register(Item.getItemFromBlock(block), META, location);
	}

}
