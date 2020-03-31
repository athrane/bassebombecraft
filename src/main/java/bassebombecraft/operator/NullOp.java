package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator} interface which does nothing.
 */
public class NullOp implements Operator {

	/**
	 * NullOp constructor.
	 */
	public NullOp() {
	}

	@Override
	public void run() {
		// NO-OP
	}

}
