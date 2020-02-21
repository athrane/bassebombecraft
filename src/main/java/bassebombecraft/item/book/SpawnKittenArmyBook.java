package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.SpawnKittenArmy;

/**
 * Book of army of kittens implementation.
 */
public class SpawnKittenArmyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnKittenArmyBook.class.getSimpleName();

	public SpawnKittenArmyBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(new SpawnKittenArmy()));
	}
}
