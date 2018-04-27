package bassebombecraft.item;

import java.util.stream.Stream;

import bassebombecraft.ModConstants;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
import bassebombecraft.item.book.CreeperApocalypseBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.HealingMistBook;
import bassebombecraft.item.book.LargeFireballBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.LingeringFlameBook;
import bassebombecraft.item.book.LingeringFuryBook;
import bassebombecraft.item.book.MovingIceMultiMistBook;
import bassebombecraft.item.book.MovingLavaMistBook;
import bassebombecraft.item.book.MovingLavaMultiMistBook;
import bassebombecraft.item.book.MovingRainbowMistBook;
import bassebombecraft.item.book.MovingTntMistBook;
import bassebombecraft.item.book.MovingWaterMultiMistBook;
import bassebombecraft.item.book.MovingWitherMistBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.Spawn100ChickensBook;
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnSquidBook;
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

	static Item[] items = {			
			new TeleportBook(),
			new SetSpawnPointBook(),	
			new SmallFireballBook(),	
			new LargeFireballBook(),		
			new SmallFireballRingBook(),		
			new LingeringFlameBook(),	
			new LingeringFuryBook(),	
			new LavaSpiralMistBook(),	
			new ToxicMistBook(),		
			new WitherSkullBook(),	
			new WitherMistBook(),		
			new MovingWitherMistBook(),				
			new MovingLavaMistBook(),			
			new MovingLavaMultiMistBook(),				
			new MovingIceMultiMistBook(),			
			new MovingRainbowMistBook(),			
			new MovingWaterMultiMistBook(),			
			new HealingMistBook(),
			new MovingTntMistBook(),
			new SpawnFlamingChickenBook(),
			new SpawnSquidBook(),
			new BaconBazookaBook(),
			new CreeperCannonBook(),
			new PrimedCreeperCannonBook(),
			new BearBlasterBook(),
			new CreeperApocalypseBook(),
			new Spawn100ChickensBook()			
	};
	
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.registerAll(items);		
		
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event) {
		Stream.of(items).forEach(i -> registerModel(i));		
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
