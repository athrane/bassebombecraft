package bassebombecraft.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NULL_TILE_ENTITY;
import static net.minecraft.util.EnumFacing.EAST;
import static net.minecraft.util.EnumFacing.NORTH;
import static net.minecraft.util.EnumFacing.SOUTH;
import static net.minecraft.util.EnumFacing.WEST;

import com.google.common.collect.ImmutableMap;

import bassebombecraft.event.block.temporary.DefaultTemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Block utilities.
 */
public class BlockUtils {

	/**
	 * Default FACING property for querying about the property.
	 */
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	/**
	 * Don't harvest temporary block.
	 */
	public static final boolean DONT_HARVEST = false;

	/**
	 * Number of rainbow wool colors.
	 */
	static final int NUMBER_RAINBOW_COLORS = 8;

	/**
	 * Number of rainbow wool colors.
	 */
	static final int NUMBER_PINK_COLORS = 2;

	/**
	 * Create single block of designated block type.
	 * 
	 * The block is only processed if the world isn't located at client side.
	 * 
	 * The block is only harvested if the block directive specifies it.
	 * 
	 * @param blockDirective block directive for block to create.
	 * @param worldQuery     world query object.
	 */
	public static void createBlock(BlockDirective blockDirective, WorldQuery worldQuery) {
		if (worldQuery.isWorldAtClientSide())
			return;

		// get block
		BlockPos blockPosition = blockDirective.getBlockPosition();
		Block block = getBlockFromPosition(blockDirective, worldQuery);

		// get world
		World world = worldQuery.getWorld();

		// harvest block
		if (blockDirective.harvestBlock()) {
			BlockState blockState = getBlockStateFromPosition(blockPosition, worldQuery);
			ItemStack emptyItemStack = new ItemStack(block);
			block.harvestBlock(worldQuery.getWorld(), worldQuery.getPlayer(), blockPosition, blockState,
					NULL_TILE_ENTITY, emptyItemStack);
		}

		// set block state
		world.setBlockState(blockPosition, blockDirective.getState());
	}

	/**
	 * Get block from block position.
	 * 
	 * @param blockPosition position of the block.
	 * @param world         world object.
	 * @return block located at block position
	 */
	public static Block getBlockFromPosition(BlockPos blockPosition, World world) {
		BlockState blockState = world.getBlockState(blockPosition);
		return blockState.getBlock();
	}

	/**
	 * Get block from block position.
	 * 
	 * @param blockPosition position of the block.
	 * @param worldQuery    world query object.
	 * @return block located at block position
	 */
	public static Block getBlockFromPosition(BlockPos blockPosition, WorldQuery worldQuery) {
		World world = worldQuery.getWorld();
		return getBlockFromPosition(blockPosition, world);
	}

	/**
	 * Get block defined at position specified by block directive.
	 * 
	 * @param blockDirective directive to get the block from.
	 * @param worldQuery     world query object.
	 * @return block defined at position specified by block directive.
	 */
	public static Block getBlockFromPosition(BlockDirective blockDirective, WorldQuery worldQuery) {
		BlockPos blockPosition = blockDirective.getBlockPosition();
		return getBlockFromPosition(blockPosition, worldQuery);
	}

	/**
	 * Get block state from block position.
	 * 
	 * @param blockPosition position of the block.
	 * @param worldQuery    world query object.
	 * @return block state located at block position
	 */
	public static BlockState getBlockStateFromPosition(BlockPos blockPosition, WorldQuery worldQuery) {
		World world = worldQuery.getWorld();
		return world.getBlockState(blockPosition);
	}

	/**
	 * Rotate block state FACING property.
	 * 
	 * If block state doesn't have the FACING property defined then the source state
	 * is returned unchanged.
	 * 
	 * The orientation is defined in degrees and only the values 0, 90, 180 and 270
	 * are processed. For all other values the FACING property is returned
	 * unchanged.
	 * 
	 * @param source      State source block state.
	 * @param orientation orientation in degrees that the block state should be
	 *                    rotated.
	 * 
	 * @return rotated block state where the FACING property is updated.
	 */
	public static BlockState rotateBlockStateWithFacingProperty(BlockState sourceState, double orientation) {

		// exit if angle is zero
		if (orientation == 0)
			return sourceState;

		// exit if block state doesn't have the FACING property defined.
		// source state is returned unchanged
		if (!hasFacingProperty(sourceState))
			return sourceState;

		// get facing property
		EnumFacing facing = sourceState.getValue(FACING);

		// calculate new orientation
		EnumFacing value = calculateFacingProperty(facing, orientation);

		// create now rotated state
		BlockState rotatedState = sourceState.withProperty(FACING, value);
		return rotatedState;
	}

