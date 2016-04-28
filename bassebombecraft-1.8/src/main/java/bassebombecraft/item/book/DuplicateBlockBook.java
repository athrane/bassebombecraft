package bassebombecraft.item.book;

import bassebombecraft.item.action.build.DuplicateBlock;

/**
 * Book of block duplication.
 */
public class DuplicateBlockBook extends GenericBlockClickedBook {

	public final static String ITEM_NAME = "DuplicateBlockBook";

	public DuplicateBlockBook() {
		super(ITEM_NAME, new DuplicateBlock());
	}
}
