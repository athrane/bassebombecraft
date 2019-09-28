package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnKittenArmy;

/**
 * Book of army of kittens implementation.
 */
public class SpawnKittenArmyBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnKittenArmyBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnKittenArmy();

	public SpawnKittenArmyBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
