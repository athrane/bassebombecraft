package bassebombecraft.operator.counter;

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
public class SingleLoopIncreasingCounter2 implements Operator2 {

	/**
	 * Max value for counter.
	 */
	int max;

	/**
	 * Constructor.
	 * 
	 * @param max max value.
	 */
	public SingleLoopIncreasingCounter2(int max) {
		this.max = max;
	}

	@Override
	public Ports run(Ports ports) {
		if (ports.getCounter() < max) {
			ports.incrementCounter();
			ports.setResultAsSucces();

		} else
			ports.setResultAsFailed();

		return ports;
	}
}
