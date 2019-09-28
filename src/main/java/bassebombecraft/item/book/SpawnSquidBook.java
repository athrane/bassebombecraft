package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnSquid;

/**
 * Book of spawn squid implementation.
 */
public class SpawnSquidBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "SpawnSquidBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnSquid();

	public SpawnSquidBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
