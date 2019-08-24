package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.NaturalizeSpiralMist;

/**
 * Book of naturalize implementation.
 */
public class NaturalizeBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = NaturalizeBook.class.getSimpleName();
	static final BlockMistActionStrategy STRATEGY = new NaturalizeSpiralMist();

	public NaturalizeBook() {
		super(ITEM_NAME, new GenericBlockSpiralFillMist(STRATEGY));
	}
}
