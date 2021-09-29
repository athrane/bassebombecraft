package bassebombecraft.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MOD_PKG_NAME;

import java.util.Arrays;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

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
			builder.append(createDebugInfo("the function", ports));

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
	 * Create string with ports debug info.
	 * 
	 * @header debug header
	 * @param ports ports to create debug info from.
	 * 
	 * @return string with ports debug info
	 */
	public static String createDebugInfo(String header, Ports ports) {
		StringBuilder builder = new StringBuilder();
		builder.append("Ports for " + header);
		builder.append(", Result=" + ports.getResult());
		builder.append(", Counter=" + ports.getCounter());
		builder.append(", Integer1=" + ports.getInteger1());
		if (ports.getString1() != null)
			builder.append(", String1=" + ports.getString1());
		if (ports.getString2() != null)
			builder.append(", String2=" + ports.getString2());
		if (ports.getDouble1() != null)
			builder.append(", Double1=" + ports.getDouble1());
		if (ports.getAabb1() != null)
			builder.append(", Aabb1=" + ports.getAabb1());
		if (ports.getBlockPosition1() != null)
			builder.append(", BlockPosition1=" + ports.getBlockPosition1());
		if (ports.getBlockPosition2() != null)
			builder.append(", BlockPosition2=" + ports.getBlockPosition2());
		if (ports.getColor4f1() != null)
			builder.append(", Color4f1=" + ports.getColor4f1());
		if (ports.getColor4f2() != null)
			builder.append(", Color4f2=" + ports.getColor4f2());
		if (ports.getDamageSource1() != null)
			builder.append(", DamageSource1=" + ports.getDamageSource1());
		if (ports.getEffectInstance1() != null)
			builder.append(", EffectInstance1=" + ports.getEffectInstance1());
		if (ports.getEntity1() != null)
			builder.append(", Entity1=" + ports.getEntity1());
		if (ports.getEntity2() != null)
			builder.append(", Entity2=" + ports.getEntity2());
		if (ports.getEntities1() != null)
			builder.append(", Entities1=" + Arrays.toString(ports.getEntities1()));
		if (ports.getLivingEntity1() != null)
			builder.append(", LivingEntity1=" + ports.getLivingEntity1());
		if (ports.getLivingEntity2() != null)
			builder.append(", LivingEntity2=" + ports.getLivingEntity2());
		if (ports.getEntities1() != null)
			builder.append(", LivingEntities1=" + Arrays.toString(ports.getLivingEntities1()));
		if (ports.getVector1() != null)
			builder.append(", Vector1=" + ports.getVector1());
		if (ports.getVectors1() != null)
			builder.append(", Vectors1=" + Arrays.toString(ports.getVectors1()));
		if (ports.getVectors2() != null)
			builder.append(", Vectors2=" + Arrays.toString(ports.getVectors2()));

		return builder.toString();
	}

	/**
	 * Logs method prior to invocation.
	 * 
	 * @param <T> return type for invoked function.
	 * @param fn invoked function.
	 * @param ports ports object used to invoke function.
	 */
	static <T> void logInvocation(Function<Ports, T> fn, Ports ports) {
		Logger logger = getBassebombeCraft().getLogger();
		logger.debug("Operators2.Apply(..) PRE invocation debugging info:");

		// log stack trace
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		Arrays.asList(ste).stream().filter(e -> e.getClassName().contains(MOD_PKG_NAME)).forEach(logger::debug);

		// log ports
		logger.debug(createDebugInfo("debug", ports));
	}

}
