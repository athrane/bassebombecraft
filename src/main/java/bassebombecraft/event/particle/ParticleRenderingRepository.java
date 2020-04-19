package bassebombecraft.event.particle;

import bassebombecraft.proxy.Proxy;

/**
 * Interface for repository for rendering of client side rendering of particles.
 * 
 * The repository is used at CLIENT side. Access to the repository is supported
 * via sided proxy, i.e.{@linkplain Proxy}.
 */
public interface ParticleRenderingRepository {

	/**
	 * Register particle for rendering.
	 * 
	 * @param first marker.
	 */
	public void add(ParticleRendering particle);

	/**
	 * Remove particle from rendering.
	 * 
	 * @param particle particle to remove.
	 */
	public void remove(ParticleRendering particle);

	/**
	 * Get particles to be rendered.
	 * 
	 * @return particles to be rendered.
	 */
	ParticleRendering[] getParticles();

	/**
	 * Clear particles.
	 */
	public void clear();

	/**
	 * Update particle duration.
	 */
	public void updateParticleDuration();

}
