package bassebombecraft.operator;

/**
 * Interface for operators.
 * 
 * @deprecated Version 1 of the operator framework should be replaced with
 *             version 2, {@linkplain Operator2}.
 */
@Deprecated
public interface Operator {

	/**
	 * Execute operator.
	 */
	public void run();
}
