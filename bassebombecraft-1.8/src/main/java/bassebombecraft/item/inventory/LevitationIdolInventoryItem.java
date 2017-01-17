package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.AddLevitationEffect;

/**
 * Levitation idol implementation.
 */
public class LevitationIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = LevitationIdolInventoryItem.class.getSimpleName();

	public LevitationIdolInventoryItem() {
		super(ITEM_NAME, new AddLevitationEffect(ITEM_NAME));
	}
}