	/**
	 * Calculate FACING property from orientation and source property.
	 * 
	 * The orientation is defined in degrees and only the values 0, 90, 180 and 270
	 * are processed. For all other values the FACING property is returned
	 * unchanged.
	 * 
	 * @param sourceFacing source FACING property.
	 * @param orientation  orientation in degrees to rotate the property.
	 * @return rotated facing property.
	 */
	public static EnumFacing calculateFacingProperty(EnumFacing sourceFacing, double orientation) {
		if (orientation == 0)
			return sourceFacing;

		if (orientation == 90) {
			if (sourceFacing == SOUTH)
				return WEST;
			if (sourceFacing == WEST)
				return NORTH;
			if (sourceFacing == NORTH)
				return EAST;
			if (sourceFacing == EAST)
				return SOUTH;
		}

		if (orientation == 180) {
			if (sourceFacing == SOUTH)
				return NORTH;
			if (sourceFacing == WEST)
				return EAST;
			if (sourceFacing == NORTH)
				return SOUTH;
			if (sourceFacing == EAST)
				return WEST;
		}

		if (orientation == 270) {
			if (sourceFacing == SOUTH)
				return EAST;
			if (sourceFacing == WEST)
				return SOUTH;
			if (sourceFacing == NORTH)
				return WEST;
			if (sourceFacing == EAST)
				return NORTH;
		}

		return sourceFacing;
	}

	/**
	 * Returns true if block state has the FACING property defined.
	 * 
	 * @param state block state to test.
	 * 
	 * @return true if block state has the FACING property defined.
	 */
	public static boolean hasFacingProperty(BlockState state) {
		ImmutableMap<IProperty<?>, Comparable<?>> properties = state.getProperties();
		return properties.containsKey(FACING);
	}

	/**
	 * Returns true if the set of blocks is all of type air.
	 * 
	 * @param blocks set of blocks to query.
	 * @return true if the set of blocks is all of type air.
	 */
	public static boolean containsAirBlocksOnly(Iterable<BlockPos> blocks, WorldQuery worldQuery) {
		for (BlockPos blockPos : blocks) {

			// get block
			Block block = getBlockFromPosition(blockPos, worldQuery);

			// exit if block isn't of type air
			if (block != Blocks.AIR)
				return false;
		}
		return true;
	}

	/**
	 * Calculate block position that was hit by ray tracing result.
	 * 
	 * @param result ray trace result.
	 * 
	 * @return block position that was hit.
	 */
	public static BlockPos calculatePosition(BlockRayTraceResult result) {

		switch (result.getFace()) {

		case UP:
			return result.getPos().up();
		case DOWN:
			return result.getPos().down();

		case SOUTH:
			return result.getPos().south();

		case NORTH:
			return result.getPos().north();

		case EAST:
			return result.getPos().east();

		case WEST:
			return result.getPos().west();

		default:
			return result.getPos().up();
		}
	}

	/**
	 * Add temporary block.
	 * 
	 * @param world     world object.
	 * @param pos       position where temporary block should be spawned.
	 * @param tempBlock temporary block to set.
	 * @param duration  duration in game ticks for temporary block to exist.
	 */
	public static void setTemporaryBlock(World world, BlockPos pos, Block tempBlock, int duration) {

		// create temporary block
		BlockDirective tempDirective = new BlockDirective(pos, tempBlock, DONT_HARVEST);
		setTemporaryBlock(world, tempDirective, duration);
	}

	/**
	 * Add temporary block.
	 * 
	 * @param world         world object.
	 * @param tempDirective temporary block directive where temporary block should
	 *                      be spawned.
	 * @param duration      duration in game ticks for temporary block to exist.
	 */
	public static void setTemporaryBlock(World world, BlockDirective tempDirective, int duration) {

		// create original block
		Block block = BlockUtils.getBlockFromPosition(tempDirective.getBlockPosition(), world);
		BlockDirective orgDirective = new BlockDirective(tempDirective.getBlockPosition(), block, DONT_HARVEST);

		// create temporary block
		TemporaryBlock temporaryBlock = DefaultTemporaryBlock.getInstance(duration, tempDirective, orgDirective);
		TemporaryBlockRepository tempBlockRepository = getBassebombeCraft().getTemporaryBlockRepository();
		tempBlockRepository.add(temporaryBlock);
	}

	/**
	 * Select rainbow colored wool block.
	 * 
	 * @param colorCounter current color counter between 0..8.
	 * 
	 * @return rainbow colored wool block.
	 */
	public static BlockState selectRainbowColoredWool(int colorCounter) {
		int colorSelector = colorCounter % NUMBER_RAINBOW_COLORS;

		switch (colorSelector) {

		case 0:
			return Blocks.MAGENTA_WOOL.getDefaultState();
		case 1:
			return Blocks.PURPLE_WOOL.getDefaultState();
		case 2:
			return Blocks.BLUE_WOOL.getDefaultState();
		case 3:
			return Blocks.LIGHT_BLUE_WOOL.getDefaultState();
		case 4:
			return Blocks.LIME_WOOL.getDefaultState();
		case 5:
			return Blocks.YELLOW_WOOL.getDefaultState();
		case 6:
			return Blocks.ORANGE_WOOL.getDefaultState();
		case 7:
			return Blocks.RED_WOOL.getDefaultState();
		default:
			return Blocks.WHITE_WOOL.getDefaultState();
		}
	}

	/**
	 * Select pink colored wool block.
	 * 
	 * @param colorCounter current color counter between 0..1.
	 * 
	 * @return pink colored wool block.
	 */
	public static BlockState selectPinkColoredWool(int colorCounter) {
		int colorSelector = colorCounter % NUMBER_RAINBOW_COLORS;

		switch (colorSelector) {

		case 0:
			return Blocks.PINK_WOOL.getDefaultState();
		case 1:
			return Blocks.MAGENTA_WOOL.getDefaultState();
		default:
			return Blocks.PINK_WOOL.getDefaultState();
		}
	}

}
