package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddHealingEffect;

/**
 * Angel idol implementation.
 */
public class AngelIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = AngelIdolInventoryItem.class.getSimpleName();

	public AngelIdolInventoryItem() {
		super(ITEM_NAME, new AddHealingEffect());
	}
}
