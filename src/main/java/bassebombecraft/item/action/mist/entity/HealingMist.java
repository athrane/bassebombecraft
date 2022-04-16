package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.healingMistDuration;
import static bassebombecraft.config.ModConfiguration.healingMistParticleInfo;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of {@linkplain EntityMistActionStrategy} for construction of
 * mist action. This class builds a mist with a healing effect.
 */
public class HealingMist implements EntityMistActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = VacuumMist.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo info;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor.
	 */
	public HealingMist() {
		info = createInfoFromConfig(healingMistParticleInfo);
		duration = healingMistDuration.get();
	}

	/**
	 * Create potion effect.
	 * 
	 * @return potion effect
	 */
	MobEffectInstance createEffect() {
		return new MobEffectInstance(MobEffects.REGENERATION, duration);
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vec3 mistPos, LivingEntity invoker) {
		target.addEffect(createEffect());
	}

	@Override
	public int getEffectDuration() {
		return duration;
	}

	@Override
	public boolean isEffectAppliedToInvoker() {
		return true;
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
