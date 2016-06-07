package bassebombecraft.item.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which makes the
 * weather raining.
 */
public class SpawnRain implements RightClickedItemAction {

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		world.getWorldInfo().setRaining(true);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
