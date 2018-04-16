package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a large fireball. 
 */
public class ShootWitherSkull implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_WITHER_SHOOT;
	static Random random = new Random();	
	
	@Override
	public void onRightClick(World world, EntityLivingBase entity) {
        Vec3d v3 = entity.getLook(1);
        EntityWitherSkull projectile = new EntityWitherSkull(world, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, v3.x, v3.y, v3.z);
        projectile.shootingEntity = entity;
        entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);
        world.spawnEntity(projectile );
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP 
	}	

}
