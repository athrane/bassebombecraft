package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.primeMobIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddMobsPrimingEffect;

/**
 * Prime mob idol implementation.
 */
public class PrimeMobIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = PrimeMobIdolInventoryItem.class.getSimpleName();

	public PrimeMobIdolInventoryItem() {
		super(ITEM_NAME, primeMobIdolInventoryItem, new AddMobsPrimingEffect());
	}
}
