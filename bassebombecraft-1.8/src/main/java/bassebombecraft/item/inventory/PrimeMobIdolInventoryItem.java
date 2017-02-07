package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddMobsPrimingEffect;

/**
 * Prime mob idol implementation.
 */
public class PrimeMobIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PrimeMobIdolInventoryItem.class.getSimpleName();

	public PrimeMobIdolInventoryItem() {
		super(ITEM_NAME, new AddMobsPrimingEffect(ITEM_NAME));
	}
}
