package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class shoots an egg at nearby entities.
 */
public class ShootEgg implements InventoryItemActionStrategy {

	static final float PITCH_OFFSET = 0.0F;

	/**
	 * Projectile spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.CHICKEN_EGG;

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {
		Random random = getBassebombeCraft().getRandom();
		ThrownEgg projectile = EntityType.EGG.create(world);

		// from EntityLlama.spit()
		double d0 = target.getX() - invoker.getX();
		double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - invoker.getY();
		double d2 = target.getZ() - invoker.getZ();
		float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.2F;
		projectile.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
		invoker.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addFreshEntity(projectile);
	}

}
