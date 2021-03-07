package bassebombecraft.operator.conditional;

import static bassebombecraft.BassebombeCraft.getProxy;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if frequency is active at the server frequency
 * repository.
 */
public class IsFrequencyIsActive2 implements Operator2 {

	/**
	 * Frequency to test for.
	 */
	int frequency;

	/**
	 * Constructor.
	 * 
	 * @param frequency frequency to test for.
	 */
	public IsFrequencyIsActive2(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public void run(Ports ports) {
		if (getProxy().getServerFrequencyRepository().isActive(frequency))
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
	}

}
