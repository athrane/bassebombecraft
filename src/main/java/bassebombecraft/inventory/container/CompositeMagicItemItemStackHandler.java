package bassebombecraft.inventory.container;

import static bassebombecraft.item.ItemUtils.isTypeCompositeItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Subclass of {@linkplain ItemStackHandler} which enforces addition of
 * composite items only to the composite item configuration/sequence in the
 * {@linkplain CompositeMagicItemContainer}.
 */
public class CompositeMagicItemItemStackHandler extends ItemStackHandler {

	/**
	 * Constructor.
	 * 
	 * @param size item stack size.
	 */
	public CompositeMagicItemItemStackHandler(int size) {
		super(size);
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {

		// exit if empty item stack
		if (stack.isEmpty())
			return false;

		// validate item is an composite item
		Item item = stack.getItem();
		return isTypeCompositeItem(item);
	}

}
