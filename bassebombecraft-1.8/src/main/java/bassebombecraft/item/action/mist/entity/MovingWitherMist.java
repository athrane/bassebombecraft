package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.potion.MobEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions. This class builds a mist with a Wither effect which moves away from
 * the invoking entity/player.
 */
public class MovingWitherMist implements EntityMistActionStrategy {

	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_MOB;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.0F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.3;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	PotionEffect createEffect() {
		return new PotionEffect(MobEffects.WITHER, getEffectDuration());
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3d mistPos, LivingEntity invoker) {
		target.addPotionEffect(createEffect());
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
		return false;
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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
