package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.rainbownizeBook;

import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.RainbowSpiralMist;

/**
 * Book of rainbownize block implementation.
 */
public class RainbownizeBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = RainbownizeBook.class.getSimpleName();

	public RainbownizeBook() {
		super(rainbownizeBook, new GenericBlockSpiralFillMist(new RainbowSpiralMist()));
	}
}
