package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.rainIdolInventoryItem;

import bassebombecraft.item.action.inventory.SpawnRain;

/**
 * Rain idol implementation.
 */
public class RainIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = RainIdolInventoryItem.class.getSimpleName();

	public RainIdolInventoryItem() {
		super(ITEM_NAME, rainIdolInventoryItem, new SpawnRain());
	}
}
