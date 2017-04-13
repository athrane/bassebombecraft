package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootSmallFireballRing;

/**
 * Book of small fireball ring implementation.
 */
public class SmallFireballRingBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SmallFireballRingBook.class.getSimpleName();

	public SmallFireballRingBook() {
		super(ITEM_NAME, new ShootSmallFireballRing());
	}
}
