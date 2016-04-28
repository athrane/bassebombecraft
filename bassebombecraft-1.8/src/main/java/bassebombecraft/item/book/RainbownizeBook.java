package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.RainbowSpiralMist;

/**
 * Book of rainbownize block implementation.
 */
public class RainbownizeBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "RainbownizeBook";
	static final BlockMistActionStrategy STRATEGY = new RainbowSpiralMist();

	public RainbownizeBook() {
		super(ITEM_NAME, new GenericBlockSpiralFillMist(STRATEGY));
	}
}
