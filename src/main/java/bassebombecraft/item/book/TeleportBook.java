package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.TeleportEntity;

/**
 * Book of teleport implementation.
 */
public class TeleportBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = TeleportBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new TeleportEntity();

	public TeleportBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
