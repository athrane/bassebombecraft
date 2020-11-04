package bassebombecraft.item.inventory;

import static bassebombecraft.config.ModConfiguration.llamaSpitIdolInventoryItem;

import bassebombecraft.item.action.inventory.LlamaSpit;

/**
 * Llama spit implementation.
 */
public class LlamaSpitIdolInventoryItem extends GenericInventoryItem {

	public static final String ITEM_NAME = LlamaSpitIdolInventoryItem.class.getSimpleName();

	public LlamaSpitIdolInventoryItem() {
		super(llamaSpitIdolInventoryItem, new LlamaSpit());
	}
}
