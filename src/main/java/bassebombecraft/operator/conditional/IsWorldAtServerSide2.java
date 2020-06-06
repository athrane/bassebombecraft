package bassebombecraft.operator.conditional;

import static bassebombecraft.world.WorldUtils.isLogicalServer;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the operator is executed at the logic server
 * side.
 */
public class IsWorldAtServerSide2 implements Operator2 {

	@Override
	public Ports run(Ports ports) {
		if (isLogicalServer(ports.getLivingEntity()))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();

		return ports;
	}

}
