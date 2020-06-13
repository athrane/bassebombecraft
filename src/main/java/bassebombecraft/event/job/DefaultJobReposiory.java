package bassebombecraft.event.job;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;

/**
 * SERVER side implementation of the {@linkplain JobRepository }.
 * 
 * Used the {@linkplain DurationRepository} as backend.
 */
public class DefaultJobReposiory implements JobRepository {

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires a
	 * {@linkplain Job} added by this repository.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the expired element will
	 * be removed from this repository as well.
	 */
	Consumer<String> cRemovalCallback = id -> remove(id);

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} updated a
	 * {@linkplain Job} added by this repository.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the updated job will be
	 * updated by this repository.
	 */
	Consumer<String> cUpdateCallback = id -> update(id);

	/**
	 * Registered jobs.
	 */
	Map<String, Job> jobs = new ConcurrentHashMap<String, Job>();

	@Override
	public void add(String id, int duration, Job job) {
		try {

			if (contains(id))
				return;

			// register job with server duration repository
			DurationRepository repository = getProxy().getServerDurationRepository();
			repository.add(id, duration, cUpdateCallback, cRemovalCallback);

			// store job
			jobs.put(id, job);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void remove(String id) {
		if (!contains(id))
			return;

		// remove
		jobs.remove(id);
	}

	public void update(String id) {
		if (!contains(id))
			return;

		// invoke job
		Job job = jobs.get(id);
		job.update();
	}

	@Override
	public boolean contains(String id) {
		return jobs.containsKey(id);
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static JobRepository getInstance() {
		return new DefaultJobReposiory();
	}

}
