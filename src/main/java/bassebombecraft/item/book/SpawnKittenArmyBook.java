package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.SpawnKittenArmy;

/**
 * Book of army of kittens implementation.
 */
public class SpawnKittenArmyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnKittenArmyBook.class.getSimpleName();

	public SpawnKittenArmyBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(new SpawnKittenArmy()));
	}
}
