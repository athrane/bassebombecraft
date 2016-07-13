package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.SpawnRain;
import bassebombecraft.item.action.inventory.RegisterForPvp;

/**
 * Pvp idol implementation.
 */
public class PvpIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PvpIdolInventoryItem.class.getSimpleName();

	public PvpIdolInventoryItem() {
		super(ITEM_NAME, new RegisterForPvp());
	}
}
