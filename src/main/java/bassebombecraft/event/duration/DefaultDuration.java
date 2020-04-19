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
	Optional<Consumer<String>> optConsumerId;

	/**
	 * Constructor.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 */
	DefaultDuration(int duration, String id) {
		super();
		this.duration = duration;
		this.id = id;
		this.optConsumerId = empty();
	}

	/**
	 * Constructor.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 * @param cId      callback function used by the duration repository to notify a
	 *                 listener that the duration has expired.
	 */
	DefaultDuration(int duration, String id, Consumer<String> cId) {
		super();
		this.duration = duration;
		this.id = id;
		this.optConsumerId = of(cId);
	}

	@Override
	public void update() {
		if (isExpired())
			return;
		duration = duration - 1;
	}

	@Override
	public boolean isExpired() {
		return (duration == 0);
	}

	@Override
	public int get() {
		return duration;
	}

	@Override
	public void accept() {
		if (!optConsumerId.isPresent())
			return;
		Consumer<String> cId = optConsumerId.get();
		cId.accept(id);
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
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 * @param cId      callback function used by the duration repository to notify a
	 *                 listener that the duration has expired.
	 */
	public static Duration getInstance(int duration, String id, Consumer<String> cId) {
		return new DefaultDuration(duration, id, cId);
	}
}
