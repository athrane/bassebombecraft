package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.baconBazookaBook;

import bassebombecraft.item.action.ShootBaconBazooka;

/**
 * Book of bacon bazooka implementation.
 */
public class BaconBazookaBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = BaconBazookaBook.class.getSimpleName();

	public BaconBazookaBook() {
		super(ITEM_NAME, baconBazookaBook, new ShootBaconBazooka());
	}

}
