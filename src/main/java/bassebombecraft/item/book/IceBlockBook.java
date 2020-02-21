package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnIceBlock;

/**
 * Book of ice block implementation.
 */
public class IceBlockBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "IceBlockBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnIceBlock();

	public IceBlockBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
