package bassebombecraft.entity.ai.goal;

import java.util.stream.Stream;

import net.minecraft.world.entity.LivingEntity;

/**
 * Interface for repository for storage of {@linkplain Observation}.
 */
public interface ObservationRepository {

	/**
	 * Observe situation.
	 * 
	 * @param target      target to observe.
	 * 
	 * @return created observation.
	 */
	public Observation observe(LivingEntity target);

	/**
	 * Get all observations.
	 * 
	 * @return all observations
	 */
	public Stream<Observation> get();

	/**
	 * Returns true if less that two observations are registered.
	 * 
	 * @return true if less that two observations are registered.
	 */
	public boolean isTooFewObservationsRegistered();
	
	/**
	 * Clears all observation.
	 */
	public void clear();
}
