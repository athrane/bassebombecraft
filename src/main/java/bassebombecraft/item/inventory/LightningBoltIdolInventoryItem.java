package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.lightningBoltIdolInventoryItem;

import bassebombecraft.item.action.inventory.SpawnLightningBolt;

/**
 * Lightning bolt idol implementation.
 */
public class LightningBoltIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = LightningBoltIdolInventoryItem.class.getSimpleName();

	public LightningBoltIdolInventoryItem() {
		super(lightningBoltIdolInventoryItem, new SpawnLightningBolt());
	}
}
