package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.SpawnCreeperArmy;

/**
 * Book of creeper army implementation.
 */
public class SpawnCreeperArmyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnCreeperArmyBook.class.getSimpleName();

	public SpawnCreeperArmyBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(new SpawnCreeperArmy()));
	}
}
