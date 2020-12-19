package bassebombecraft.item.basic;

import static bassebombecraft.config.ModConfiguration.hudItem;

import bassebombecraft.item.action.NullRightClickedItemAction;

/**
 * HUD implementation.
 */
public class HudItem extends GenericNullItem {

	public static final String NAME = HudItem.class.getSimpleName();

	public HudItem() {
		super(hudItem, new NullRightClickedItemAction());
	}

}
