package bassebombecraft.item.action;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Null implementation of the {@linkplain RightClickedItemAction} interface.
 */
public class NullRightClickedItemAction implements RightClickedItemAction {

	@Override
	public void onRightClick(Level world, LivingEntity entity) {
		// NOP
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
