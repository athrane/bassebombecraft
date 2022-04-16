package bassebombecraft.item.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import bassebombecraft.entity.projectile.GenericEggProjectile;
import bassebombecraft.projectile.action.ProjectileAction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain RightClickedItemAction} which shoot an egg
 * projectile which executes an {@linkplain ProjectileAction} on impact.
 */
@Deprecated
public class ShootGenericEggProjectile implements RightClickedItemAction {

	static final float VELOCITY = 3.0F;
	static final float INACCURANCY = 1.0F;
	static final SoundEvent SOUND = SoundEvents.EVOKER_CAST_SPELL;
	ProjectileAction action;

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.1D;

	/**
	 * GenericShootEggProjectile constructor.
	 * 
	 * @param action item action which is executed on impact.
	 */
	public ShootGenericEggProjectile(ProjectileAction action) {
		this.action = action;
	}

	@Override
	public void onRightClick(Level world, LivingEntity entity) {
		Random random = getBassebombeCraft().getRandom();

		// calculate orientation
		Vec3 orientation = entity.getViewVector(1).scale(ACCELERATION_MODIFIER);

		GenericEggProjectile projectile = new GenericEggProjectile(world, entity, action);
		projectile.setPos(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
		projectile.shoot(orientation.x, orientation.y, orientation.z, VELOCITY, INACCURANCY);
		entity.playSound(SOUND, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
		world.addFreshEntity(projectile);
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

}
