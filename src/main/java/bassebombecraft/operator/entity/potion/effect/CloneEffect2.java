package bassebombecraft.operator.entity.potion.effect;

import static bassebombecraft.operator.DefaultPorts.getFnEffectInstance1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

/**
 * Implementation of the {@linkplain Operator2} interface which clones a potion
 * effect instance to target entity.
 */
public class CloneEffect2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = CloneEffect2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Function to get effect instance.
	 */
	Function<Ports, EffectInstance> fnGetEffectInstance;

	/**
	 * Constructor.
	 * 
	 * @param fnGetTarget         function to get target entity.
	 * @param fnGetEffectInstance function to get effect instance.
	 */
	public CloneEffect2(Function<Ports, LivingEntity> fnGetTarget,
			Function<Ports, EffectInstance> fnGetEffectInstance) {
		this.fnGetTarget = fnGetTarget;
		this.fnGetEffectInstance = fnGetEffectInstance;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as target from ports.
	 * 
	 * Instance is configured to get effect instance from effect instance #1 in the
	 * ports.
	 */
	public CloneEffect2(Effect effect, int duration, int amplifier) {
		this(getFnGetLivingEntity1(), getFnEffectInstance1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);
		EffectInstance sourceInstance = applyV(fnGetEffectInstance, ports);

		// create effect instance
		EffectInstance effectInstance = new EffectInstance(sourceInstance);

		// add effect
		target.addPotionEffect(effectInstance);
	}

}
