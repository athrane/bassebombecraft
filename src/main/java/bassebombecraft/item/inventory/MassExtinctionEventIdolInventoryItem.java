package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.massExtinctionEventIdolInventoryItem;

import bassebombecraft.item.action.inventory.MassExtinctionEvent;

/**
 * Mass extinction event idol implementation.
 */
public class MassExtinctionEventIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = MassExtinctionEventIdolInventoryItem.class.getSimpleName();

	public MassExtinctionEventIdolInventoryItem() {
		super(ITEM_NAME, massExtinctionEventIdolInventoryItem, new MassExtinctionEvent());
	}
}
