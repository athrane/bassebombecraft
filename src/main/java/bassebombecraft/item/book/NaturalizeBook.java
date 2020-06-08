package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.naturalizeBook;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.NaturalizeSpiralMist;

/**
 * Book of naturalize implementation.
 */
public class NaturalizeBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = NaturalizeBook.class.getSimpleName();
	static final BlockMistActionStrategy STRATEGY = new NaturalizeSpiralMist();

	public NaturalizeBook() {
		super(ITEM_NAME, naturalizeBook, new GenericBlockSpiralFillMist(STRATEGY));
	}
}
