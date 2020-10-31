package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator} interface which does nothing.
 */
@Deprecated
public class NullOp implements Operator {

	@Override
	public void run() {
		// NO-OP
	}

}
