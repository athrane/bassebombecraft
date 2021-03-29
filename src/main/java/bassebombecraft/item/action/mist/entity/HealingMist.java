package bassebombecraft.item.action.mist.entity;

import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.healingMistDuration;
import static bassebombecraft.config.ModConfiguration.healingMistParticleInfo;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;

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
	EffectInstance createEffect() {
		return new EffectInstance(Effects.REGENERATION, duration);
	}

	@Override
	public void applyEffectToEntity(LivingEntity target, Vector3d mistPos, LivingEntity invoker) {
		target.addPotionEffect(createEffect());
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
