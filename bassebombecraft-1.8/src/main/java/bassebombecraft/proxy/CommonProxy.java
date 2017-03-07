package bassebombecraft.proxy;

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
	public void registerForRendering(Item item) {
		// NO-OP
	}

}
