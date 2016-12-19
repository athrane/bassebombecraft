package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.BlockColored;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumParticleTypes;
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

	private static final int NUMBER_COLORS = 8;
	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 800; // Measured in ticks

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.NOTE;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float B = 0.75F;
	static final float G = 0.75F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	int colorCounter = 0;

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {
		colorCounter++;

		// create wool block
		BlockDirective directive = new BlockDirective(target, Blocks.WOOL, DONT_HARVEST);
		directive.setState(selectWoolColor());

		// create block
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		directivesRepository.add(directive);
	}

	/**
	 * Select block based on wool color.
	 * 
	 * @return block based on wool color.
	 */
	IBlockState selectWoolColor() {
		int colorSelector = colorCounter % NUMBER_COLORS;

		switch (colorSelector) {

		case 0:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.MAGENTA);
		case 1:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.PURPLE);
		case 2:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BLUE);
		case 3:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIGHT_BLUE);
		case 4:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.LIME);
		case 5:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
		case 6:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
		case 7:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
		default:
			return Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);

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
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

	@Override
	public int getSpiralOffset() {
		return 0;
	}

}
