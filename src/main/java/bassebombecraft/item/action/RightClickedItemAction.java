package bassebombecraft.item.action;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Interface for implementing item actions when item is right clicked.
 */
public interface RightClickedItemAction {

	/**
	 * On item is right clicked, i.e. used.
	 * 
	 * @param world  world object.
	 * @param entity entity object.
	 */
	void onRightClick(Level world, LivingEntity entity);

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
