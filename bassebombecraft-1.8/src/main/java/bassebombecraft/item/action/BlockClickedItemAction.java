package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

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
	ActionResultType onItemUse(ItemUseContext context);

	/**
	 * On item update.
	 * 
	 * @param stack
	 * @param worldIn
	 * @param entityIn
	 * @param itemSlot
	 * @param isSelected
	 */
	void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected);

}
