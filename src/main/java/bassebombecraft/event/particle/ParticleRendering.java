package bassebombecraft.event.particle;

import java.util.Random;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;

/**
 * Interface for rendering of particle.
 */
public interface ParticleRendering {

	/**
	 * Get Id used to register particle rendering.
	 * 
	 * @return id used to register particle rendering.
	 */
	String getId();

	/**
	 * Get position where particles should be rendered.
	 * 
	 * @return position where particles should be rendered.
	 */
	BlockPos getPosition();

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
	 * Update duration of particle.
	 */
	void updateDuration();

	/**
	 * Get duration of particle.
	 * 
	 * @return duration of particle.
	 */
	int getDuration();

	/**
	 * Return red color component.
	 * 
	 * @param random random generator to randomise the color.
	 * 
	 * @return red color component
	 */
	float getRedColorComponent(Random random);

	/**
	 * Return green color component.
	 * 
	 * @param random random generator to randomise the color.
	 * 
	 * @return green color component
	 */
	float getGreenColorComponent(Random random);

	/**
	 * Return blue color component.
	 * 
	 * @param random random generator to randomise the color.
	 * 
	 * @return blue color component
	 */
	float getBlueColorComponent(Random random);

	/**
	 * Return particle speed.
	 * 
	 * @return particle speed
	 */
	double getSpeed();

	/**
	 * Return rendering info.
	 * 
	 * @return rendering info.
	 */
	ParticleRenderingInfo getInfo();
}
