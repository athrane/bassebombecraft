package bassebombecraft.operator.conditional;

import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if frequency is active at the server frequency
 * repository.
 */
public class IsFrequencyActive2 implements Operator2 {

	/**
	 * Function to get frequency to test for.
	 */
	Function<Ports, Integer> fnGetFrequency;

	/**
	 * Constructor.
	 * 
	 * @param fnGetFrequency function to get frequency to test for.
	 */
	public IsFrequencyActive2(Function<Ports, Integer> fnGetFrequency) {
		this.fnGetFrequency = fnGetFrequency;
	}

	@Override
	public void run(Ports ports) {
		Integer frequency = fnGetFrequency.apply(ports);

		if (getProxy().getServerFrequencyRepository().isActive(frequency))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
	}

}
