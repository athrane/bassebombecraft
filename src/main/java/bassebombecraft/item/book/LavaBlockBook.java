package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnLavaBlock;

/**
 * Book of lava block implementation.
 */
public class LavaBlockBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "LavaBlockBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnLavaBlock();

	public LavaBlockBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
