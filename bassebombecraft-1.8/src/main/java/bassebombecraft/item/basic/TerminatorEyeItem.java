package bassebombecraft.item.basic;

import bassebombecraft.item.action.NullRightClickedItemAction;

/**
 * Book of bacon bazooka implementation.
 */
public class TerminatorEyeItem extends GenericNullItem {

	public final static String ITEM_NAME = TerminatorEyeItem.class.getSimpleName();

	public TerminatorEyeItem() {
		super(ITEM_NAME, new NullRightClickedItemAction());
	}

}
