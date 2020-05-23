package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.block.BlockUtils.selectRainbowColoredWool;
import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;
import java.util.function.Supplier;

import bassebombecraft.event.block.BlockDirectivesRepository;
import static bassebombecraft.geom.BlockDirective.*;

import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.GeometryUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class rainbownizes the areas around the
 * invoker.
 */
public class Rainbownize implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = Rainbownize.class.getSimpleName();

	/**
	 * Spiral size, measured in rotations around the centre.
	 */
	final int spiralSize;

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
	 * 
	 * @param splSpiralSize Spiral size, measured in rotations around the centre.
	 */
	public Rainbownize(Supplier<Integer> splSpiralSize) {
		spiralSize = splSpiralSize.get();

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
	public void applyEffect(LivingEntity target, World world, LivingEntity invoker) {
		colorCounter++;

		// calculate position
		BlockPos targetPosition = calculatePostion(target);

		// locate ground block
		BlockPos groundPosition = locateGroundBlockPos(targetPosition, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// create wool block
		BlockState woolBlock = selectRainbowColoredWool(colorCounter);
		BlockDirective directive = getInstance(groundPosition, woolBlock.getBlock(), DONT_HARVEST);
		directive.setState(woolBlock);

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
