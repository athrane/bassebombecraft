package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.config.ModConfiguration.naturalizeSpiralSize;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.createFlowerDirective;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.GeometryUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class naturalizes the areas around the
 * invoker.
 */
public class Naturalize implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = Naturalize.class.getSimpleName();

	/**
	 * Defines if processed blocks should be harvested.
	 */
	static final boolean DONT_HARVEST = false;

	/**
	 * Spiral size, measured in rotations around the centre.
	 */
	final int spiralSize;

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
	 * Constructor.
	 */
	public Naturalize() {
		spiralSize = naturalizeSpiralSize.get();

		// calculate spiral
		spiralCoordinates = GeometryUtils.calculateSpiral(spiralSize, spiralSize);
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
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {

		// calculate position
		BlockPos targetPosition = calculatePostion(target);

		// locate ground block
		BlockPos groundPosition = locateGroundBlockPos(targetPosition, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// create flower block
		BlockPos flowerPos = groundPosition.above();
		BlockDirective directive = createFlowerDirective(flowerPos, world);

		// create block
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();
		repository.add(directive);
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
		if (!spiralCenter.equals(target.blockPosition()))
			initializeSpiral(target);

		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size())
			return target.blockPosition();

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
		spiralCenter = new BlockPos(target.getX(), target.getY(), target.getZ());
	}

}
