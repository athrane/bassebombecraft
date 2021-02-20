package bassebombecraft.operator;

import java.util.Arrays;

/**
 * Implementation of the {@linkplain Operator2} interface which executes a list
 * of embedded operators in sequence.
 * 
 * The {@linkplain Ports} object used to invoke the operator is also used to
 * invoke the embedded operators in the sequence.
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
	public void run(Ports ports) {
		Operators2.run(ports, operators);
	}

	@Override
	public String toString() {
		return "Sequence2 [operator=" + Arrays.toString(operators) + "]";
	}

}
