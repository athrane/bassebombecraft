package bassebombecraft.event.frequency;

import bassebombecraft.proxy.Proxy;

/**
 * Interface for frequency repository which activates at fixed updates (ticks).
 * 
 * The repository is used at both CLIENT and SERVER side. Access to the
 * repository is supported via sided proxy, i.e.{@linkplain Proxy}.
 */
public interface FrequencyRepository {

	/**
	 * Update registered frequencies.
	 */
	public void update();

	/**
	 * Register frequency
	 * 
	 * @param id        frequency id to register
	 * @param frequency number updates between activation.
	 */
	public void add(String id, int frequency);

	/**
	 * Returns true if frequency is active at this time.
	 * 
	 * @param id frequency to query.
	 * 
	 * @return true if frequency is active at this time.
	 */
	public boolean isActive(String id);

	/**
	 * Returns true if frequency is active at this time.
	 * 
	 * @param id frequency to query.
	 * 
	 * @return true if frequency is active at this time.
	 */
	public boolean isActive(int frequency);

}
