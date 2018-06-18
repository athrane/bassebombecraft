package bassebombecraft.event.frequency;

/**
 * Interface for frequency which activates at fixed updates (ticks).
 */
public interface FrequencyRepository {

	/**
	 * Update registered frequencies.
	 */
	public void update();

	/**
	 * Register frequency
	 * 
	 * @param id
	 *            frequency id to register
	 * @param frequency
	 *            number updates between activation.
	 */
	public void add(String id, int frequency);

	/**
	 * Returns true if frequency is active at this time.
	 * 
	 * @param id
	 *            frequency id to query.
	 * 
	 * @return true if frequency is active at this time.
	 */
	public boolean isActive(String id);

}
