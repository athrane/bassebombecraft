package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.isType;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the entity is of the expected type.
 */
public class IsEntityOfType2 implements Operator2 {

	/**
	 * Type to test for.
	 */
	Class<?> type;

	/**
	 * Constructor.
	 * 
	 * @param type type to test for.
	 */
	public IsEntityOfType2(Class<?> type) {
		this.type = type;
	}

	@Override
	public Ports run(Ports ports) {
		if (isType(ports.getLivingEntity1(), type))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
		return ports;
	}

}
