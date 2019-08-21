package bassebombecraft.block;

import static bassebombecraft.ModConstants.MODID;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 * Random book block which drops a random book when destroyed.
 */
public class RandomBookBlock extends Block {

	/**
	 * Block name.
	 */
	public final static String BLOCK_NAME = RandomBookBlock.class.getSimpleName();

	public RandomBookBlock() {
		super(Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F).lightValue(14));
		setRegistryName(MODID, BLOCK_NAME.toLowerCase());
	}
}
