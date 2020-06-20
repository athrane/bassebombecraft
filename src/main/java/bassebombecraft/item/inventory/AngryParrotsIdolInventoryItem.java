package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.angryParrotsIdolInventoryItem;

import bassebombecraft.item.action.inventory.SpawnAngryParrots;

/**
 * Angry parrots idol implementation.
 */
public class AngryParrotsIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = AngryParrotsIdolInventoryItem.class.getSimpleName();

	public AngryParrotsIdolInventoryItem() {
		super(ITEM_NAME, angryParrotsIdolInventoryItem, new SpawnAngryParrots());
	}
}
