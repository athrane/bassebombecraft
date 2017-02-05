package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.DigMobHole;
import bassebombecraft.projectile.action.ProjectileAction;

/**
 * Book of mob hole.
 */
public class DigMobHoleBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = DigMobHoleBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new DigMobHole();

	public DigMobHoleBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
