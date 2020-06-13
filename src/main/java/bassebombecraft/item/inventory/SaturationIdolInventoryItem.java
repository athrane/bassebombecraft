package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.saturationIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddSaturationEffect;

/**
 * Saturation idol implementation.
 */
public class SaturationIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = SaturationIdolInventoryItem.class.getSimpleName();

	public SaturationIdolInventoryItem() {
		super(ITEM_NAME, saturationIdolInventoryItem, new AddSaturationEffect());
	}
}
