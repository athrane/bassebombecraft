package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Effect which amplifies existing effects.
 */
public class AmplifierEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = AmplifierEffect.class.getSimpleName();
	
	/**
	 * Update frequency for effect.
	 */
	static final int UPDATE_FREQUENCY = 10;

	/**
	 * AmplifierEffect constructor.
	 */
	public AmplifierEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// get active effects
		Collection<EffectInstance> effects = entity.getActivePotionEffects();
		
		// identify effects to be amplified 
		List<EffectInstance> toBeAmplifiedEffects = new ArrayList<EffectInstance>();
		for (EffectInstance effectInstance : effects) {		
			
			// skip replacement/amplification, if effect is amplifier effect
			Effect currentEffect = effectInstance.getPotion();
			if(this.equals(currentEffect)) {
				continue;
			}
			
			// if effect has amplifier => than this, then skip amplification
			if(amplifier > effectInstance.getAmplifier()) 
				toBeAmplifiedEffects.add(effectInstance);
		}

		// amplify identified effects
		for(EffectInstance effectInstance : toBeAmplifiedEffects) {
			
			// remove effect
			entity.removePotionEffect(effectInstance.getPotion());
			
			// create amplified effect 
			EffectInstance amplifiedEffect = new EffectInstance(effectInstance.getPotion(), effectInstance.getDuration(), amplifier,
					effectInstance.isAmbient(), effectInstance.doesShowParticles(), effectInstance.isShowIcon());

			// add amplified effect
			entity.addPotionEffect(amplifiedEffect);
		}				
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int moduloValue = duration % UPDATE_FREQUENCY; 
		return (moduloValue == 0);
	}

}