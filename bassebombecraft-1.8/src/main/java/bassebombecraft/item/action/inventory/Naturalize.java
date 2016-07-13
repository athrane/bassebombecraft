package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.GeometryUtils.createFlowerDirective;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class naturalizes the areas around the
 * invoker.
 */
public class Naturalize implements InventoryItemActionStrategy {

	/**
	 * Vertical blocks to query for a "ground block".
	 */
	static final int ITERATIONS_TO_QUERY = 256;

	static final boolean DONT_HARVEST = false;

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

	/**
	 * Random generator
	 */
	Random random = new Random();

	/**
	 * Block directives repository
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Naturalize constructor.
	 */
	public Naturalize() {
		super();

		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}
	
	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		return targetIsInvoker;
	}

	@Override
	public void applyEffect(Entity target, World world) {

		// locate ground block
		BlockPos groundPosition = locateGroundBlockPos(target.getPosition(), ITERATIONS_TO_QUERY, world);


		// create flower block
		BlockPos flowerPos = groundPosition.up();
		BlockDirective directive = createFlowerDirective(flowerPos, random);

		// create block
		directivesRepository.add(directive);
	}

	@Override
	public int getEffectRange() {
		return 1; // Not a AOE effect
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return INFOS;
	}

}
