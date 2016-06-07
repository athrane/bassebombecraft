package bassebombecraft.item.inventory;

import bassebombecraft.item.action.SpawnRain;

/**
 * Rain idol implementation.
 */
public class RainIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = RainIdolInventoryItem.class.getSimpleName();

	public RainIdolInventoryItem() {
		super(ITEM_NAME, new SpawnRain());
	}
}
