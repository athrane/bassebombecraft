package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.angelIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddHealingEffect;

/**
 * Angel idol implementation.
 */
public class AngelIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = AngelIdolInventoryItem.class.getSimpleName();

	public AngelIdolInventoryItem() {
		super(ITEM_NAME, angelIdolInventoryItem, new AddHealingEffect());
	}
}
