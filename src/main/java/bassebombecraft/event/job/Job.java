package bassebombecraft.event.job;

/**
 * Interface for job registered at the {@linkplain JobRepository}
 */
public interface Job {

	/**
	 * Update job.
	 */
	void update();

	/**
	 * Invoked when job is terminated.
	 */
	void terminate();

}
