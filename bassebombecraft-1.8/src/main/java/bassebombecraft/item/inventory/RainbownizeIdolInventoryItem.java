package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.Rainbownize;

/**
 * Rainbownize idol implementation.
 */
public class RainbownizeIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = RainbownizeIdolInventoryItem.class.getSimpleName();

	public RainbownizeIdolInventoryItem() {
		super(ITEM_NAME, new Rainbownize());
	}
}
