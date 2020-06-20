package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.mobsLevitationIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddMobsLevitationEffect;

/**
 * Mobs levitation idol implementation.
 */
public class MobsLevitationIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MobsLevitationIdolInventoryItem.class.getSimpleName();

	public MobsLevitationIdolInventoryItem() {
		super(ITEM_NAME, mobsLevitationIdolInventoryItem, new AddMobsLevitationEffect());
	}

}
