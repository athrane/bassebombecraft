package bassebombecraft.inventory.container;

import static bassebombecraft.item.composite.CompositeMagicItem.getItemStackHandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

/**
 * Implementation of the {@linkplain INamedContainerProvider} interface to
 * provide a server container instance with the createMenu method.
 */
public class CompositeMagicItemContainerProvider implements INamedContainerProvider {

	/**
	 * Itemstack used to initialize container.
	 */
	ItemStack itemStack;

	/**
	 * Constructor.
	 * 
	 * @param itemStack composite magic item stack.
	 */
	public CompositeMagicItemContainerProvider(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public ITextComponent getDisplayName() {
		return itemStack.getDisplayName();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new CompositeMagicItemContainer(id, inventory, getItemStackHandler(itemStack), itemStack);
	}

}
