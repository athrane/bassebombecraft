package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a
 * creeper.
 */
public class ShootCreeperCannon implements RightClickedItemAction {

	static final int FORCE = 3; // Emit force
	static final String SOUND = "mob.creeper.say";
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
		Vec3 v3 = entity.getLookVec();

		// get random
		Random random = entity.getRNG();

		// spawn creeper
		EntityCreeper creeper = new EntityCreeper(world);
		creeper.copyLocationAndAnglesFrom(entity);
		creeper.posX = entity.posX + v3.xCoord;
		creeper.posY = entity.posY + entity.getEyeHeight();
		creeper.posZ = entity.posZ + v3.zCoord;
		world.playSoundAtEntity(entity, SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
		world.spawnEntityInWorld(creeper);

		// push mob
		double x = v3.xCoord * FORCE;
		double y = v3.yCoord * FORCE;
		double z = v3.zCoord * FORCE;
		Vec3 motionVecForced = new Vec3(x, y, z);
		creeper.addVelocity(motionVecForced.xCoord, motionVecForced.yCoord, motionVecForced.zCoord);
		
		if(isPrimed) {
			creeper.func_146079_cb();
			creeper.setCreeperState(1);					
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
