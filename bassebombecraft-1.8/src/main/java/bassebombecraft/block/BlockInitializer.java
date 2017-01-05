package bassebombecraft.block;

import static bassebombecraft.ModConstants.MODID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
	 * Initialize blocks.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 */
	public void initialize(CreativeTabs targetTab) {

		RandomBookBlock randomBookBlock = new RandomBookBlock(Material.GROUND);
		registerBlock(targetTab, randomBookBlock, RandomBookBlock.BLOCK_NAME);
	}

	/**
	 * Helper method for registration of block.
	 * 
	 * @param targetTab
	 *            target tab for block.
	 * @param block
	 *            block to register.
	 * @param blockName
	 *            block name.
	 */
	void registerBlock(CreativeTabs targetTab, Block block, String blockName) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		block.setCreativeTab(targetTab);
		block.setRegistryName(blockName);
		GameRegistry.register(block);
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(blockName);
		GameRegistry.register(itemBlock);
		ModelResourceLocation location = new ModelResourceLocation(MODID + ":" + blockName, "inventory");
		mesher.register(Item.getItemFromBlock(block), META, location);
	}

	public static BlockInitializer getInstance() {
		return new BlockInitializer();
	}

}
