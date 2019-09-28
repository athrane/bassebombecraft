package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.LavaSpiralMist;

/**
 * Book of Lava spiral block implementation.
 */
public class LavaSpiralMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "LavaSpiralMistBook";
	static final BlockMistActionStrategy STRATEGY = new LavaSpiralMist();

	public LavaSpiralMistBook() {
		super(ITEM_NAME, new GenericBlockSpiralFillMist(STRATEGY));
	}
}
