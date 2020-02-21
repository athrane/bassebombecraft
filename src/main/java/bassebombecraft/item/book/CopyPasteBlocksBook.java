package bassebombecraft.item.book;

import bassebombecraft.item.action.build.CopyPasteBlocks;

/**
 * Book of copy-paste blocks implementation.
 */
public class CopyPasteBlocksBook extends GenericBlockClickedBook {

	public static final String ITEM_NAME = CopyPasteBlocksBook.class.getSimpleName();;
	
	public CopyPasteBlocksBook() {
		super(ITEM_NAME, new CopyPasteBlocks());
	}
}
