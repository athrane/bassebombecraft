package bassebombecraft.event.frequency;

/**
 * Default implementation of the {@linkplain FrequencyRepository} interface.
 */
public class DefaultFrequencyRepository implements FrequencyRepository {

	/**
	 * Running counter.
	 */
	int counter = 0;

	@Override
	public void update() {
		counter++;
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
