package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootCreeperCannon;

/**
 * Book of primed creeper cannon implementation.
 */
public class PrimedCreeperCannonBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = PrimedCreeperCannonBook.class.getSimpleName();
	static final boolean IS_PRIMED = true;

	public PrimedCreeperCannonBook() {
		super(ITEM_NAME, new ShootCreeperCannon(IS_PRIMED));
	}
}
