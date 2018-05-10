package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddSaturationEffect;

/**
 * Saturation idol implementation.
 */
public class SaturationIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = SaturationIdolInventoryItem.class.getSimpleName();

	public SaturationIdolInventoryItem() {
		super(ITEM_NAME, new AddSaturationEffect(ITEM_NAME));
	}
}
