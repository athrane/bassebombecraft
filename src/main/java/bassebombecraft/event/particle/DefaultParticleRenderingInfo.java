package bassebombecraft.event.particle;

import net.minecraft.particles.BasicParticleType;

public class DefaultParticleRenderingInfo implements ParticleRenderingInfo {

	private BasicParticleType type;
	private int number;
	private int duration;
	private float rgbRed;
	private float rgbBlue;
	private float rgbGreen;
	private double speed;

	/**
	 * DefaultParticleRenderingInfo constructor.
	 * 
	 * @param type     particle type.
	 * @param number   number of particles to render per update.
	 * @param duration number of updates to render particles. Particle is removed
	 *                 when the number reaches zero. If the duration is negative
	 *                 then it is interpreted as an infinite duration.
	 * @param rgbRed   red RGB color component of the particle.
	 * @param rgbGren  green RGB color component of the particle.
	 * @param rgbBlue  blue RGB color component of the particle.
	 * @param speed    particle speed.
	 */
	private DefaultParticleRenderingInfo(BasicParticleType type, int number, int duration, float rgbRed, float rgbGreen,
			float rgbBlue, double speed) {
		this.type = type;
		this.number = number;
		this.duration = duration;
		this.rgbRed = rgbRed;
		this.rgbGreen = rgbGreen;
		this.rgbBlue = rgbBlue;
		this.speed = speed;
	}

	@Override
	public BasicParticleType getParticleType() {
		return type;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public float getRedColorComponent() {
		return rgbRed;
	}

	@Override
	public float getGreenColorComponent() {
		return rgbGreen;
	}

	@Override
	public float getBlueColorComponent() {
		return rgbBlue;
	}

	@Override
	public double getSpeed() {
		return speed;
	}

	/**
	 * Factory method for creation of a particle rendering info.
	 * 
	 * @param type     particle type.
	 * @param number   number of particles to render per update.
	 * @param duration number of updates to render particles. Particle is removed
	 *                 when the number reaches zero. If the duration is negative
	 *                 then it is interpreted as an infinite duration.
	 * @param rgbRed   red RGB color component of the particle.
	 * @param rgbGren  green RGB color component of the particle.
	 * @param rgbBlue  blue RGB color component of the particle.
	 * @param speed    particle speed.
	 * 
	 * @return particle rendering info.
	 */
	public static ParticleRenderingInfo getInstance(BasicParticleType type, int number, int duration, float rgbRed,
			float rgbGreen, float rgbBlue, double speed) {
		return new DefaultParticleRenderingInfo(type, number, duration, rgbRed, rgbGreen, rgbBlue, speed);
	}

}
