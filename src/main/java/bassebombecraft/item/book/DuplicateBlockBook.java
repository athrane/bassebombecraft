package bassebombecraft.item.book;

import bassebombecraft.item.action.build.DuplicateBlock;

/**
 * Book of block duplication.
 */
public class DuplicateBlockBook extends GenericBlockClickedBook {

	public static final String ITEM_NAME = DuplicateBlockBook.class.getSimpleName();

	public DuplicateBlockBook() {
		super(ITEM_NAME, new DuplicateBlock());
	}
}
