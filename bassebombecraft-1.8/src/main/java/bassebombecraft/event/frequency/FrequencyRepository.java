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
	 * Returns true if frequency is active at this time.
	 * 
	 * @param id
	 *            frequency to query.
	 * 
	 * @return true if frequency is active at this time.
	 */	
	public boolean isActive(int frequency);

}
