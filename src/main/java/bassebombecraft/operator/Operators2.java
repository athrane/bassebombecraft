package bassebombecraft.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.function.Function;

/**
 * Class for execution of operator.
 */
public class Operators2 {

	/**
	 * Execute operator.
	 * 
	 * @param ports   input ports
	 * @param opertor operator to execute.
	 */
	public static void run(Ports ports, Operator2 operator) {
		try {
			operator.run(ports);
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Execute operators.
	 * 
	 * Operators are executed in sequence.
	 * 
	 * If an executed operator set the result port to failed (i.e. false) then the
	 * execution of the sequence is aborted.
	 * 
	 * @param ports   input ports
	 * @param opertor operators executed in sequence.
	 */
	public static void run(Ports ports, Operator2... operators) {
		for (Operator2 op : operators) {
			run(ports, op);
			if (!ports.getResult())
				break;
		}
	}

	/**
	 * Validates that object isn't null. Intended to be use to value operator input
	 * parameter: <blockquote>
	 * 
	 * <pre>
	 * Entity source = fnGetSource.apply(ports);
	 * Operators2.validateNotNull(source);
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @param obj the object reference to check for nullity
	 * @param <T> the type of the reference
	 * @return {@code obj} if not {@code null}
	 * 
	 * @throws UndefinedOperatorInputException if {@code obj} is {@code null}
	 */
	static <T> T validateNotNull(T obj, Function<Ports, T> fn) {
		if (obj == null)
			throw new UndefinedOperatorInputException(fn.toString());
		return obj;
	}

	/**
	 * Apply function and validate returned values isn't null.
	 * 
	 * @param <T>   the type of the return value of the function.
	 * @param fn    function which is applied.
	 * @param ports ports which is used as input to the function.
	 * 
	 * @return return value of the applied function
	 * 
	 * @throws UndefinedOperatorInputException if function returns null.
	 */
	public static <T> T applyV(Function<Ports, T> fn, Ports ports) {
		T retVal = fn.apply(ports);
		validateNotNull(retVal, fn);
		return retVal;
	}

}
