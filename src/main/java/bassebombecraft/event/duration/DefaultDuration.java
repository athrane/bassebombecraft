package bassebombecraft.event.duration;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implementation of the {@linkplain Duration} interface.
 */
public class DefaultDuration implements Duration {
	
	/**
	 * Is expired.
	 */
	static final int IS_EXPIRED = 0;

	/**
	 * Duration in ticks.
	 */
	int duration;

	/**
	 * Id of duration.
	 */
	String id;

	/**
	 * Callback function used by the duration repository to notify a listener that
	 * the duration has expired.
	 */
	Optional<Consumer<String>> optRemovalCallback;

	/**
	 * Callback function used by the duration repository to notify a listener that
	 * the duration has been updated.
	 */
	Optional<Consumer<String>> optUpdateCallback;
	
	/**
	 * Constructor.
	 * 
	 * @param duration duration of duration. -1 will result in non expiring
	 *                 duration.
	 * @param id       id of the duration.
	 */
	DefaultDuration(int duration, String id) {
		super();
		this.duration = duration;
		this.id = id;
		this.optUpdateCallback = empty();				
		this.optRemovalCallback = empty();
	}

	/**
	 * Constructor.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	DefaultDuration(int duration, String id, Consumer<String> cRemovalCallback) {
		super();
		this.duration = duration;
		this.id = id;
		this.optUpdateCallback = empty();		
		this.optRemovalCallback = of(cRemovalCallback);
	}

	/**
	 * Constructor.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 * @param cUpdateCallback  callback function used by the duration repository to
	 *                         notify a listener that the duration has be updated.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	DefaultDuration(int duration, String id, Consumer<String> cUpdateCallback, Consumer<String> cRemovalCallback) {
		super();
		this.duration = duration;
		this.id = id;
		this.optUpdateCallback = of(cUpdateCallback);		
		this.optRemovalCallback = of(cRemovalCallback);
	}
	
	@Override
	public void update() {
		if (isExpired())
			return;

		// update of present
		if (optUpdateCallback.isPresent()) {
			Consumer<String> cUpdateCallback = optUpdateCallback.get();
			cUpdateCallback.accept(id);			
		}
				
		// don't decrement a non expiring duration.
		if (neverExpires())
			return;

		// decrement
		duration = duration - 1;
	}

	@Override
	public boolean isExpired() {
		return (duration == IS_EXPIRED);
	}

	@Override
	public boolean neverExpires() {
		return (duration == NO_EXPIRY);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int get() {
		return duration;
	}

	@Override
	public void notifyOfExpiry() {
		if (!optRemovalCallback.isPresent())
			return;
		Consumer<String> cRemovalCallback = optRemovalCallback.get();
		cRemovalCallback.accept(id);
	}

	/**
	 * Factory method.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 */
	public static Duration getInstance(int duration, String id) {
		return new DefaultDuration(duration, id);
	}

	/**
	 * Factory method.
	 * 
	 * @param duration         duration of duration.
	 * @param id               id of the duration.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	public static Duration getInstance(int duration, String id, Consumer<String> cRemovalCallback) {
		return new DefaultDuration(duration, id, cRemovalCallback);
	}

	/**
	 * Factory method.
	 * 
	 * @param duration         duration of duration.
	 * @param id               id of the duration.
	 * @param cUpdateCallback  callback function used by the duration repository to
	 *                         notify a listener that the duration has be updated.
	 * @param cRemovalCallback callback function used by the duration repository to
	 *                         notify a listener that the duration has expired.
	 */
	public static Duration getInstance(int duration, String id, Consumer<String> cUpdateCallback,
			Consumer<String> cRemovalCallback) {
		return new DefaultDuration(duration, id, cUpdateCallback, cRemovalCallback);
	}

}
