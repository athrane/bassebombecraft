package bassebombecraft.entity.ai.goal;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

import net.minecraft.entity.LivingEntity;

/**
 * Implementation of {@linkplain ObservationRepository}.
 */
public class DefaultObservationRepository implements ObservationRepository {

	/**
	 * Number of stored observations.
	 */
	static final int LIMIT = 2;

	/**
	 * Observed entity.
	 */
	LivingEntity entity;

	Queue<Observation> observations = new ArrayBlockingQueue<>(LIMIT);
	
	/**
	 * DefaultObservationRepository constructor.
	 * 
	 * @param entity entity to observe.
	 */
	public DefaultObservationRepository(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public Observation observe(LivingEntity target) {
		Observation observation = DefaultObservation.getInstance(entity, target);
		
		// remote last element if queue is full
		if(observations.size() == LIMIT) 
			observations.remove();
		
		observations.add(observation);		
		return observation;
	}
		
	@Override
	public Stream<Observation> get() {
		return observations.stream();
	}
	
	@Override
	public boolean isTooFewObservationsRegistered() {
		return(observations.size() < 2); 
	}

	
	@Override
	public void clear() {
		observations.clear();
	}

	/**
	 * Factory method.
	 * 
	 * @param entity entity to observe.
	 * 
	 * @return repository.
	 */
	public static ObservationRepository getInstance(LivingEntity entity) {
		return new DefaultObservationRepository(entity);
	}
}
