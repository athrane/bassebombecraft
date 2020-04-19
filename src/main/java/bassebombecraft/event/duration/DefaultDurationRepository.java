package bassebombecraft.event.duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Default implementation of the {@linkplain DurationRepository} interface.
 * 
 * Registered objects are removed when they expire.
 */
public class DefaultDurationRepository implements DurationRepository {

	/**
	 * Registered durable objects.
	 */
	ConcurrentHashMap<String, Duration> durableObjects = new ConcurrentHashMap<String, Duration>();

	@Override
	public void update() {

		// step 1: update objects and identify expired objects
		List<String> expired = new ArrayList<String>();
		durableObjects.forEachKey(10, k -> {

			// update
			Duration state = durableObjects.get(k);
			state.update();

			// remove if expired
			if (isExpired(k))
				expired.add(k);
		});

		// step 2: remove expired objects
		expired.forEach(k -> {

			// invoke callback if registered
			durableObjects.get(k).accept();

			// remove
			durableObjects.remove(k);
		});
	}

	@Override
	public void add(String id, int duration) {
		if (durableObjects.containsKey(id))
			return;

		Duration state = DefaultDuration.getInstance(duration, id);
		durableObjects.put(id, state);
	}

	@Override
	public void add(String id, int duration, Consumer<String> cId) {
		if (durableObjects.containsKey(id))
			return;

		Duration state = DefaultDuration.getInstance(duration, id, cId);
		durableObjects.put(id, state);
	}

	@Override
	public void remove(String id) {
		durableObjects.remove(id);
	}

	@Override
	public boolean isExpired(String id) {
		if (!durableObjects.containsKey(id))
			return true;

		Duration state = durableObjects.get(id);
		return state.isExpired();
	}

	@Override
	public int get(String id) {
		if (!durableObjects.containsKey(id))
			return 0;

		Duration state = durableObjects.get(id);
		return state.get();
	}

	/**
	 * Factory method.
	 * 
	 * @return duration repository instance.
	 */
	public static DurationRepository getInstance() {
		return new DefaultDurationRepository();
	}

}
