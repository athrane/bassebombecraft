package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SetSpawnPoint;

/**
 * Book of set spawn point implementation.
 */
public class SetSpawnPointBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SetSpawnPointBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SetSpawnPoint();

	public SetSpawnPointBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}

	
}
