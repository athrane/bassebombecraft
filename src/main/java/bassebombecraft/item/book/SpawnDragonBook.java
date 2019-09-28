package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnDragon;

/**
 * Book of instant dragon implementation.
 */
public class SpawnDragonBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnDragonBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnDragon();

	public SpawnDragonBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
