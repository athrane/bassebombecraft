package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import bassebombecraft.event.potion.DecreaseSizedEffectRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which increase the size of the entity to approximately double size.
 * 
 * Size is dependent by amplifier.
 * 
 * The logic of the effect is implemented in {@linkplain DecreaseSizedEffectRenderer}
 * which renders the entity in its increased size.
 */
public class IncreaseSizeEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = IncreaseSizeEffect.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public IncreaseSizeEffect() {
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
