package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.SpawnSkeletonArmy;

/**
 * Book of skeleton army implementation.
 */
public class SpawnSkeletonArmyBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = SpawnSkeletonArmyBook.class.getSimpleName();

	public SpawnSkeletonArmyBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(new SpawnSkeletonArmy()));
	}
}
