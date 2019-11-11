package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.meteorIdolInventoryItem;

import bassebombecraft.item.action.inventory.ShootMeteor;

/**
 * Meteor idol implementation.
 */
public class MeteorIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = MeteorIdolInventoryItem.class.getSimpleName();

	public MeteorIdolInventoryItem() {
		super(ITEM_NAME, meteorIdolInventoryItem, new ShootMeteor());
	}
}
