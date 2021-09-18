package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.operator.DefaultPorts.getBcSetEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator2} interface which adds a potion effect
 * to entity.
 * 
 * The added effect is write back to the ports.
 */
public class AddEffect2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AddEffect2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Function to set effect instance (at ports).
	 */
	BiConsumer<Ports, EffectInstance> bcSetEffectInstance;

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
	 * Constructor.
	 * 
	 * @param fnGetTarget         function to get target entity.
	 * @param bcSetEffectInstance function to set effect instance.
	 * @param effect              effect.
	 * @param duration            duration of the potion effect.
	 * @param amplifier           amplifier of the potion effect.
	 */
	public AddEffect2(Function<Ports, LivingEntity> fnGetTarget, BiConsumer<Ports, EffectInstance> bcSetEffectInstance,
			Effect effect, int duration, int amplifier) {
		this.fnGetTarget = fnGetTarget;
		this.bcSetEffectInstance = bcSetEffectInstance;
		this.effect = effect;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as target from ports.
	 * 
	 * Instance is configured to set created effect instance as effect instance #1
	 * in the ports.
	 * 
	 * @param effect    effect.
	 * @param duration  duration of the potion effect.
	 * @param amplifier amplifier of the potion effect.
	 */
	public AddEffect2(Effect effect, int duration, int amplifier) {
		this(getFnGetLivingEntity1(), getBcSetEffectInstance1(), effect, duration, amplifier);
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);

		// create effect instance (for outbound port)
		EffectInstance effectInstance = new EffectInstance(effect, duration, amplifier);

		// add effect
		target.addPotionEffect(effectInstance);

		// store effect instance
		bcSetEffectInstance.accept(ports, effectInstance);
	}

}
