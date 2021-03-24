package bassebombecraft.operator.projectile.modifier.tag;

import bassebombecraft.event.projectile.ProjectileModifierEventHandler;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which serves to define
 * a tag for the {@linkplain ProjectileModifierEventHandler}.
 */
public class ReceiveAggro2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = ReceiveAggro2.class.getSimpleName();

	@Override
	public void run(Ports ports) {
		// NO-OP		
	}

}
