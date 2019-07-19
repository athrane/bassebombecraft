package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.selectRainbowColoredWool;
import static bassebombecraft.event.particle.DefaultParticleRenderingInfo.getInstance;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.GeometryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class rainbownizes the areas around the
 * invoker.
 */
public class Rainbownize implements InventoryItemActionStrategy {

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
	 * Spiral size.
	 */
	static final int SPIRAL_SIZE = 20;

	/**
	 * Random generator
	 */
	Random random = new Random();

	/**
	 * Block directives repository
	 */
	BlockDirectivesRepository directivesRepository;

	/**
	 * Current color counter.
	 */
	int colorCounter = 0;

	/**
	 * Spiral counter.
	 */
	int spiralCounter;

	/**
	 * Global centre of the spiral.
	 */
	BlockPos spiralCenter;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Rainbownize constructor.
	 */
	public Rainbownize() {
		super();

		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(SPIRAL_SIZE, SPIRAL_SIZE);
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
	public void applyEffect(Entity target, World world, LivingEntity invoker) {
		colorCounter++;

		// calculate position
		BlockPos targetPosition = calculatePostion(target);

		// locate ground block
		BlockPos groundPosition = locateGroundBlockPos(targetPosition, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// create wool block
		BlockDirective directive = new BlockDirective(groundPosition, Blocks.WOOL, DONT_HARVEST);
		directive.setState(selectRainbowColoredWool(colorCounter));

		// create block
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
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

	/**
	 * Calculate target position in spiral.
	 * 
	 * @param target
	 *            target to calculate position for.
	 * 
	 * @return target position in spiral
	 */
	BlockPos calculatePostion(Entity target) {

		// initialize if no last position or a new position
		if (spiralCenter == null)
			initializeSpiral(target);
		if (!spiralCenter.equals(target.getPosition()))
			initializeSpiral(target);

		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size())
			return target.getPosition();

		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

		// calculate ground coordinates
		int x = spiralCenter.getX() + spiralCoord.getX();
		int y = spiralCenter.getY();
		int z = spiralCenter.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x, y, z);

		// update spiral
		spiralCounter++;

		return groundCandidate;
	}

	/**
	 * Initialize spiral
	 * 
	 * @param target
	 *            target to initialize spiral from from.
	 */
	void initializeSpiral(Entity target) {
		spiralCounter = 0;
		spiralCenter = new BlockPos(target);
	}

}
