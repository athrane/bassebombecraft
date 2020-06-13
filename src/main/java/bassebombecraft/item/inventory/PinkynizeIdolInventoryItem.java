package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.pinkynizeIdolInventoryItem;
import static bassebombecraft.config.ModConfiguration.pinkynizeSpiralSize;

import java.util.function.Supplier;

import bassebombecraft.item.action.inventory.Pinkynize;

/**
 * Pinkynize idol implementation.
 */
public class PinkynizeIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = PinkynizeIdolInventoryItem.class.getSimpleName();

	public PinkynizeIdolInventoryItem() {
		super(ITEM_NAME, pinkynizeIdolInventoryItem, new Pinkynize());
	}
}
