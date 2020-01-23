package bassebombecraft.item.action.mist.block;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.config.ConfigUtils.createFromConfig;

import java.util.function.Supplier;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	public final static String NAME = LavaSpiralMist.class.getSimpleName();

	/**
	 * Don't harvest processed blocks.
	 */
	static final boolean DONT_HARVEST = false;

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * LavaSpiralMist constructor.
	 *
	 * @param splDuration effect duration.
	 */
	public LavaSpiralMist(Supplier<Integer> splDuration) {
		infos = createFromConfig(ModConfiguration.lavaSpiralMistParticleInfo);
		duration = splDuration.get();
	}

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {
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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

	@Override
	public int getSpiralOffset() {
		return 1;
	}

}
