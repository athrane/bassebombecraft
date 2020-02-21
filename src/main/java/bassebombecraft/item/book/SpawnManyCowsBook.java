package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnManyCows;

/**
 * Book of many cows implementation.
 */
public class SpawnManyCowsBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = "SpawnManyCowsBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnManyCows();

	public SpawnManyCowsBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
