package bassebombecraft.client.event.particle;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ClientModConstants.PARTICLE_SPAWN_DURATION;
import static bassebombecraft.event.duration.Duration.NO_EXPIRY;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bassebombecraft.event.duration.DurationRepository;
import bassebombecraft.event.particle.ParticleRendering;

/**
 * Default implementation of the {@linkplain ParticleRenderingRepository}.
 */
public class DefaultParticleRenderingRepository implements ParticleRenderingRepository {

	/**
	 * Consumer to support callback when {@linkplain DurationRepository} expires a
	 * {@linkplain ParticleRendering} added by this repository.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the expired element will
	 * be removed from this repository as well.
	 */
	Consumer<String> cRemovalCallback = id -> remove(id);

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
			int duration = resolveSpawnDuration(particle.getInfo().getDuration());
			getProxy().getClientDurationRepository().add(id, duration, cRemovalCallback);

			// store particle
			particles.put(id, particle);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	/**
	 * Resolve spawn duration of particles renderings.
	 * 
	 * If particle rendering is configured with -1 then it will be configured never to expire.
	 * Otherwise it will be configured to use default expiry value.
	 * 
	 * @param duration particle duration to resolve.
	 * 
	 * @return resolved duration value for particle.
	 */
	int resolveSpawnDuration(int duration) {
		if (duration == NO_EXPIRY)
			return NO_EXPIRY;
		return PARTICLE_SPAWN_DURATION;
	}

	@Override
	public void remove(String id) {
		try {
			// remove particle
			particles.remove(id);

			// remove duration
			getProxy().getClientDurationRepository().remove(id);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public Stream<ParticleRendering> get() {
		return particles.values().stream();
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
