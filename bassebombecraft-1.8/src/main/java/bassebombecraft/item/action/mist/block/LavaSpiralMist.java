package bassebombecraft.item.action.mist.block;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions.
 * 
 * This class builds a mist moving in a outward spiral which temporarily
 * replaces the blocks with lava.
 */
public class LavaSpiralMist implements BlockMistActionStrategy {

	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.FLAME;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.0F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {
		setTemporaryBlock(world, target, Blocks.lava, EFFECT_DURATION);
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
		return 1;
	}
	
}
