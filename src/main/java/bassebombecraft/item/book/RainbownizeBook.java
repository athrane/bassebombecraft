package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.rainbownizeBook;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.RainbowSpiralMist;

/**
 * Book of rainbownize block implementation.
 */
public class RainbownizeBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = RainbownizeBook.class.getSimpleName();
	static final BlockMistActionStrategy STRATEGY = new RainbowSpiralMist();

	public RainbownizeBook() {
		super(ITEM_NAME, rainbownizeBook, new GenericBlockSpiralFillMist(STRATEGY));
	}
}
