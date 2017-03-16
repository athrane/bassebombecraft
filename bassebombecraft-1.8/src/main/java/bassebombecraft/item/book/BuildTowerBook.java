package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildTower;

/**
 * Book of tower construction.
 */
public class BuildTowerBook extends GenericBlockClickedBook {

	public final static String ITEM_NAME = BuildTowerBook.class.getSimpleName();

	public BuildTowerBook() {
		super(ITEM_NAME, new BuildTower());
	}
}
