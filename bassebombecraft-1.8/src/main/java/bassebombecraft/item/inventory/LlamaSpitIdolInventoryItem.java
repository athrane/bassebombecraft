package bassebombecraft.item.inventory;

import bassebombecraft.item.action.inventory.LlamaSpit;

/**
 * Llama spit implementation.
 */
public class LlamaSpitIdolInventoryItem extends GenericInventoryItem {

	public final static String ITEM_NAME = LlamaSpitIdolInventoryItem.class.getSimpleName();

	public LlamaSpitIdolInventoryItem() {
		super(ITEM_NAME, new LlamaSpit(ITEM_NAME));
	}
}
