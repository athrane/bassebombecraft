package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnCreeperArmy;

/**
 * Book of creeper army implementation.
 */
public class SpawnCreeperArmyBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnCreeperArmyBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnCreeperArmy();

	public SpawnCreeperArmyBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
