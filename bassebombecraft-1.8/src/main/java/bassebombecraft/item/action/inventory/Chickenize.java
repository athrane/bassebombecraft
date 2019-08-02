package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import java.util.Random;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class transform an entity to a chicken.
 */
public class Chickenize implements InventoryItemActionStrategy {
	static final int CHILD_AGE = 0;
	SoundEvent SOUND = SoundEvents.ENTITY_CHICKEN_HURT;
	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.ENTITY_EFFECT;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.75F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		if (target instanceof ChickenEntity)
			return false;
		return true;
	}

	@Override
	public void applyEffect(Entity target, World world, LivingEntity invoker) {

		// get entity position
		BlockPos position = target.getPosition();
		float yaw = target.rotationYaw;
		float pitch = target.rotationPitch;

		// kill of entity
		target.remove();

		// spawn chicken
		ChickenEntity entity = EntityType.CHICKEN.create(world);
		entity.setGrowingAge(CHILD_AGE);
		entity.setLocationAndAngles(position.getX(), position.getY(), position.getZ(), yaw, pitch);
		world.addEntity(entity);

		// play sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
	}

	@Override
	public int getEffectRange() {
		return 5;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
