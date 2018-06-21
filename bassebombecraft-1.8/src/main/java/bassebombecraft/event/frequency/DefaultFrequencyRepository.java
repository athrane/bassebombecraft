package bassebombecraft.event.frequency;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the {@linkplain FrequencyRepository} interface.
 */
public class DefaultFrequencyRepository implements FrequencyRepository {

	/**
	 * Frequency state.
	 */
	class FrequencyState {
		int frequency;
		String id;
	}

	/**
	 * Running counter.
	 */
	int counter = 0;

	/**
	 * Registered frequencies
	 */
	ConcurrentHashMap<String, FrequencyState> frequencies = new ConcurrentHashMap<String, FrequencyState>();

	@Override
	public void update() {
		counter++;
	}

	@Override
	public void add(String id, int frequency) {
		if (frequencies.containsKey(id))
			return;

		// add new frequency
		FrequencyState freq = new FrequencyState();
		freq.id = id;
		freq.frequency = frequency;
		frequencies.put(id, freq);
	}

	@Override
	public boolean isActive(String id) {
		if (!frequencies.containsKey(id))
			return false;

		// calculate if active
		FrequencyState freq = frequencies.get(id);
		int value = counter % freq.frequency;
		return (value == 0);
	}

	@Override
	public boolean isActive(int frequency) {
		int value = counter % frequency;
		return (value == 0);
	}
	
	/**
	 * Factory method.
	 * 
	 * @return frequency instance.
	 */
	public static FrequencyRepository getInstance() {
		return new DefaultFrequencyRepository();
	}

}
