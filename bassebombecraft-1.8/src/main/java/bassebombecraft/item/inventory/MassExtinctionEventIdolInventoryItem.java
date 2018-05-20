package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.MassExtinctionEvent;

/**
 * Mass extinction event idol implementation.
 */
public class MassExtinctionEventIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = MassExtinctionEventIdolInventoryItem.class.getSimpleName();

	public MassExtinctionEventIdolInventoryItem() {
		super(ITEM_NAME, new MassExtinctionEvent(ITEM_NAME));
	}
}
