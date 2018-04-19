package bassebombecraft.item;

import bassebombecraft.ModConstants;
import bassebombecraft.item.book.TeleportBook;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Event handler for registration of items.
 */
@Mod.EventBusSubscriber
@ObjectHolder(ModConstants.MODID)
public class ItemRegistryEventHandler {

	@ObjectHolder("TeleportBook")
	public static Item teleportBook = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(new TeleportBook());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		registerRender(teleportBook);
	}	
	
	/**
	 * Register model for item.
	 * 
	 * @param item for which the model is registered.
	 */
	static void registerRender(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}	
}
