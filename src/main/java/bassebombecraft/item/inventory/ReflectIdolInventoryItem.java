package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.reflectIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddReflectEffect;


/**
 * Reflect idol implementation.
 */
public class ReflectIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = ReflectIdolInventoryItem.class.getSimpleName();

	public ReflectIdolInventoryItem() {
		super(reflectIdolInventoryItem, new AddReflectEffect());
	}
}
