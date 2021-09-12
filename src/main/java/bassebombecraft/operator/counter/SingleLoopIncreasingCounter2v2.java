package bassebombecraft.operator.counter;

import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import static bassebombecraft.operator.DefaultPorts.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which implements a
 * counter.
 * 
 * The counter is increasing to and including the max value. When the max value
 * is reached then it isn't increased further.
 * 
 * If the counter hasn't reached the max value, the result port is updated as
 * successful to support conditional execution of other sequential operators.
 * 
 * If the counter has reached the max value, the result port is updated as
 * unsuccessful.
 * 
 * The state of the counter is maintained in the {@linkplain Ports} using its
 * counter.
 */
public class SingleLoopIncreasingCounter2v2 implements Operator2 {

	/**
	 * Function to get max value.
	 */
	Function<Ports, Integer> fnGetMax;

	/**
	 * Constructor.
	 * 
	 * @param fnGetMax function to get max value.
	 */
	public SingleLoopIncreasingCounter2v2(Function<Ports, Integer> fnGetMax) {
		this.fnGetMax = fnGetMax;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with integer #1 from ports.
	 */
	public SingleLoopIncreasingCounter2v2() {
		this(getFnGetInteger1());
	}

	@Override
	public void run(Ports ports) {
		int max = applyV(fnGetMax, ports);

		if (ports.getCounter() < max) {
			ports.incrementCounter();
			ports.setResultAsSucces();

		} else
			ports.setResultAsFailed();
	}
}
