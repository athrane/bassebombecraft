package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.lavaSpiralMistBook;

import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.LavaSpiralMist;

/**
 * Book of Lava spiral block implementation.
 */
public class LavaSpiralMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = LavaSpiralMistBook.class.getSimpleName();

	public LavaSpiralMistBook() {
		super(lavaSpiralMistBook, new GenericBlockSpiralFillMist(new LavaSpiralMist()));
	}
}
