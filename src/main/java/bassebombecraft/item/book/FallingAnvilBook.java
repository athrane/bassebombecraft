package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnAnvil;

/**
 * Book of anvil block implementation.
 */
public class FallingAnvilBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = FallingAnvilBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnAnvil();

	public FallingAnvilBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
