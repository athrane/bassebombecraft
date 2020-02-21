package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingLavaMist;

/**
 * Book of moving lava mist implementation.
 */
public class MovingLavaMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "MovingLavaMistBook";
	static final BlockMistActionStrategy STRATEGY = new MovingLavaMist();

	public MovingLavaMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
