package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SetSpawnPoint;

/**
 * Book of set spawn point implementation.
 */
public class SetSpawnPointBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SetSpawnPointBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SetSpawnPoint();

	public SetSpawnPointBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}

	
}
