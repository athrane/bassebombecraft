package bassebombecraft.item;

import bassebombecraft.ModConstants;
import bassebombecraft.item.book.LargeFireballBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.LingeringFlameBook;
import bassebombecraft.item.book.LingeringFuryBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.ToxicMistBook;
import bassebombecraft.item.book.WitherMistBook;
import bassebombecraft.item.book.WitherSkullBook;
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
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Event handler for registration of items.
 */
@Mod.EventBusSubscriber
@ObjectHolder(ModConstants.MODID)
public class ItemRegistryEventHandler {

	@ObjectHolder("TeleportBook")
	public static Item teleportBook = null;

	@ObjectHolder("SetSpawnPointBook")
	public static Item setSpawnPointBook = null;
	
	@ObjectHolder("SmallFireballBook")
	public static Item smallFireballBook = null;

	@ObjectHolder("LargeFireballBook")
	public static Item largeFireballBook = null;
	
	@ObjectHolder("SmallFireballRingBook")
	public static Item smallFireballRingBook = null;	
	
	@ObjectHolder("LingeringFlameBook")
	public static Item lingeringFlameBook = null;	

	@ObjectHolder("LingeringFuryBook")
	public static Item lingeringFuryBook = null;	

	@ObjectHolder("LavaSpiralMistBook")
	public static Item lavaSpiralMistBook = null;
	
	@ObjectHolder("ToxicMistBook")
	public static Item toxicMistBook = null;
	
	@ObjectHolder("WitherSkullBook")
	public static Item witherSkullBook = null;

	@ObjectHolder("WitherMistBook")
	public static Item witherMistBook = null;	
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(new TeleportBook());
		registry.register(new SetSpawnPointBook());	
		registry.register(new SmallFireballBook());	
		registry.register(new LargeFireballBook());		
		registry.register(new SmallFireballRingBook());		
		registry.register(new LingeringFlameBook());	
		registry.register(new LingeringFuryBook());	
		registry.register(new LavaSpiralMistBook());	
		registry.register(new ToxicMistBook());		
		registry.register(new WitherSkullBook());	
		registry.register(new WitherMistBook());				
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		registerModel(teleportBook);
		registerModel(setSpawnPointBook);
		registerModel(smallFireballBook);
		registerModel(largeFireballBook);	
		registerModel(smallFireballRingBook);		
		registerModel(lingeringFlameBook);		
		registerModel(lingeringFuryBook);	
		registerModel(lavaSpiralMistBook);	
		registerModel(toxicMistBook);
		registerModel(witherSkullBook);	
		registerModel(witherMistBook);		
	}	
	
	/**
	 * Register model for item.
	 * 
	 * @param item for which the model is registered.
	 */
	static void registerModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation( item.getRegistryName(), "inventory"));
	}	
}
