package bassebombecraft.item.inventory;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.action.inventory.AddAggroPlayerEffect;

/**
 * Player Aggro idol implementation.
 */
public class PlayerAggroIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = PlayerAggroIdolInventoryItem.class.getSimpleName();

	public PlayerAggroIdolInventoryItem() {
		super(ModConfiguration.playerAggroIdolInventoryItem, new AddAggroPlayerEffect());
	}
}
