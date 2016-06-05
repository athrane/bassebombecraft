package bassebombecraft.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static bassebombecraft.ModConstants.*;

import bassebombecraft.BassebombeCraft;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Class for initializing blocks.
 */
public class BlockInitializer {

	/**
	 * Meta data for block.
	 */
	static final int META = 0;

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Initialize blocks.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 */
	public void initialize(CreativeTabs targetTab) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		
		RandomBookBlock randomBookBlock = new RandomBookBlock(Material.ground);
		randomBookBlock.setCreativeTab(targetTab);
		GameRegistry.registerBlock(randomBookBlock, RandomBookBlock.BLOCK_NAME);
		ModelResourceLocation location = new ModelResourceLocation(MODID+":"+RandomBookBlock.BLOCK_NAME, "inventory");
		mesher.register(Item.getItemFromBlock(randomBookBlock), META, location);		
		logger.info("initializing Block: " + RandomBookBlock.BLOCK_NAME);
	}

	public static BlockInitializer getInstance() {
		return new BlockInitializer();
	}

}
