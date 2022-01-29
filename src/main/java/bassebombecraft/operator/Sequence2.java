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
	 * Default behaviour for resetting the result state prior to execution.
	 */
	public static final boolean DONT_RESET = false;

	/**
	 * Default behaviour for resetting the result state prior to execution.
	 */
	public static final boolean RESET = false;

	/**
	 * Strategy for resetting the result state.
	 */
	static final Operator2 RESET_STRATEGY = new ResetResult2();

	/**
	 * Strategy for not resetting the result state.
	 */
	static final Operator2 DONT_RESET_STRATEGY = new NullOp2();;

	/**
	 * Embedded operators.
	 */
	Operator2[] operators;

	/**
	 * Reset operator.
	 */
	Operator2 resetOp;

	/**
	 * Constructor.
	 * 
	 * @param reset     if value is true then the result state in the ports will be
	 *                  reset before execution of the operators in the sequence.
	 *                  Useful for resetting the result state for conditional
	 *                  execution of operators in a sequence where the ports are
	 *                  reused across invocations.
	 * @param operators embedded operators which are executed in sequence
	 */
	public Sequence2(boolean reset, Operator2... operators) {
		resetOp = createResetStrategy(reset);
		this.operators = operators;
	}

	/**
	 * Constructor.
	 * 
	 * @param operators embedded operators which are executed in sequence
	 */
	public Sequence2(Operator2... operators) {
		this(DONT_RESET, operators);
	}

	@Override
	public void run(Ports ports) {
		Operators2.run(ports, resetOp);
		Operators2.run(ports, operators);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("[operators=");
		builder.append(Arrays.toString(operators));
		builder.append("]");		
		return builder.toString();
	}

	/**
	 * Create reset strategy.
	 * 
	 * @param reset state to create strategy from.
	 * 
	 * @return reset strategy.
	 */
	Operator2 createResetStrategy(boolean reset) {
		if (reset)
			return RESET_STRATEGY;
		return DONT_RESET_STRATEGY;
	}

}
