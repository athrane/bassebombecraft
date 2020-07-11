package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator2} interface which executes two
 * embedded operators in sequence.
 */
public class Sequence2 implements Operator2 {

	/**
	 * Embedded operators.
	 */
	Operator2[] ops;

	/**
	 * Constructor.
	 * 
	 * @param ops embedded operators which are executed in sequence
	 */
	public Sequence2(Operator2... ops) {
		this.ops = ops;
	}

	@Override
	public Ports run(Ports ports) {
		Operators2.run(ports, ops);
		return ports;
	}

}
