package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.witherSkullBook;

import bassebombecraft.item.action.ShootWitherSkull;

/**
 * Book of wither skull implementation.
 */
public class WitherSkullBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = WitherSkullBook.class.getSimpleName();

	public WitherSkullBook() {
		super(ITEM_NAME, witherSkullBook, new ShootWitherSkull());
	}
}
