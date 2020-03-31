package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class shoots an egg at nearby entities.
 */
public class ShootEgg implements InventoryItemActionStrategy {

	static final float PITCH_OFFSET = 0.0F;

	/**
	 * Projectile spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_CHICKEN_EGG;

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
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		Random random = getBassebombeCraft().getRandom();
		EggEntity projectile = EntityType.EGG.create(world);

		// from EntityLlama.spit()
		double d0 = target.getPosX() - invoker.getPosX();
		double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - invoker.getPosY();
		double d2 = target.getPosZ() - invoker.getPosZ();
		float f = MathHelper.sqrt(d0 * d0 + d2 * d2) * 0.2F;
		projectile.shoot(d0, d1 + (double) f, d2, 1.5F, 10.0F);
		invoker.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(projectile);
	}

}
