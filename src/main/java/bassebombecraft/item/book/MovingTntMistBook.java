package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingTntMist;

/**
 * Book of moving TNT mist implementation.
 */
public class MovingTntMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = MovingTntMistBook.class.getSimpleName();
	static final BlockMistActionStrategy STRATEGY = new MovingTntMist();

	public MovingTntMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
