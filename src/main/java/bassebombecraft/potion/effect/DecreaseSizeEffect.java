package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;

import bassebombecraft.client.event.rendering.DecreaseSizeEffectRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;

/**
 * Effect which decrease the size of the entity to approximately half size.
 * 
 * The logic of the effect is implemented in
 * {@linkplain DecreaseSizeEffectRenderer} which renders the entity in its
 * reduced size.
 */
public class DecreaseSizeEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = DecreaseSizeEffect.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public DecreaseSizeEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
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
