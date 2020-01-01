package bassebombecraft.item.book;

import bassebombecraft.item.action.build.BuildMine;

/**
 * Book of mine construction implementation.
 */
public class BuildMineBook extends GenericBlockClickedBook {

	public final static String ITEM_NAME = BuildMineBook.class.getSimpleName();

	public BuildMineBook() {
		super(ITEM_NAME, new BuildMine());
	}
}
