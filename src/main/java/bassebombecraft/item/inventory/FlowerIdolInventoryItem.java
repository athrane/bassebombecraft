package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.flowerIdolInventoryItem;

import bassebombecraft.item.action.inventory.Naturalize;

/**
 * Flower idol implementation.
 */
public class FlowerIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = FlowerIdolInventoryItem.class.getSimpleName();

	public FlowerIdolInventoryItem() {
		super(ITEM_NAME, flowerIdolInventoryItem, new Naturalize());
	}
}
