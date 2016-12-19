package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a
 * creeper.
 */
public class ShootCreeperCannon implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_CREEPER_HURT;	
	static final int FORCE = 3; // Emit force
	boolean isPrimed;

	/**
	 * ShootCreeperCannon constructor.
	 * 
	 * @param isPrimed defines whether creeper is primed.
	 */
	public ShootCreeperCannon(boolean isPrimed) {
		this.isPrimed = isPrimed; 
	}

	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
		Vec3d v3 = entity.getLookVec();

		// get random
		Random random = entity.getRNG();

		// spawn creeper
		EntityCreeper creeper = new EntityCreeper(world);
		creeper.copyLocationAndAnglesFrom(entity);
		creeper.posX = entity.posX + v3.xCoord;
		creeper.posY = entity.posY + entity.getEyeHeight();
		creeper.posZ = entity.posZ + v3.zCoord;
        entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);
        world.spawnEntity(creeper);

		// push mob
		double x = v3.xCoord * FORCE;
		double y = v3.yCoord * FORCE;
		double z = v3.zCoord * FORCE;
		Vec3d motionVecForced = new Vec3d(x, y, z);
		creeper.addVelocity(motionVecForced.xCoord, motionVecForced.yCoord, motionVecForced.zCoord);
		
		if(isPrimed) {
			creeper.ignite();
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
