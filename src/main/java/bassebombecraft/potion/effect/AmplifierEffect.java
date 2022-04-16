package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.config.ModConfiguration.amplifierEffectUpdateFrequency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

/**
 * Effect which amplifies existing effects.
 */
public class AmplifierEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = AmplifierEffect.class.getSimpleName();

	/**
	 * AmplifierEffect constructor.
	 */
	public AmplifierEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// get active effects
		Collection<MobEffectInstance> effects = entity.getActiveEffects();

		// step 1: identify effects to be amplified
		List<MobEffectInstance> toBeAmplifiedEffects = new ArrayList<MobEffectInstance>();
		for (MobEffectInstance effectInstance : effects) {

			// skip replacement/amplification, if effect is amplifier effect
			MobEffect currentEffect = effectInstance.getEffect();
			if (this.equals(currentEffect)) {
				continue;
			}

			// if effect has amplifier => than this, then skip amplification
			if (amplifier > effectInstance.getAmplifier())
				toBeAmplifiedEffects.add(effectInstance);
		}

		// step 2: amplify identified effects
		for (MobEffectInstance currentEffect : toBeAmplifiedEffects) {

			// remove effect
			entity.removeEffect(currentEffect.getEffect());

			// create amplified effect
			MobEffectInstance amplifiedEffect = new MobEffectInstance(currentEffect.getEffect(), currentEffect.getDuration(),
					amplifier, currentEffect.isAmbient(), currentEffect.isVisible(),
					currentEffect.showIcon());

			// add amplified effect
			entity.addEffect(amplifiedEffect);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int updateFrequency = amplifierEffectUpdateFrequency.get();
		int moduloValue = duration % updateFrequency;
		return (moduloValue == 0);
	}

}
