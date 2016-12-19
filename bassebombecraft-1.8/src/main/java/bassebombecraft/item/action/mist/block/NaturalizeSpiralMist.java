package bassebombecraft.item.action.mist.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.GeometryUtils.createFlowerDirective;

import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain MistActionStrategy} for construction of mist
 * actions.
 * 
 * This class paints the the landscape with dirt blocks and plants random
 * flowers in an outward spiral from the invoking entity/player.
 */
public class NaturalizeSpiralMist implements BlockMistActionStrategy {

	static final float FLOWER_CHANCE = 0.75F;

	private static final int NUMBER_COLORS = 8;
	static final boolean DONT_HARVEST = false;
	static final int EFFECT_DURATION = 400; // Measured in ticks

	static final EnumParticleTypes PARTICLE_TYPE = EnumParticleTypes.SPELL_MOB;
	static final int PARTICLE_NUMBER = 5;
	static final int PARTICLE_DURATION = 20;
	static final float R = 0.0F;
	static final float B = 0.0F;
	static final float G = 0.75F;
	static final double PARTICLE_SPEED = 0.075;
	static final ParticleRenderingInfo MIST = getInstance(PARTICLE_TYPE, PARTICLE_NUMBER, PARTICLE_DURATION, R, G, B,
			PARTICLE_SPEED);
	static final ParticleRenderingInfo[] INFOS = new ParticleRenderingInfo[] { MIST };

	int colorCounter = 0;

	/**
	 * Random generator.
	 */
	Random random = new Random();

	@Override
	public void applyEffectToBlock(BlockPos target, World world) {
		colorCounter++;

		// create dirt block
		BlockDirective directive = new BlockDirective(target, Blocks.GRASS, DONT_HARVEST);

		// create block
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		directivesRepository.add(directive);

		if (shouldPlaceFlower()) {
			// create flower block
			BlockPos flowerPos = target.up();
			directive = createFlowerDirective(flowerPos, random);

			// create block
			directivesRepository.add(directive);
		}
	}

	/**
	 * Determines if a flower should be spawned.
	 * 
	 * @return true if a flower should be spawned
	 */
	boolean shouldPlaceFlower() {
		if (random.nextFloat() < FLOWER_CHANCE)
			return true;
		return false;
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
