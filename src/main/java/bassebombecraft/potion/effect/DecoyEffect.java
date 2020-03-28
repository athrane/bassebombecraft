package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import bassebombecraft.event.potion.DecoyEffectRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which turns entity into a decoy.
 * 
 * The logic of the effect is implemented in {@linkplain DecoyEffectRenderer}
 * which renders the entity as an 2D entity.
 */
public class DecoyEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = DecoyEffect.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public DecoyEffect() {
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
