package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.world.WorldUtils.addLightningAtBlockPos;
import static net.minecraft.core.particles.ParticleTypes.CLOUD;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist with a lightning bolt effect.
 */
public class LightningBoltMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 500; // Measured in ticks

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo info;

	/**
	 * LightningBoltMist constructor.
	 */
	public LightningBoltMist() {
		super();

		// initiate particle rendering info
		float r = 0.75F;
		float g = 0.75F;
		float b = 0.75F;
		int numbers = 5;
		SimpleParticleType type = CLOUD;
		int duration = 20;
		double speed = 0.1;
		info = getInstance(type, numbers, duration, r, g, b, speed);
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker) {
		Level world = target.getCommandSenderWorld();

		AABB aabb = target.getBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		BlockPos.betweenClosedStream(min, max).forEach(pos -> addLightningAtBlockPos(world, pos));
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return false;
	}

	@Override
	public boolean isStationary() {
		return true;
	}

	@Override
	public boolean isOneShootEffect() {
		return false;
	}

	@Override
	public int getEffectRange() {
		return 5;
	}

	@Override
	public ParticleRenderingInfo getRenderingInfos() {
		return info;
	}

}
