package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.CreeperApocalypse;
import bassebombecraft.projectile.action.ProjectileAction;

/**
 * Book of creeper apocalypse implementation.
 */
public class CreeperApocalypseBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = CreeperApocalypseBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new CreeperApocalypse();

	public CreeperApocalypseBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
