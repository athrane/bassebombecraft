package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.smallFireballBook;

import bassebombecraft.item.action.ShootSmallFireball;

/**
 * Book of small fireball implementation.
 */
public class SmallFireballBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "SmallFireballBook";

	public SmallFireballBook() {
		super(ITEM_NAME, smallFireballBook, new ShootSmallFireball());
	}
}
