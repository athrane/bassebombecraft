package bassebombecraft.item.basic;

import bassebombecraft.item.action.NullRightClickedItemAction;

/**
 * HUD implementation.
 */
public class HudItem extends GenericNullItem {

	public final static String ITEM_NAME = HudItem.class.getSimpleName();

	public HudItem() {
		super(ITEM_NAME, new NullRightClickedItemAction());
	}

}
