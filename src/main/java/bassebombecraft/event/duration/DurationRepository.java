package bassebombecraft.event.duration;

import java.util.function.Consumer;

import bassebombecraft.proxy.Proxy;

/**
 * Interface for handling object which expired after a duration is reached.
 * Registered Objects are updated at fixed updates (ticks).
 * 
 * The repository is used at both CLIENT and SERVER side. Access to the
 * repository is supported via sided proxy, i.e.{@linkplain Proxy}.
 */
public interface DurationRepository {

	/**
	 * Update registered durable objects. A durable object is removed if it has
	 * expired.
	 * 
	 * A durable object registered with a positive value is decremented with 1 on
	 * every update. When a durable object reaches zero then the duration is expired
	 * and removed from the repository.
	 * 
	 * A durable object registered with an initial value of -1 will never expired or
	 * be decremented.
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
	 * Register durable object.
	 * 
	 * @param id               id to register
	 * @param duration         number updates until object is expired.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	public void add(String id, int duration, Consumer<String> cRemovalCallback);

	/**
	 * Register durable object.
	 * 
	 * @param id               id to register
	 * @param duration         number updates until object is expired.
	 * @param cUpdateCallback  callback function used by the duration repository to
	 *                         notify a listener that the duration has be updated.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	public void add(String id, int duration, Consumer<String> cUpdateCallback, Consumer<String> cRemovalCallback);

	/**
	 * Remove durable object.
	 * 
	 * @param id id to remove.
	 */
	public void remove(String id);

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
