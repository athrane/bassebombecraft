package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddMobsAggroEffect;

/**
 * Mobs Aggro idol implementation.
 */
public class MobsAggroIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = MobsAggroIdolInventoryItem.class.getSimpleName();

	public MobsAggroIdolInventoryItem() {
		super(ITEM_NAME, new AddMobsAggroEffect(ITEM_NAME));
	}
}
