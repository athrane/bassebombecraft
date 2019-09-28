package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootLargeFireball;

/**
 * Book of fireball implementation.
 */
public class LargeFireballBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "LargeFireballBook";
	
	public LargeFireballBook() {
		super(ITEM_NAME, new ShootLargeFireball());
	}
}
