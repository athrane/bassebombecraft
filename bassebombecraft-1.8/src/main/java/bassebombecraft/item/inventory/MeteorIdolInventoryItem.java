package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.ShootMeteor;

/**
 * Meteor idol implementation.
 */
public class MeteorIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = MeteorIdolInventoryItem.class.getSimpleName();

	public MeteorIdolInventoryItem() {
		super(ITEM_NAME, new ShootMeteor(ITEM_NAME));
	}
}
