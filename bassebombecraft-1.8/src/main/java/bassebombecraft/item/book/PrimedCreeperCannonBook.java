package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootCreeperCannon;

/**
 * Book of creeper cannon implementation.
 */
public class PrimedCreeperCannonBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = PrimedCreeperCannonBook.class.getSimpleName();
	static final boolean IS_PRIMED = true;
	final static String CONFIG_KEY = "ShootPrimedCreeperCannon";

	public PrimedCreeperCannonBook() {
		super(ITEM_NAME, new ShootCreeperCannon(IS_PRIMED, CONFIG_KEY));
	}
}
