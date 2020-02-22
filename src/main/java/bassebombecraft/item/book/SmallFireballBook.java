package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootSmallFireball;

/**
 * Book of small fireball implementation.
 */
public class SmallFireballBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "SmallFireballBook";
	
	public SmallFireballBook() {
		super(ITEM_NAME, new ShootSmallFireball());
	}
}
