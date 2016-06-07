package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which transform an
 * entity to a chicken.
 */
public class TransformToChicken implements RightClickedItemAction {
	static final int CHILD_AGE = 0;
	static final String SOUND = "mob.chicken.plop";

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		if (entity instanceof EntityChicken)
			return;

		// get entity position
		BlockPos position = entity.getPosition();
		float yaw = entity.rotationYaw;
		float pitch = entity.rotationPitch;

		// kill of entity
		world.removeEntity(entity);

		// spawn chicken
		EntityChicken chicken = new EntityChicken(world);
		chicken.setGrowingAge(CHILD_AGE);
		chicken.setLocationAndAngles(position.getX(), position.getY(), position.getZ(), yaw, pitch);
		world.spawnEntityInWorld(chicken);
		
		// play sound
		Random random = entity.getRNG();
		world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));		
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
