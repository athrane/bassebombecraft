package bassebombecraft.client.operator;

import static bassebombecraft.operator.Operators2.validateNotNull;

import java.util.function.Function;

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
		T retVal = fn.apply((ClientPorts) ports);
		validateNotNull(retVal, fn);
		return retVal;
	}
}
