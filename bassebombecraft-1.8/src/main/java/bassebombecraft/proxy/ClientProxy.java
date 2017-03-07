package bassebombecraft.proxy;

import static bassebombecraft.ModConstants.MODID;

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
	
	
}
