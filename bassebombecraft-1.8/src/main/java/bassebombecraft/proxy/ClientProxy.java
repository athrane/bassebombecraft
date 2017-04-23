package bassebombecraft.proxy;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.config.VersionUtils.endSession;
import static bassebombecraft.config.VersionUtils.postItemUsageEvent;
import static bassebombecraft.config.VersionUtils.startSession;

import org.apache.logging.log4j.Logger;

import static bassebombecraft.player.PlayerUtils.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
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
	public void registerItemForRendering(Item item) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		ModelResourceLocation location;
		location = new ModelResourceLocation(MODID + ":" + item.getUnlocalizedName().substring(5), "inventory");
		renderItem.getItemModelMesher().register(item, 0, location);
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
	public void endAnalyticsSession(Logger logger) {

		try {
			endSession(getUser());

		} catch (Exception ex) {
			// NO-OP

			// logger.error("Usage initialization failed with: " +
			// ex.getMessage());
		}
	}

	@Override
	public void postItemUsage(String itemName) {

		try {
			postItemUsageEvent(getUser(), itemName);

		} catch (Exception ex) {
			// NO-OP
			// Logger logger = BassebombeCraft.getLogger();
			// logger.error("Usage logging failed with: " + ex.getMessage());
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
