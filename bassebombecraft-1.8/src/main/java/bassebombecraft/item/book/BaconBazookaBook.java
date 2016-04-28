package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootBaconBazooka;

/**
 * Book of bacon bazooka implementation.
 */
public class BaconBazookaBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "BaconBazookaBook";

	public BaconBazookaBook() {
		super(ITEM_NAME, new ShootBaconBazooka());
	}
}
