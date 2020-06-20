package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.mobsAggroIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddAggroMobEffect;

/**
 * Mobs Aggro idol implementation.
 */
public class MobsAggroIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MobsAggroIdolInventoryItem.class.getSimpleName();

	public MobsAggroIdolInventoryItem() {
		super(ITEM_NAME, mobsAggroIdolInventoryItem, new AddAggroMobEffect());
	}
}
