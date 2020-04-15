package bassebombecraft.item.book;

import static bassebombecraft.config.ModConfiguration.largeFireballBook;

import bassebombecraft.item.action.ShootLargeFireball;

/**
 * Book of large fireball implementation.
 */
public class LargeFireballBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = LargeFireballBook.class.getSimpleName();

	public LargeFireballBook() {
		super(ITEM_NAME, largeFireballBook, new ShootLargeFireball());
	}
}
