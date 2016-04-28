package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootCreeperCannon;

/**
 * Book of creeper cannon implementation.
 */
public class CreeperCannonBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "CreeperCannonBook";
	static final boolean ISNT_PRIMED = false;

	public CreeperCannonBook() {
		super(ITEM_NAME, new ShootCreeperCannon(ISNT_PRIMED));
	}
}
