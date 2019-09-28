package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootMultipleArrows;

/**
 * Book of mutiple arrows implementation.
 */
public class MultipleArrowsBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "MultipleArrowsBook";
	
	public MultipleArrowsBook() {
		super(ITEM_NAME, new ShootMultipleArrows());
	}
}