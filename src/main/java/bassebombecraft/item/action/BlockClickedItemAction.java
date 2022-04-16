package bassebombecraft.item.action;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;

/**
 * Interface for implementing item actions when block is clicked.
 */
public interface BlockClickedItemAction {

	/**
	 * On block right clicked with item.
	 * 
	 * @param context item use context.
	 * 
	 * @return action result..
	 */
	InteractionResult onItemUse(UseOnContext context);

	/**
	 * On item update.
	 * 
	 * @param stack
	 * @param worldIn
	 * @param entityIn
	 * @param itemSlot
	 * @param isSelected
	 */
	void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected);

}
