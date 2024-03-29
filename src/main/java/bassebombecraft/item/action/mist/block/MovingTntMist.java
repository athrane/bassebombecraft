package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.BlockDirective.getInstance;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions. This class builds a mist which spawns TNT blocks when the mist moves
 * away from the invoking entity/player.
 */
public class MovingTntMist implements BlockMistActionStrategy {

	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 200; // Measured in ticks

	static final SimpleParticleType PARTICLE_TYPE = ParticleTypes.FLAME;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.0F;
	static final float G = 0.0F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo INFO = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final int MOD_VALUE = 10;

	/**
	 * TNT counter
	 */
	int counter = 0;

	@Override
	public void applyEffectToBlock(BlockPos target, Level world) {
		counter++;
		counter = counter % MOD_VALUE;

		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();

		if (counter == 0) {
			BlockDirective directive = getInstance(target, Blocks.REDSTONE_BLOCK, DONT_HARVEST, world);
			repository.add(directive);
			return;
		}

		BlockDirective directive = getInstance(target, Blocks.TNT, DONT_HARVEST, world);
		repository.add(directive);
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
		return 1;
	}

	@Override
	public double getMistAngle() {
		return 0;
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
