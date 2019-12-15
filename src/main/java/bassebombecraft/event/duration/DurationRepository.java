package bassebombecraft.event.duration;

/**
 * Interface for handling object which expired after a duration is reached.
 * Registered Objects are updated at fixed updates (ticks).
 */
public interface DurationRepository {

	/**
	 * Update registered durable objects.
	 */
	public void update();

	/**
	 * Register durable object.
	 * 
	 * @param id       id to register
	 * @param duration number updates until object is expired.
	 */
	public void add(String id, int duration);

	/**
	 * Returns true if object has expired.
	 * 
	 * @param id id of registered object
	 * 
	 * @return true if object has expired.
	 */
	public boolean isExpired(String id);

	/**
	 * Returns remaining duration for registered object. If object is expired then
	 * zero is returned.
	 * 
	 * @param id id of registered object.
	 * 
	 * @return remaining duration for registered object. If object is expired then
	 *         zero is returned.
	 */
	public int get(String id);

}
