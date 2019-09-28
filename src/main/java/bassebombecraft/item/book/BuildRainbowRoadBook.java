package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildRainbowRoad;

/**
 * Book of rainbow road construction implementation.
 */
public class BuildRainbowRoadBook extends GenericBlockClickedBook {

	public final static String ITEM_NAME = "BuildRainbowRoadBook";

	public BuildRainbowRoadBook() {
		super(ITEM_NAME, new BuildRainbowRoad());
	}
}
