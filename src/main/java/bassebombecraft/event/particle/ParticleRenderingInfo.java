package bassebombecraft.event.particle;

import net.minecraft.particles.BasicParticleType;

/**
 * Static information for rendering a particle.
 */
public interface ParticleRenderingInfo {

	/**
	 * Get particle type to render.
	 * 
	 * @return particle type to render.
	 */
	BasicParticleType getParticleType();

	/**
	 * Get number of particle instances to render per update.
	 * 
	 * @return number of particle instances to render per update.
	 */
	int getNumber();

	/**
	 * Get duration of particle in game ticks.
	 */
	int getDuration();

	/**
	 * Return red color component.
	 * 
	 * @return red color component
	 */
	float getRedColorComponent();

	/**
	 * Return green color component.
	 * 
	 * @return green color component
	 */
	float getGreenColorComponent();

	/**
	 * Return blue color component.
	 * 
	 * @return blue color component
	 */
	float getBlueColorComponent();

	/**
	 * Return particle speed.
	 * 
	 * @return particle speed
	 */
	double getSpeed();

}
