package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.BlockDirective.getInstance;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions. This class builds a mist which spawns rainbow colored wool blocks
 * when the mist moves away from the invoking entity/player.
 */
public class MovingRainbowMist implements BlockMistActionStrategy {

	private static final int NUMBER_COLORS = 8;
	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.NOTE;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float B = 0.75F;
	static final float G = 0.75F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);

	int colorCounter = 0;

	@Override
	public void applyEffectToBlock(BlockPos target, Level world) {
		colorCounter++;

		// create rainbow block
		BlockState blockstate = selectWoolColor();
		BlockDirective directive = getInstance(target, blockstate.getBlock(), DONT_HARVEST, world);
		directive.setState(blockstate);

		// create temporary block
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);
	}

	/**
	 * Select block based on wool color.
	 * 
	 * @return block based on wool color.
	 */
	BlockState selectWoolColor() {
		int colorSelector = colorCounter % NUMBER_COLORS;

		switch (colorSelector) {

		case 0:
			return Blocks.MAGENTA_WOOL.defaultBlockState();
		case 1:
			return Blocks.PURPLE_WOOL.defaultBlockState();
		case 2:
			return Blocks.BLUE_WOOL.defaultBlockState();
		case 3:
			return Blocks.LIGHT_BLUE_WOOL.defaultBlockState();
		case 4:
			return Blocks.LIME_WOOL.defaultBlockState();
		case 5:
			return Blocks.YELLOW_WOOL.defaultBlockState();
		case 6:
			return Blocks.ORANGE_WOOL.defaultBlockState();
		case 7:
			return Blocks.RED_WOOL.defaultBlockState();
		default:
			return Blocks.WHITE_WOOL.defaultBlockState();

		}
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
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
		return INFO;
	}

	@Override
	public int getSpiralOffset() {
		return 0;
	}

}
