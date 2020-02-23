package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnCobweb;

/**
 * Book of cobweb implementation.
 */
public class CobwebBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "CobwebBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnCobweb();

	public CobwebBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(PROJECTILE_ACTION));
	}
}
