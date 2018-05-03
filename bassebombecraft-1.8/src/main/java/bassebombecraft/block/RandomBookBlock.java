package bassebombecraft.block;

import java.util.Random;

import bassebombecraft.BassebombeCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

/**
 * Random book block which drops a random book when destroyed.
 */
public class RandomBookBlock extends Block {

	public final static String BLOCK_NAME = RandomBookBlock.class.getSimpleName();

	public RandomBookBlock(Material materialIn) {
		super(materialIn);
		setUnlocalizedName(BLOCK_NAME);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		Item[] bookItems = BassebombeCraft.getBassebombeCraft().getBookItems();

		// exit if no list is returned
		if (bookItems == null)
			return super.getItemDropped(state, rand, fortune);

		// return random book
		int randomBook = rand.nextInt(bookItems.length);
		return bookItems[randomBook];

	}

	@Override
	public int quantityDropped(Random random) {
		return 1;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return 1;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1;
	}

}
