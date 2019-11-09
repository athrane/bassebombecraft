package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.NOT_AN_AOE_EFFECT;
import static bassebombecraft.block.BlockUtils.selectPinkColoredWool;
import static bassebombecraft.config.ConfigUtils.createFromConfig;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.calculateSpiral;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.config.InventoryItemConfig;
import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class pinkynizes the areas around the
 * invoker.
 */
public class Pinkynize implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public final static String NAME = Pinkynize.class.getSimpleName();

	/**
	 * Particle rendering info
	 */
	ParticleRenderingInfo[] infos;

	/**
	 * Spiral size, measured in rotations around the centre.
	 */
	final int spiralSize;

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
	 * Pinkynize constructor
	 * 
	 * @param config        inventory item configuration.
	 * @param splSpiralSize Spiral size, measured in rotations around the centre.
	 */
	public Pinkynize(InventoryItemConfig config, Supplier<Integer> splSpiralSize) {
		infos = createFromConfig(config.particles);
		spiralSize = splSpiralSize.get();

		// get directives reposiory
		directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();

		// calculate spiral
		spiralCoordinates = calculateSpiral(spiralSize, spiralSize);
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
		BlockState woolBlock = selectPinkColoredWool(colorCounter);
		BlockDirective directive = new BlockDirective(groundPosition, woolBlock.getBlock(), DONT_HARVEST);
		directive.setState(selectPinkColoredWool(colorCounter));

		// create block
		BlockDirectivesRepository directivesRepository = getBassebombeCraft().getBlockDirectivesRepository();
		directivesRepository.add(directive);
	}

	@Override
	public int getEffectRange() {
		return NOT_AN_AOE_EFFECT;
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() {
		return infos;
	}

	/**
	 * Calculate target position in spiral.
	 * 
	 * @param target target to calculate position for.
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
	 * @param target target to initialize spiral from from.
	 */
	void initializeSpiral(Entity target) {
		spiralCounter = 0;
		spiralCenter = new BlockPos(target);
	}

}
