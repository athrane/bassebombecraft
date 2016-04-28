package bassebombecraft.event.particle;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Default implementation of the {@linkplain ParticleRenderingRepository}.
 */
public class DefaultParticleRenderingRepository implements ParticleRenderingRepository {

	/**
	 * Particle container.
	 */
	Set<ParticleRendering> particles;

	/**
	 * DefaultParticleRenderingRepository constructor.
	 */
	public DefaultParticleRenderingRepository() {
		super();
		this.particles = Collections.synchronizedSet(new HashSet<ParticleRendering>());
	}

	@Override
	public void add(ParticleRendering particle) {
		if (particles.contains(particle))
			return;
		particles.add(particle);
	}

	@Override
	public void remove(ParticleRendering particle) {
		particles.remove(particle);
	}

	@Override
	public ParticleRendering[] getParticles() {
		ParticleRendering[] array = new ParticleRendering[particles.size()];
		return particles.toArray(array);
	}

	@Override
	public void clear() {
		particles.clear();
	}

	@Override
	public void updateParticleDuration() {

		synchronized (particles) {

			for (Iterator<ParticleRendering> it = particles.iterator(); it.hasNext();) {
				ParticleRendering particle = it.next();

				// update
				particle.updateDuration();

				// remove if expired
				if (particle.isExpired())
					it.remove();
			}
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
