package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddMobsLevitationEffect;

/**
 * Levitation idol implementation.
 */
public class MobsLevitationIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = MobsLevitationIdolInventoryItem.class.getSimpleName();

	public MobsLevitationIdolInventoryItem() {
		super(ITEM_NAME, new AddMobsLevitationEffect(ITEM_NAME));
	}
}
