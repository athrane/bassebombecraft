package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.Chickenize;

/**
 * Chickenize idol implementation.
 */
public class CharmBeastIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = CharmBeastIdolInventoryItem.class.getSimpleName();

	public CharmBeastIdolInventoryItem() {
		super(ITEM_NAME, new Chickenize());
	}
}
