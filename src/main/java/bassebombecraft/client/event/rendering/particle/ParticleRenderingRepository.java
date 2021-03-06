package bassebombecraft.client.event.rendering.particle;

import java.util.stream.Stream;

import bassebombecraft.event.particle.ParticleRendering;
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
	 * @param particle particle rendering directive to register for rendering.
	 */
	public void add(String id, ParticleRendering particle);

	/**
	 * Remove particle from rendering.
	 * 
	 * @param id id to remove particle rendering directive.
	 */
	public void remove(String id);

	/**
	 * Get particles to be rendered.
	 * 
	 * @return stream of particle rendering directives to be rendered.
	 */
	Stream<ParticleRendering> get();

}
