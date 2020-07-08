package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot a small
 * fireball.
 */
public class ShootSmallFireball implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_FIREWORK_ROCKET_SHOOT;

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		Vec3d lookVector = entity.getLook(1);

		SmallFireballEntity projectile = EntityType.SMALL_FIREBALL.create(world);
		projectile.setPosition(entity.getPosX(), entity.getPosY() + entity.getEyeHeight(), entity.getPosZ());
		projectile.setMotion(lookVector);
		projectile.accelerationX = lookVector.x;
		projectile.accelerationY = lookVector.y;
		projectile.accelerationZ = lookVector.z;
		
		// add spawn sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NOP
	}

}
