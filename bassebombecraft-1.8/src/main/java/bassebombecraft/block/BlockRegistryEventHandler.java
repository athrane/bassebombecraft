package bassebombecraft.block;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Class for initializing blocks.
 */
@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class BlockRegistryEventHandler {
	
	/**
	 * Initialize blocks.
	 * 
	 * @param event register blocks event.
	 */	
    @SubscribeEvent
    public static void handleEvent(RegistryEvent.Register<Block> event) {
    	//new Block(Block.Properties.create(Material.IRON)).setRegistryName(ChickenMod.MODID,"test_block")    	
        event.getRegistry().register(new RandomBookBlock());
    }
    
}
