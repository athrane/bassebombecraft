package bassebombecraft.operator.entity.potion.effect;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator} interface which adds an effect to
 * entity.
 * 
 * @deprecated Use {@linkplain AddEffect2} instead.
 */
@Deprecated
public class AddEffect implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddEffect.class.getSimpleName();

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Effect amplifier.
	 */
	int amplifier;

	/**
	 * Effect.
	 */
	Effect effect;

	/**
	 * Effect instance (for outbound port).
	 */
	EffectInstance effectInstance;

	/**
	 * {@linkplain EffectInstance} supplier.
	 */
	Supplier<EffectInstance> splEffectInstance = () -> effectInstance;

	/**
	 * Constructor.
	 * 
	 * @param splEntity    entity supplier.
	 * @param effect       effect.
	 * @param splDuration  duration as a potion effect.
	 * @param splAmplifier amplifier as a potion effect.
	 */
	public AddEffect(Supplier<LivingEntity> splEntity, Effect effect, IntSupplier splDuration,
			IntSupplier splAmplifier) {
		this.splEntity = splEntity;
		this.duration = splDuration.getAsInt();
		this.amplifier = splAmplifier.getAsInt();
		this.effect = effect;
	}

	/**
	 * Get {@linkplain EffectInstance} supplier.
	 * 
	 * Defines an outbound port.
	 * 
	 * @return effect supplier.
	 */
	public Supplier<EffectInstance> getSplEffectInstance() {
		return splEffectInstance;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity entity = splEntity.get();

		// create effect instance (for outbound port)
		effectInstance = new EffectInstance(effect, duration, amplifier);

		// add effect
		entity.addPotionEffect(effectInstance);
	}

}
