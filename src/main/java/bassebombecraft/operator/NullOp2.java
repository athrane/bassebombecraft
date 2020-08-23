package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator2} interface which does nothing.
 */
public class NullOp2 implements Operator2 {
	
	@Override
	public Ports run(Ports ports) {
		return ports;
	}

}
