package bassebombecraft.proxy;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;
import static bassebombecraft.player.PlayerUtils.getClientSidePlayerUId;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Client side proxy.
 */
public class ClientProxy extends CommonProxy {

	/**
	 * Meta data for block.
	 */
	static final int META = 0;

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void registerItemForRendering(Block block, String blockName) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation location = new ModelResourceLocation(MODID + ":" + blockName, "inventory");
		mesher.register(Item.getItemFromBlock(block), META, location);
	}

	@Override
	public void startAnalyticsSession(Logger logger) {

		try {
			startSession(getUser());

		} catch (Exception ex) {
			logger.error("Usage initialization failed with: " + ex.getMessage());
		}
	}

	@Override
	public void endAnalyticsSession() {

		try {
			endSession(getUser());

		} catch (Exception ex) {
			// NO-OP
		}
	}

	@Override
	public void postItemUsage(String itemName, String user) {

		try {
			postItemUsageEvent(user, itemName);

		} catch (Exception ex) {
			// NO-OP
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
