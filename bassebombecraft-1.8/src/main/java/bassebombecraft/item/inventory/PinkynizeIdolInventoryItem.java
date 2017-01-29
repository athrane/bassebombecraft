package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.Pinkynize;

/**
 * Pinkynize idol implementation.
 */
public class PinkynizeIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PinkynizeIdolInventoryItem.class.getSimpleName();

	public PinkynizeIdolInventoryItem() {
		super(ITEM_NAME, new Pinkynize(ITEM_NAME));
	}
}
