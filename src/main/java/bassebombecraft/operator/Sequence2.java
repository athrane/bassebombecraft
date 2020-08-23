package bassebombecraft.operator;

import java.util.Arrays;

/**
 * Implementation of the {@linkplain Operator2} interface which executes two
 * embedded operators in sequence.
 */
public class Sequence2 implements Operator2 {

	/**
	 * Embedded operators.
	 */
	Operator2[] operators;

	/**
	 * Constructor.
	 * 
	 * @param operators embedded operators which are executed in sequence
	 */
	public Sequence2(Operator2... operators) {
		this.operators = operators;
	}

	@Override
	public Ports run(Ports ports) {
		Operators2.run(ports, operators);
		return ports;
	}

	@Override
	public String toString() {
		return "Sequence2 [operator=" + Arrays.toString(operators) + "]";
	}
	
}
