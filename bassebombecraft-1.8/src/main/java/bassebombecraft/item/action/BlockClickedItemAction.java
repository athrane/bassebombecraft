package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Interface for implementing item actions when block is clicked.
 */
public interface BlockClickedItemAction {

	/**
	 * On block right clicked with item.
	 * 
	 * @param player
	 * @param worldIn
	 * @param pos
	 * @param hand
	 * @param facing
	 * @param hitX
	 * @param hitY
	 * @param hitZ
	 * @return action result enum.
	 */
	EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ);

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
