package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.Spawn100Chickens;

/**
 * Book of 100 chicken implementation.
 */
public class Spawn100ChickensBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "Spawn100ChickensBook";
	static final ProjectileAction PROJECTILE_ACTION = new Spawn100Chickens();

	public Spawn100ChickensBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
