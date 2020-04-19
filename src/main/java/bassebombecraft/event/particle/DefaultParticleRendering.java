package bassebombecraft.event.particle;

import java.util.Random;
import java.util.UUID;

import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.BlockPos;

/**
 * Default implementation of {@linkplain ParticleRendering}
 */
public class DefaultParticleRendering implements ParticleRendering {

	/**
	 * Infinite duration.
	 */
	static final int INFINITE_DURATION = -1;

	/**
	 * Id used to register and unregister particle rendering.
	 */
	String id;

	/**
	 * position for rendering of particles.
	 */
	BlockPos position;

	/**
	 * Duration of rendering in game ticks.
	 */
	int duration;

	/**
	 * Particle rendering info.
	 */
	ParticleRenderingInfo info;

	/**
	 * Constructor.
	 * 
	 * @param pos  particle position.
	 * @param info particle rendering info.
	 */
	DefaultParticleRendering(BlockPos pos, ParticleRenderingInfo info) {
		this.id = UUID.randomUUID().toString();
		this.position = pos;
		this.info = info;
		this.duration = info.getDuration();
	}

	/**
	 * Constructor.
	 * 
	 * @param id   used to register and unregister particle rendering.
	 * @param pos  particle position.
	 * @param info particle rendering info.
	 */
	DefaultParticleRendering(String id, BlockPos pos, ParticleRenderingInfo info) {
		this.id = id;
		this.position = pos;
		this.info = info;
		this.duration = info.getDuration();
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public BlockPos getPosition() {
		return position;
	}

	@Override
	public BasicParticleType getParticleType() {
		return info.getParticleType();
	}

	@Override
	public int getNumber() {
		return info.getNumber();
	}

	@Override
	public void updateDuration() {
		if (duration > 0)
			duration--;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public boolean isExpired() {
		return (duration == 0);
	}

	@Override
	public float getRedColorComponent(Random random) {
		float rgbRed = info.getRedColorComponent();
		return (random.nextFloat() * 0.25f) + (rgbRed * 0.75f) - 0.12f;
	}

	@Override
	public float getGreenColorComponent(Random random) {
		float rgbGreen = info.getGreenColorComponent();
		return (random.nextFloat() * 0.25f) + (rgbGreen * 0.75f) - 0.12f;
	}

	@Override
	public float getBlueColorComponent(Random random) {
		float rgbBlue = info.getBlueColorComponent();
		return (random.nextFloat() * 0.25f) + (rgbBlue * 0.75f) - 0.12f;
	}

	@Override
	public double getSpeed() {
		return info.getSpeed();
	}

	@Override
	public ParticleRenderingInfo getInfo() {
		return info;
	}

	/**
	 * Factory method for creation of a particle rendering info object.
	 * 
	 * @param pos  particle position.
	 * @param info particle rendering info.
	 * 
	 * @return particle rendering object.
	 */
	public static ParticleRendering getInstance(BlockPos pos, ParticleRenderingInfo info) {
		return new DefaultParticleRendering(pos, info);
	}

	/**
	 * Factory method for creation of a particle rendering info object.
	 * 
	 * @param id   used to register and unregister particle rendering.
	 * @param pos  particle position.
	 * @param info particle rendering info.
	 * 
	 * @return particle rendering object.
	 */
	public static ParticleRendering getInstance(String id, BlockPos pos, ParticleRenderingInfo info) {
		return new DefaultParticleRendering(id, pos, info);
	}
}
