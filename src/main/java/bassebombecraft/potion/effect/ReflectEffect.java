package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import bassebombecraft.event.potion.ReflectEffectEventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which reflect damage inflicted to the entity with the effect active.
 * 
 * The logic of the effect is implemented in the
 * {@linkplain ReflectEffectEventHandler} which is invoked when damage is
 * inflicted to an entity.
 * 
 */
public class ReflectEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = ReflectEffect.class.getSimpleName();

	/**
	 * RevengeEffect constructor.
	 */
	public ReflectEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		// NO-OP
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
