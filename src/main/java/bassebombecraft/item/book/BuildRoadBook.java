package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildRoad;

/**
 * Book of road construction implementation.
 */
public class BuildRoadBook extends GenericBlockClickedBook {

	public final static String ITEM_NAME = "BuildRoadBook";

	public BuildRoadBook() {
		super(ITEM_NAME, new BuildRoad());
	}
}