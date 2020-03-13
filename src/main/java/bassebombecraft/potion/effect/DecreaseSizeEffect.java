package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.Random;

import bassebombecraft.event.potion.DecreasedSizedEffectRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which decrease the size of the entity to approximately half size.
 * 
 * The logic of the effect is implemented in
 * {@linkplain DecreasedSizedEffectRenderer} which renders the entity in its
 * reduced size.
 */
public class DecreaseSizeEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = DecreaseSizeEffect.class.getSimpleName();

	/**
	 * Current size.
	 */
	float currentSize;

	/**
	 * Random.
	 */
	Random random;

	/**
	 * Constructor.
	 */
	public DecreaseSizeEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {
		currentSize = (float) amplifier / 100.0F;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	/**
	 * Return current size.
	 * 
	 * @return
	 */
	public float getSize() {
		return currentSize;
	}
}
