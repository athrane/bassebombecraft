package bassebombecraft.item.action.mist.block;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;

import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions. This class builds a mist which spawns multiple lava blocks when the mist
 * moves away from the invoking entity/player.
 */
public class MovingIceMultiMist implements BlockMistActionStrategy {

	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.ITEM_SNOWBALL;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.75F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);

	@Override
	public void applyEffectToBlock(BlockPos target, Level world) {
		setTemporaryBlock(world, target, Blocks.ICE, EFFECT_DURATION);
	}

	@Override
	public int getEffectDuration() {
		return EFFECT_DURATION;
	}

	@Override
	public boolean isOneShootEffect() {
		return false;
	}
	
	@Override
	public int getNumberMists() {
		return 5;
	}

	@Override
	public double getMistAngle() {
		return 20;
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
