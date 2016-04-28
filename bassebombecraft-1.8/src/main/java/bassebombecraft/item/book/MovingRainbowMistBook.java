package bassebombecraft.item.book;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockMist;
import bassebombecraft.item.action.mist.block.MovingLavaMist;
import bassebombecraft.item.action.mist.block.MovingRainbowMist;

/**
 * Book of moving rainbow mist implementation.
 */
public class MovingRainbowMistBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "MovingRainbowMistBook";
	static final BlockMistActionStrategy STRATEGY = new MovingRainbowMist();

	public MovingRainbowMistBook() {
		super(ITEM_NAME, new GenericBlockMist(STRATEGY));
	}
}
