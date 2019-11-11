package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.reaperIdolInventoryItem;

import bassebombecraft.item.action.inventory.KillInvokerAndDestroyIdol;

/**
 * Reaper idol implementation.
 */
public class ReaperIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = ReaperIdolInventoryItem.class.getSimpleName();

	public ReaperIdolInventoryItem() {
		super(ITEM_NAME, reaperIdolInventoryItem, new KillInvokerAndDestroyIdol());
	}
}
