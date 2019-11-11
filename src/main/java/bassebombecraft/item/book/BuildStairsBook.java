package bassebombecraft.item.book;

import bassebombecraft.item.action.GenericShootEggProjectile;
import bassebombecraft.projectile.action.SpawnStairs;

/**
 * Book of convenient stairs implementation.
 */
public class BuildStairsBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = BuildStairsBook.class.getSimpleName();

	public BuildStairsBook() {
		super(ITEM_NAME, new GenericShootEggProjectile(new SpawnStairs()));
	}
}
