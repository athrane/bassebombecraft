package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.chickenizeIdolInventoryItem;

import bassebombecraft.item.action.inventory.Chickenize;

/**
 * Chickenize idol implementation.
 */
public class ChickenizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = ChickenizeIdolInventoryItem.class.getSimpleName();

	public ChickenizeIdolInventoryItem() {
		super(chickenizeIdolInventoryItem, new Chickenize());
	}
}
