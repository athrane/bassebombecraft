package bassebombecraft.item.action.mist.block;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.lavaSpiralMistDuration;
import static bassebombecraft.config.ModConfiguration.lavaSpiralMistParticleInfo;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions.
 * 
 * This class builds a mist moving in a outward spiral which temporarily
 * replaces the blocks with lava.
 */
public class LavaSpiralMist implements BlockMistActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = LavaSpiralMist.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo info;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor.
	 */
	public LavaSpiralMist() {
		info = createInfoFromConfig(lavaSpiralMistParticleInfo);
		duration = lavaSpiralMistDuration.get();
	}

	@Override
	public void applyEffectToBlock(BlockPos target, Level world) {
		setTemporaryBlock(world, target, Blocks.LAVA, duration);
	}

	@Override
	public int getEffectDuration() {
		return duration;
	}

	@Override
	public int getNumberMists() {
		return 1;
	}

	@Override
	public double getMistAngle() {
		return 0;
	}

	@Override
	public boolean isOneShootEffect() {
		return false;
	}

	@Override
	public ParticleRenderingInfo getRenderingInfo() {
		return info;
	}

	@Override
	public int getSpiralOffset() {
		return 1;
	}

}
