package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildAbyss;

/**
 * Book of construction of abyss implementation.
 */
public class BuildAbyssBook extends GenericBlockClickedBook {

	public static final String ITEM_NAME = "BuildAbyssBook";

	public BuildAbyssBook() {
		super(ITEM_NAME, new BuildAbyss());
	}
}
