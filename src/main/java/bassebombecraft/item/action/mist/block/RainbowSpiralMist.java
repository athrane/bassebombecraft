package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.block.BlockUtils.selectRainbowColoredWool;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.BlockDirective.getInstance;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.BlockState;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
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

	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 800; // Measured in ticks

	static final BasicParticleType PARTICLE_TYPE = ParticleTypes.NOTE;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.75F;
	static final float B = 0.75F;
	static final float G = 0.75F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	/**
	 * Current color counter.
	 */
	int colorCounter = 0;

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
