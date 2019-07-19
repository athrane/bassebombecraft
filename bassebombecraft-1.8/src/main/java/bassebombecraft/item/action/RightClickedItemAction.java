package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	void onRightClick(World world, LivingEntity entity);

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
