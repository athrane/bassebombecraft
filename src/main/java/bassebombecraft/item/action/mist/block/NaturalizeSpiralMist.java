package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.config.ConfigUtils.createInfoFromConfig;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralMistDuration;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralMistParticleInfo;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.geom.GeometryUtils.createFlowerDirective;
import static net.minecraft.world.level.block.Blocks.GRASS_BLOCK;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions.
 * 
 * This class paints the the landscape with dirt blocks and plants random
 * flowers in an outward spiral from the invoking entity/player.
 */
public class NaturalizeSpiralMist implements BlockMistActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = NaturalizeSpiralMist.class.getSimpleName();

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
	public NaturalizeSpiralMist() {
		info = createInfoFromConfig(naturalizeSpiralMistParticleInfo);
		duration = naturalizeSpiralMistDuration.get();
	}

	@Override
	public void applyEffectToBlock(BlockPos target, Level world) {

		// create dirt block
		BlockDirective directive = getInstance(target, GRASS_BLOCK, DONT_HARVEST, world);

		// create block
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);

		// create flower block
		BlockPos flowerPos = target.above();
		directive = createFlowerDirective(flowerPos, world);

		// create block
		repository.add(directive);
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
		return 0;
	}

}
