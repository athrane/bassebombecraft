package bassebombecraft.item.action;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain RightClickedItemAction} interface which
 * shoots a large fireball.
 */
public class ShootLargeFireball implements RightClickedItemAction {

	static final SoundEvent SOUND = SoundEvents.ENTITY_GHAST_SHOOT;
	static Random random = new Random();

	@Override
	public void onRightClick(World world, LivingEntity entity) {
		Vec3d v3 = entity.getLook(1);
		EntityLargeFireball projectile = new EntityLargeFireball(world, entity.posX,
				entity.posY + entity.getEyeHeight(), entity.posZ, v3.x, v3.y, v3.z);
		projectile.shootingEntity = entity;
		entity.playSound(SOUND, 1.0F, 1.0F / random.nextFloat() * 0.4F + 0.8F);
		world.spawnEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
