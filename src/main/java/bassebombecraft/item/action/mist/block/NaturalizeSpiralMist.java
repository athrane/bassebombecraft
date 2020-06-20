package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralMistDuration;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralMistParticleInfo;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.geom.GeometryUtils.createFlowerDirective;
import static net.minecraft.block.Blocks.GRASS_BLOCK;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Constructor.
	 */
	public NaturalizeSpiralMist() {
		infos = createFromConfig(naturalizeSpiralMistParticleInfo);
		duration = naturalizeSpiralMistDuration.get();
	}

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {

		// create dirt block
		BlockDirective directive = getInstance(target, GRASS_BLOCK, DONT_HARVEST, world);

		// create block
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);

		// create flower block
		BlockPos flowerPos = target.up();
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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

	@Override
	public int getSpiralOffset() {
		return 0;
	}

}
