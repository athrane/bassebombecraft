package bassebombecraft.item.book;

import bassebombecraft.item.action.ShootBearBlaster;

/**
 * Book of bear blaster implementation.
 */
public class BearBlasterBook extends GenericRightClickedBook {

	public final static String ITEM_NAME = BearBlasterBook.class.getSimpleName();

	public BearBlasterBook() {
		super(ITEM_NAME, new ShootBearBlaster());
	}

}
