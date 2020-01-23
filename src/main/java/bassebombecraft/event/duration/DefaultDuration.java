package bassebombecraft.event.duration;

/**
 * Implementation of the {@linkplain Duration} interface.
 */
public class DefaultDuration implements Duration {

	int duration;
	String id;

	/**
	 * DefaultDuration constructor.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 */
	DefaultDuration(int duration, String id) {
		super();
		this.duration = duration;
		this.id = id;
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

	/**
	 * Factory method.
	 * 
	 * @param duration duration of duration.
	 * @param id       id of the duration.
	 */
	public static Duration getInstance(int duration, String id) {
		return new DefaultDuration(duration, id);
	}
}
