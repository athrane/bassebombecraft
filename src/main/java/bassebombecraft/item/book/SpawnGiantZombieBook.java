package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnGiantZombie;

/**
 * Book of giant zombie .
 */
public class SpawnGiantZombieBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnGiantZombieBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnGiantZombie();

	public SpawnGiantZombieBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
