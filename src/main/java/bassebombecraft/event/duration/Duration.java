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
	 * @return true if duration has expired
	 */
	public boolean isExpired();

	/**
	 * Get remaining duration.
	 * 
	 * @return remaining duration.
	 */
	public int get();
}
