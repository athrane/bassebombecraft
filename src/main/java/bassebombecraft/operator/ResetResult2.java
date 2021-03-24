package bassebombecraft.operator;

/**
 * Implementation of the {@linkplain Operator2} interface which resets the
 * result state in the ports.
 * 
 * Useful for resetting the result state for conditional execution of operators
 * in a sequence where the ports are reused across invocations.
 */
public class ResetResult2 implements Operator2 {

	@Override
	public void run(Ports ports) {
		ports.setResultAsSucces();
	}
}
