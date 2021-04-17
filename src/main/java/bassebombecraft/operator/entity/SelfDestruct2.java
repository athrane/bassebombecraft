package bassebombecraft.operator.entity;

import static bassebombecraft.entity.EntityUtils.selfDestruct;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which destroys entity.
 */
public class SelfDestruct2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SelfDestruct2.class.getSimpleName();

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Constructor.
	 * 
	 * @param fnGetTarget function to get target entity.
	 */
	public SelfDestruct2(Function<Ports, LivingEntity> fnGetTarget) {
		this.fnGetTarget = fnGetTarget;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as target from ports.
	 */
	public SelfDestruct2() {
		this(getFnGetLivingEntity1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity target = applyV(fnGetTarget, ports);

		selfDestruct(target);
	}

}
