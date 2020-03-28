package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator} interface which executes two
 * embedded operators in sequence.
 */
public class Sequence2 implements Operator {

	/**
	 * Embedded operator #1.
	 */
	Operator operator1;

	/**
	 * Embedded operator #2.
	 */
	Operator operator2;

	/**
	 * Constructor.
	 * 
	 * @param operator1 embedded operator which is executed first.
	 * @param operator2 embedded operator which is executed second.
	 */
	public Sequence2(Operator operator1, Operator operator2) {
		this.operator1 = operator1;
		this.operator2 = operator2;
	}

	@Override
	public void run() {
		operator1.run();
		operator2.run();
	}

}
