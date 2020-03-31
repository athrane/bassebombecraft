package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnGiantZombie;

/**
 * Book of giant zombie .
 */
public class SpawnGiantZombieBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnGiantZombieBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnGiantZombie();

	public SpawnGiantZombieBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
