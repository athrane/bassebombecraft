package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import bassebombecraft.projectile.action.SpawnSkeletonArmy;

/**
 * Book of skeleton army implementation.
 */
public class SpawnSkeletonArmyBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = SpawnSkeletonArmyBook.class.getSimpleName();
	static final ProjectileAction PROJECTILE_ACTION = new SpawnSkeletonArmy();

	public SpawnSkeletonArmyBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(PROJECTILE_ACTION));
	}
}
