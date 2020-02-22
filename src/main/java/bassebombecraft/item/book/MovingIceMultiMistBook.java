package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingIceMultiMist;

/**
 * Book of multiple moving ice mists implementation.
 */
public class MovingIceMultiMistBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "MovingIceMultiMistBook";
	static final BlockMistActionStrategy STRATEGY = new MovingIceMultiMist();

	public MovingIceMultiMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
