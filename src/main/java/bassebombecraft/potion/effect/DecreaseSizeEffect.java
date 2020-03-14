package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;
import static bassebombecraft.rendering.RenderingUtils.oscillate;

import java.util.Random;

import bassebombecraft.event.potion.DecreaseSizedEffectRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which decrease the size of the entity to approximately half size.
 * 
 * The logic of the effect is implemented in
 * {@linkplain DecreaseSizedEffectRenderer} which renders the entity in its
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
		float scaledSize = (float) amplifier / 100.0F;
		float scaledSizeFraction = scaledSize * 0.25F;
		float sizeVariation = (float) oscillate(entity.hashCode(), 0, scaledSizeFraction);
		currentSize = scaledSize + sizeVariation;
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
