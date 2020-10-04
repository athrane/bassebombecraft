package bassebombecraft.inventory.container;

import static bassebombecraft.ModConstants.COMPOSITE_MAX_SIZE;

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
	ItemStack compositeItemStack;

	/**
	 * Constructor.
	 * 
	 * @param compositeItemStack composite magic item stack.
	 */
	public CompositeMagicItemContainerProvider(ItemStack compositeItemStack) {
		this.compositeItemStack = compositeItemStack;
	}

	@Override
	public ITextComponent getDisplayName() {
		return compositeItemStack.getDisplayName();
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new CompositeMagicItemContainer(id, inventory,
				new CompositeMagicItemItemStackHandler(COMPOSITE_MAX_SIZE), compositeItemStack);
	}

}
