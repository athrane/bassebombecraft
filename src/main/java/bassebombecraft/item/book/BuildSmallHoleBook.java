package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildSmallHole;

/**
 * Book of construction of small hole implementation.
 */
public class BuildSmallHoleBook extends GenericBlockClickedBook {

	public static final String ITEM_NAME = "BuildSmallHoleBook";

	public BuildSmallHoleBook() {
		super(ITEM_NAME, new BuildSmallHole());
	}
}
