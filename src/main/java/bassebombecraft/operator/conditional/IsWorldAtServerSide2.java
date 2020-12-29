package bassebombecraft.operator.conditional;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.world.WorldUtils.isLogicalServer;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;

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
	public Ports run(Ports ports) {

		// exit as failed if no living entity is defined
		LivingEntity livingEntity = fnGetEntity.apply(ports);
		if (livingEntity == null) {
			ports.setResultAsFailed();
			return ports;
		}

		// exit is failed if not at server side
		if (!isLogicalServer(ports.getLivingEntity1())) {
			ports.setResultAsFailed();
			return ports;
		}

		// set as successful
		ports.setResultAsSucces();
		return ports;
	}

}
