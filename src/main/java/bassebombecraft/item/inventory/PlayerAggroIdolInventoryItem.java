package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddPlayerAggroEffect;

/**
 * Player Aggro idol implementation.
 */
public class PlayerAggroIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = PlayerAggroIdolInventoryItem.class.getSimpleName();

	public PlayerAggroIdolInventoryItem() {
		super(ITEM_NAME, new AddPlayerAggroEffect(ITEM_NAME));
	}
}
