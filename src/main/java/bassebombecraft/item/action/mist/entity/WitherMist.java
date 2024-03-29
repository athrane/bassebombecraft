package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist with a Wither effect.
 */
public class WitherMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 200; // Measured in ticks

	/**
	 * Particle rendering info.
	 */	
	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.EFFECT;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.0F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(MobEffects.WITHER, getEffectDuration());
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker) {
		target.addEffect(createEffect());
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
		return INFO;
	}

}
