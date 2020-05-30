package bassebombecraft.event.duration;

/**
 * Interface for durable object in the {@linkplain DurationRepository}.
 */
public interface Duration {

	/**
	 * Update duration state.
	 */
	public void update();

	/**
	 * Returns true if duration has expired.
	 * 
	 * A durable object registered with a positive value is decremented with 1 on
	 * every update. When a durable object reaches zero then the duration is
	 * considered expired.
	 * 
	 * @return true if duration has expired
	 */
	public boolean isExpired();

	/**
	 * Returns true if duration never expires.
	 * 
	 * A durable object registered with an initial value of -1 will never expired or
	 * be decremented.
	 * 
	 * @return true if duration has expired
	 */
	public boolean neverExpires();

	/**
	 * Get duration ID.
	 * 
	 * @return id.
	 */
	public String getId();
	
	/**
	 * Get remaining duration.
	 * 
	 * @return remaining duration.
	 */
	public int get();

	/**
	 * Invoke callback (if defined).
	 */
	public void notifyOfExpiry();

}
