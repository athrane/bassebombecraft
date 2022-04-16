package bassebombecraft.operator.conditional;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static bassebombecraft.world.WorldUtils.isLogicalServer;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.LivingEntity;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the operator is executed at the logic server
 * side.
 */
public class IsWorldAtServerSide2 implements Operator2 {

	/**
	 * Function to get living entity.
	 */
	Function<Ports, LivingEntity> fnGetEntity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetEntity function to get living entity.
	 */
	public IsWorldAtServerSide2(Function<Ports, LivingEntity> fnGetEntity) {
		this.fnGetEntity = fnGetEntity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as entity from ports.
	 */
	public IsWorldAtServerSide2() {
		this(getFnGetLivingEntity1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity entity = applyV(fnGetEntity, ports);

		// exit as failed if not at server side
		if (!isLogicalServer(entity)) {
			ports.setResultAsFailed();
			return;
		}

		// set as successful
		ports.setResultAsSucces();
		return;
	}

}
