package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.selectRainbowColoredWool;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.config.ModConfiguration.rainbowSpiralMistDuration;
import static bassebombecraft.config.ModConfiguration.rainbowSpiralMistParticleInfo;
import static bassebombecraft.geom.BlockDirective.getInstance;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions.
 * 
 * This class paints the the landscape with rainbow colored wool blocks in an
 * outward spiral from the invoking entity/player.
 */
public class RainbowSpiralMist implements BlockMistActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = RainbowSpiralMist.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Effect duration.
	 */
	int duration;

	/**
	 * Current color counter.
	 */
	int colorCounter = 0;

	/**
	 * Constructor.
	 */
	public RainbowSpiralMist() {
		infos = createFromConfig(rainbowSpiralMistParticleInfo);
		duration = rainbowSpiralMistDuration.get();
	}

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {
		colorCounter++;

		// create wool block
		BlockState woolBlock = selectRainbowColoredWool(colorCounter);
		BlockDirective directive = getInstance(target, woolBlock.getBlock(), DONT_HARVEST, world);
		directive.setState(selectRainbowColoredWool(colorCounter));

		// create block
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
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
