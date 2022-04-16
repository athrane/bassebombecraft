package bassebombecraft.event.particle;

import static net.minecraftforge.registries.ForgeRegistries.PARTICLE_TYPES;

import bassebombecraft.BassebombeCraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

/**
 * Default implementation of the {@linkplain ParticleRenderingInfo} interface.
 */
public class DefaultParticleRenderingInfo implements ParticleRenderingInfo {

	SimpleParticleType type;
	String unresolvedType;
	int number;
	int duration;
	float rgbRed;
	float rgbBlue;
	float rgbGreen;
	double speed;

	/**
	 * Constructor.
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
	DefaultParticleRenderingInfo(SimpleParticleType type, int number, int duration, float rgbRed, float rgbGreen,
			float rgbBlue, double speed) {
		this.type = type;
		this.unresolvedType = type.getRegistryName().toString();				
		this.number = number;
		this.duration = duration;
		this.rgbRed = rgbRed;
		this.rgbGreen = rgbGreen;
		this.rgbBlue = rgbBlue;
		this.speed = speed;
	}

	/**
	 * Constructor.
	 * 
	 * @param type     unresolved particle type. The type is specified as a string
	 *                 and not resolved to an actual particle due to class loading
	 *                 issues. 
	 * @param number   number of particles to render per update.
	 * @param duration number of updates to render particles. Particle is removed
	 *                 when the number reaches zero. If the duration is negative
	 *                 then it is interpreted as an infinite duration.
	 * @param rgbRed   red RGB color component of the particle.
	 * @param rgbGren  green RGB color component of the particle.
	 * @param rgbBlue  blue RGB color component of the particle.
	 * @param speed    particle speed.
	 */
	DefaultParticleRenderingInfo(String type, int number, int duration, float rgbRed, float rgbGreen,
			float rgbBlue, double speed) {
		this.unresolvedType = type;		
		this.type = null;
		this.number = number;
		this.duration = duration;
		this.rgbRed = rgbRed;
		this.rgbGreen = rgbGreen;
		this.rgbBlue = rgbBlue;
		this.speed = speed;
	}
	
	@Override
	public SimpleParticleType getParticleType() {

		// return type if resolved
		if(type != null) return type;
		
		// resolved unresolved type
		ResourceLocation key = new ResourceLocation(unresolvedType.toLowerCase());
		ParticleType<?> resolved = PARTICLE_TYPES.getValue(key);
		
		// store and return resolved type
		if(resolved != null) {
			type = (SimpleParticleType) resolved;
			return type;
		}
		
		// report exception
		String msg = "Failed to initialize particle from configuration value : " + key;
		BassebombeCraft.getBassebombeCraft().reportAndLogError(msg);
		
		// add default particle 
		return ParticleTypes.HEART;
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
	public static ParticleRenderingInfo getInstance(SimpleParticleType type, int number, int duration, float rgbRed,
			float rgbGreen, float rgbBlue, double speed) {
		return new DefaultParticleRenderingInfo(type, number, duration, rgbRed, rgbGreen, rgbBlue, speed);
	}

	/**
	 * Factory method for creation of a unresolved particle rendering info.
	 * 
	 * @param type     unresolved particle type. The type is specified as a string
	 *                 and not resolved to an actual particle due to class loading
	 *                 issues. 
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
	public static ParticleRenderingInfo getUnresolvedInstance(String type, int number, int duration,
			float rgbRed, float rgbGreen, float rgbBlue, double speed) {
		return new DefaultParticleRenderingInfo(type, number, duration, rgbRed, rgbGreen, rgbBlue, speed);
	}

}
