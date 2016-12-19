package bassebombecraft.event.particle;

import java.util.Random;

import org.lwjgl.util.Color;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;

/**
 * Default implementation of {@linkplain ParticleRendering}
 */
public class DefaultParticleRendering implements ParticleRendering {

	static final Color COLOR_WHITE = new Color(255, 255, 255);
	static final int INFINITE_DURATION = -1;
	private BlockPos position;
	private int duration;
	private ParticleRenderingInfo info;

	/**
	 * DefaultParticleRendering constructor.
	 * 
	 * @param pos
	 *            particle position.
	 * @param info
	 *            particle rendering info.
	 */
	private DefaultParticleRendering(BlockPos pos, ParticleRenderingInfo info) {
		this.position = pos;
		this.info = info;
		this.duration = info.getDuration();
	}

	@Override
	public BlockPos getPosition() {
		return position;
	}

	@Override
	public EnumParticleTypes getParticleType() {
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

	/**
	 * Factory method for creation of a particle rendering info object.
	 * 
	 * @param pos
	 *            particle position.
	 * @param info
	 *            particle rendering info.
	 * 
	 * @return particle rendering object.
	 */
	public static ParticleRendering getInstance(BlockPos pos, ParticleRenderingInfo info) {
		return new DefaultParticleRendering(pos, info);
	}
}
