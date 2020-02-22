package bassebombecraft.item.basic;

import bassebombecraft.item.action.NullRightClickedItemAction;

/**
 * HUD implementation.
 */
public class HudItem extends GenericNullItem {

	/**
	 * Item identifier.
	 */	
	public static final String NAME = HudItem.class.getSimpleName();

	/**
	 * HudItem no-arg constructor,
	 */	
	public HudItem() {
		super(NAME, new NullRightClickedItemAction());
	}

}
