package bassebombecraft.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MOD_PKG_NAME;

import static java.util.Arrays.*;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.logging.log4j.Logger;

import net.minecraft.entity.LivingEntity;

/**
 * Class for execution of operator.
 */
public class Operators2 {

	static final String OPERATORS2_CLASS = "bassebombecraft.operator.Operators2";

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
	 * @param obj   the object reference to check for nullity
	 * @param <T>   the type of the reference
	 * @param ports the ports
	 * 
	 * @return {@code obj} if not {@code null}
	 * 
	 * @throws UndefinedOperatorInputException if {@code obj} is {@code null}
	 */
	public static <T> T validateNotNull(T obj, Function<? extends Ports, T> fn, Ports ports) {
		if (obj == null) {

			// create message
			StringBuilder builder = new StringBuilder();
			builder.append("Validation failed for function: ");
			builder.append(fn.toString());
			builder.append(" ");
			builder.append(ports.toString());
			throw new UndefinedOperatorInputException(builder.toString());
		}
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
		if (ports.isDebugEnabled())
			logInvocation(fn, ports);
		T retVal = fn.apply(ports);
		validateNotNull(retVal, fn, ports);
		return retVal;
	}

	/**
	 * Logs method prior to invocation.
	 * 
	 * @param <T>   return type for invoked function.
	 * @param fn    invoked function.
	 * @param ports ports object used to invoke function.
	 */
	public static <T> void logInvocation(Function<? extends Ports, T> fn, Ports ports) {
		Logger logger = getBassebombeCraft().getLogger();
		logger.debug("Operator call sequence:");

		// log invoked operators
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		asList(ste).stream().filter(e -> e.getClassName().contains(MOD_PKG_NAME))
				.filter(e -> !e.getClassName().contains(OPERATORS2_CLASS)).forEach(logger::debug);

		// log ports
		logger.debug(ports.toString());
	}
}
