package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddBlindingEffect;

/**
 * Blindness idol implementation.
 */
public class BlindnessIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = BlindnessIdolInventoryItem.class.getSimpleName();

	public BlindnessIdolInventoryItem() {
		super(ITEM_NAME, new AddBlindingEffect());
	}
}