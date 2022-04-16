package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;

import bassebombecraft.event.potion.ReflectEffectEventHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;

/**
 * Effect which reflect damage inflicted to the entity with the effect active.
 * 
 * The logic of the effect is implemented in the
 * {@linkplain ReflectEffectEventHandler} which is invoked when damage is
 * inflicted to an entity.
 * 
 */
public class ReflectEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = ReflectEffect.class.getSimpleName();

	/**
	 * ReflectEffect constructor.
	 */
	public ReflectEffect() {
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
