package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.rainbownizeIdolInventoryItem;

import bassebombecraft.item.action.inventory.Rainbownize;

/**
 * Rainbownize idol implementation.
 */
public class RainbownizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = RainbownizeIdolInventoryItem.class.getSimpleName();

	public RainbownizeIdolInventoryItem() {
		super(rainbownizeIdolInventoryItem, new Rainbownize());
	}
}
