package bassebombecraft.inventory.container;

import static bassebombecraft.item.composite.CompositeMagicItem.getItemStackHandler;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

/**
 * Implementation of the {@linkplain INamedContainerProvider} interface to
 * provide a server container instance with the createMenu method.
 */
public class CompositeMagicItemContainerProvider implements MenuProvider {

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
	public Component getDisplayName() {
		return itemStack.getHoverName();
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
		return new CompositeMagicItemContainer(id, inventory, getItemStackHandler(itemStack), itemStack);
	}

}
