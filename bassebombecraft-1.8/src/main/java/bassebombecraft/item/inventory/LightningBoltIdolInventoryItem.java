package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.SpawnLightningBolt;

/**
 * Lightning bolt idol implementation.
 */
public class LightningBoltIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = LightningBoltIdolInventoryItem.class.getSimpleName();

	public LightningBoltIdolInventoryItem() {
		super(ITEM_NAME, new SpawnLightningBolt());
	}
}
