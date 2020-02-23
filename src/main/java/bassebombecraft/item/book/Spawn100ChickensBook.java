package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.Spawn100Chickens;

/**
 * Book of 100 chicken implementation.
 */
public class Spawn100ChickensBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "Spawn100ChickensBook";
	static final ProjectileAction PROJECTILE_ACTION = new Spawn100Chickens();

	public Spawn100ChickensBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
