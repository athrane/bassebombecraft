package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingLavaMultiMist;

/**
 * Book of multiple moving lava mists implementation.
 */
public class MovingLavaMultiMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "MovingLavaMultiMistBook";
	static final BlockMistActionStrategy STRATEGY = new MovingLavaMultiMist();

	public MovingLavaMultiMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
