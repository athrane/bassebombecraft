package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a bacon bazooka.
 */
public class ShootBaconBazooka implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_PIG_HURT;	
	static final int FORCE = 3; // Emit force
	static final int CHILD_AGE = -24000;

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		Vec3d v3 = entity.getLookVec();
		
		// get random
		Random random = entity.getRNG();
		
		// spawn pig
		EntityPig pig = new EntityPig(world);
		pig.setGrowingAge(CHILD_AGE);		
		pig.copyLocationAndAnglesFrom(entity);
		pig.posX = entity.posX+v3.xCoord;
		pig.posY = entity.posY+entity.getEyeHeight();
		pig.posZ = entity.posZ+v3.zCoord;
        entity.playSound(SOUND, 0.5F, 0.4F  / random.nextFloat() * 0.4F + 0.8F);
        world.spawnEntity(pig);
		
		// push mob
		double x = v3.xCoord * FORCE;
		double y = v3.yCoord * FORCE;
		double z = v3.zCoord * FORCE;
		Vec3d motionVecForced = new Vec3d(x, y, z);
		pig.addVelocity(motionVecForced.xCoord, motionVecForced.yCoord, motionVecForced.zCoord);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
