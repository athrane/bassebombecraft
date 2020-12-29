package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.levitationIdolInventoryItem;

import bassebombecraft.item.action.inventory.AddLevitationEffect;

/**
 * Levitation idol implementation.
 */
public class LevitationIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = LevitationIdolInventoryItem.class.getSimpleName();

	public LevitationIdolInventoryItem() {
		super(levitationIdolInventoryItem, new AddLevitationEffect());
	}
}
