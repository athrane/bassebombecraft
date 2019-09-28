package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnFlamingChicken;

/**
 * Book of flaming chicken implementation.
 */
public class SpawnFlamingChickenBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = "SpawnFlamingChickenBook";
	static final ProjectileAction PROJECTILE_ACTION = new SpawnFlamingChicken();

	public SpawnFlamingChickenBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
