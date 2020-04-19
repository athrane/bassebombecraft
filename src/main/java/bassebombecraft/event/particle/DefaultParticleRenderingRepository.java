package bassebombecraft.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import bassebombecraft.event.duration.DurationRepository;

/**
 * Default implementation of the {@linkplain ParticleRenderingRepository}.
 */
public class DefaultParticleRenderingRepository implements ParticleRenderingRepository {

	/**
	 * Registered particles.
	 */
	ConcurrentHashMap<String, ParticleRendering> particles = new ConcurrentHashMap<String, ParticleRendering>();

	@Override
	public void add(String id, ParticleRendering particle) {
		try {
			if (particles.containsKey(id))
				return;

			// register duration
			getProxy().getDurationRepository().add(id, particle.getDuration());

			// store particle
			particles.put(id, particle);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void remove(String id) {
		try {
			// remove particle
			particles.remove(id);

			// remove duration
			getProxy().getDurationRepository().remove(id);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public ParticleRendering[] getParticles() {
		ParticleRendering[] array = new ParticleRendering[particles.size()];
		return particles.values().toArray(array);
	}

	@Override
	public Stream<ParticleRendering> get() {
		return particles.values().stream();
	}
	
	@Override
	public void clear() {
		try {
			// get repository
			DurationRepository repository = getProxy().getDurationRepository();

			// remove durations
			particles.forEachKey(10, k -> repository.remove(k));

			// remove particles
			particles.clear();

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static ParticleRenderingRepository getInstance() {
		return new DefaultParticleRenderingRepository();
	}
}
