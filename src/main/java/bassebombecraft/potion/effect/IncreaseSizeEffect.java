package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;

import bassebombecraft.client.event.rendering.DecreaseSizeEffectRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;

/**
 * Effect which increase the size of the entity to approximately double size.
 * 
 * The logic of the effect is implemented in
 * {@linkplain DecreaseSizeEffectRenderer} which renders the entity in its
 * increased size.
 */
public class IncreaseSizeEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = IncreaseSizeEffect.class.getSimpleName();

	/**
	 * Constructor.
	 */
	public IncreaseSizeEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		// NO-OP
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

}
