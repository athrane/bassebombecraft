package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingWaterMultiMist;

/**
 * Book of multiple moving water mists implementation.
 */
public class MovingWaterMultiMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "MovingWaterMultiMistBook";
	static final BlockMistActionStrategy STRATEGY = new MovingWaterMultiMist();

	public MovingWaterMultiMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
