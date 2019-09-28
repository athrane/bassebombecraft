package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.Chickenize;

/**
 * Chickenize idol implementation.
 */
public class ChickenizeIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = ChickenizeIdolInventoryItem.class.getSimpleName();

	public ChickenizeIdolInventoryItem() {
		super(ITEM_NAME, new Chickenize());
	}
}