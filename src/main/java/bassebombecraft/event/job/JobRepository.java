package bassebombecraft.event.job;

import bassebombecraft.proxy.Proxy;

/**
 * Interface for repository for handling timed jobs.
 * 
 * The repository is used at SERVER side. Access to the repository is supported
 * via sided proxy, i.e.{@linkplain Proxy}.
 * 
 */
public interface JobRepository {

	/**
	 * Add timed job.
	 * 
	 * @param id ID of the job to add.
	 * @param duration duration of the job.
	 * @param job job to register at the repository.
	 */
	public void add(String id, int duration, Job job);

	/**
	 * Returns true if job with ID is already registered.
	 * 
	 * @param id ID of the job to query for.
	 * @return true if job is registered.
	 */
	public boolean contains(String id);

	/**
	 * Remove job.
	 * 
	 * @param id ID of job which is removed.
	 */
	public void remove(String id);

}
