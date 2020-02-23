package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootGenericEggProjectile;
import bassebombecraft.projectile.action.SpawnStairs;

/**
 * Book of convenient stairs implementation.
 */
public class BuildStairsBook extends GenericRightClickedBook {

	public static final String ITEM_NAME = BuildStairsBook.class.getSimpleName();

	public BuildStairsBook() {
		super(ITEM_NAME, new ShootGenericEggProjectile(new SpawnStairs()));
	}
}
