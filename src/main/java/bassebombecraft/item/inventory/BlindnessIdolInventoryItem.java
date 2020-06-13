package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.blindnessIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddBlindingEffect;

/**
 * Blindness idol implementation.
 */
public class BlindnessIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = BlindnessIdolInventoryItem.class.getSimpleName();

	public BlindnessIdolInventoryItem() {
		super(ITEM_NAME, blindnessIdolInventoryItem, new AddBlindingEffect());
	}
}
