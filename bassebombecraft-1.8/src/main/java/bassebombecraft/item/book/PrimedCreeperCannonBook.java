package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootCreeperCannon;

/**
 * Book of creeper cannon implementation.
 */
public class PrimedCreeperCannonBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "PrimedCreeperCannonBook";
	static final boolean IS_PRIMED = true;

	public PrimedCreeperCannonBook() {
		super(ITEM_NAME, new ShootCreeperCannon(IS_PRIMED));
	}
}
