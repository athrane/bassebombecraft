package bassebombecraft.proxy;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Base class for proxies.
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		// NO-OP
	}

	public void init(FMLInitializationEvent e) {
		// NO-OP
	}

	public void postInit(FMLPostInitializationEvent e) {
		// NO-OP
	}

	/**
	 * Register item for rendering.
	 * 
	 * @param item
	 *            item to be registered.
	 */
	public void registerItemForRendering(Item item) {
		// NO-OP
	}

	/**
	 * Register block for rendering.
	 * 
	 * @param block
	 *            block to be registered.
	 * @param blockName
	 *            block name.
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
		// NO-OP
	}

	/**
	 * Shutdown analytics.
	 * 
	 * @param logger
	 */
	public void endAnalyticsSession(Logger logger) {
		// NO-OP
	}


	/**
	 * Post item usage.
	 * 
	 * @param itemName
	 *            item to register usage of.
	 */
	public void postItemUsage(String itemName) {
		// NO-OP		
	}

	
}
