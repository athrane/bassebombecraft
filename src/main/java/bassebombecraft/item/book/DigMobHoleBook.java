package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.DigMobHole;

/**
 * Book of mob hole.
 */
public class DigMobHoleBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = DigMobHoleBook.class.getSimpleName();

	public DigMobHoleBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(new DigMobHole()));
	}
}
