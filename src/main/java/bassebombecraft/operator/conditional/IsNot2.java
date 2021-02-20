package bassebombecraft.operator.conditional;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port with the negated result of the embedded operator.
 * 
 * The {@linkplain Ports} object used to invoke the operator is also used to
 * invoke the embedded operator.
 */
public class IsNot2 implements Operator2 {

	/**
	 * Embedded operator.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param operator embedded operator to execute.
	 */
	public IsNot2(Operator2 operator) {
		this.operator = operator;
	}

	@Override
	public void run(Ports ports) {
		Operators2.run(ports, operator);

		// negate result
		if (ports.getResult()) {
			ports.setResultAsFailed();
			return;
		}

		ports.setResultAsSucces();
	}
}
