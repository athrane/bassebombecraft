package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildRoad;

/**
 * Book of road construction implementation.
 */
public class BuildRoadBook extends GenericBlockClickedBook {

	public static final String ITEM_NAME = BuildRoadBook.class.getSimpleName();
	
	public BuildRoadBook() {
		super(ITEM_NAME, new BuildRoad());
	}
}
