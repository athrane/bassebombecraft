package bassebombecraft.client.operator;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.MOD_PKG_NAME;
import static bassebombecraft.operator.Operators2.*;

import java.util.Arrays;
import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import bassebombecraft.operator.Ports;
import bassebombecraft.operator.UndefinedOperatorInputException;

/**
 * Class for execution of operator.
 */
public class ClientOperators2 {

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
	public static <T> T clientApplyV(Function<ClientPorts, T> fn, Ports ports) {
		if (ports.isDebugEnabled())
			logInvocation(fn, ports);		
		T retVal = fn.apply((ClientPorts) ports);
		validateNotNull(retVal, fn, ports);
		return retVal;
	}
	
	/**
	 * Logs method prior to invocation.
	 * 
	 * @param <T> return type for invoked function.
	 * @param fn invoked function.
	 * @param ports ports object used to invoke function.
	 */
	static <T> void logInvocation(Function<ClientPorts, T> fn, Ports ports) {
		Logger logger = getBassebombeCraft().getLogger();
		logger.debug("ClientOperators2.clientApplyV(..) PRE invocation debugging info:");

		// log stack trace
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		Arrays.asList(ste).stream().filter(e -> e.getClassName().contains(MOD_PKG_NAME)).forEach(logger::debug);

		// log ports
		logger.debug(createDebugInfo("debug", ports));
	}
	
}
