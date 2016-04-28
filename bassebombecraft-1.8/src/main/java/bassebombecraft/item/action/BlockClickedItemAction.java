package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Interface for implementing item actions when block is clicked.
 */
public interface BlockClickedItemAction {

	/**
	 * On block right clicked with item.
	 * 
	 * @param stack
	 * @param playerIn
	 * @param worldIn
	 * @param pos
	 * @param side
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return
	 */
	boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ);

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
