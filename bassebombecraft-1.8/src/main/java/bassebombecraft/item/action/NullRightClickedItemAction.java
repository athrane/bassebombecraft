package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Null implementation of the {@linkplain RightClickedItemAction} interface.
 */
public class NullRightClickedItemAction implements RightClickedItemAction {

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		// NOP
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
