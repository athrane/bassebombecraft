package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootWitherSkull;

/**
 * Book of wither skull implementation.
 */
public class WitherSkullBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "WitherSkullBook";
	
	public WitherSkullBook() {
		super(ITEM_NAME, new ShootWitherSkull());
	}
}
