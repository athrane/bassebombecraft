package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.Naturalize;

/**
 * Flower idol implementation.
 */
public class FlowerIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = FlowerIdolInventoryItem.class.getSimpleName();

	public FlowerIdolInventoryItem() {
		super(ITEM_NAME, new Naturalize());
	}
}
