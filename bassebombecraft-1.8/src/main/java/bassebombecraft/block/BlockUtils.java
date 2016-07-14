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
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Block utilities.
 */
public class BlockUtils {

	/**
	 * Default FACING property for querying about the property.
	 */
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	/**
	 * Don't harvest temporary block.
	 */
	public static final boolean DONT_HARVEST = false;

	/**
	 * Create single block of designated block type.
	 * 
	 * The block is only processed if the world isn't located at client side.
	 * 
	 * The block is only harvested if the block directive specifies it.
	 * 
	 * @param blockDirective
	 *            block directive for block to create.
	 * @param worldQuery
	 *            world query object.
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
			IBlockState blockState = getBlockStateFromPosition(blockPosition, worldQuery);
			block.harvestBlock(worldQuery.getWorld(), worldQuery.getPlayer(), blockPosition, blockState,
					NULL_TILE_ENTITY);
		}

		// set block state
		world.setBlockState(blockPosition, blockDirective.getState());
	}

	/**
	 * Get block from block position.
	 * 
	 * @param blockPosition
	 *            position of the block.
	 * @param world
	 *            world object.
	 * @return block located at block position
	 */
	public static Block getBlockFromPosition(BlockPos blockPosition, World world) {
		IBlockState blockState = world.getBlockState(blockPosition);
		return blockState.getBlock();
	}

	/**
	 * Get block from block position.
	 * 
	 * @param blockPosition
	 *            position of the block.
	 * @param worldQuery
	 *            world query object.
	 * @return block located at block position
	 */
	public static Block getBlockFromPosition(BlockPos blockPosition, WorldQuery worldQuery) {
		World world = worldQuery.getWorld();
		return getBlockFromPosition(blockPosition, world);
	}

	/**
	 * Get block defined at position specified by block directive.
	 * 
	 * @param blockDirective
	 *            directive to get the block from.
	 * @param worldQuery
	 *            world query object.
	 * @return block defined at position specified by block directive.
	 */
	public static Block getBlockFromPosition(BlockDirective blockDirective, WorldQuery worldQuery) {
		BlockPos blockPosition = blockDirective.getBlockPosition();
		return getBlockFromPosition(blockPosition, worldQuery);
	}

	/**
	 * Get block state from block position.
	 * 
	 * @param blockPosition
	 *            position of the block.
	 * @param worldQuery
	 *            world query object.
	 * @return block state located at block position
	 */
	public static IBlockState getBlockStateFromPosition(BlockPos blockPosition, WorldQuery worldQuery) {
		World world = worldQuery.getWorld();
		return world.getBlockState(blockPosition);
	}

	/**
	 * Rotate block state FACING property.
	 * 
	 * If block state doesn't have the FACING property defined then the source
	 * state is returned unchanged.
	 * 
	 * The orientation is defined in degrees and only the values 0, 90, 180 and
	 * 270 are processed. For all other values the FACING property is returned
	 * unchanged.
	 * 
	 * @param source
	 *            State source block state.
	 * @param orientation
	 *            orientation in degrees that the block state should be rotated.
	 * 
	 * @return rotated block state where the FACING property is updated.
	 */
	public static IBlockState rotateBlockStateWithFacingProperty(IBlockState sourceState, double orientation) {

		// exit if angle is zero
		if (orientation == 0)
			return sourceState;

		// exit if block state doesn't have the FACING property defined.
		// source state is returned unchanged
		if (!hasFacingProperty(sourceState))
			return sourceState;

		// get facing property
		ImmutableMap properties = sourceState.getProperties();
		EnumFacing facing = sourceState.getValue(FACING);

		// calculate new orientation
		EnumFacing value = calculateFacingProperty(facing, orientation);

		// create now rotated state
		IBlockState rotatedState = sourceState.withProperty(FACING, value);
		return rotatedState;
	}

	/**
	 * Calculate FACING property from orientation and source property.
	 * 
	 * The orientation is defined in degrees and only the values 0, 90, 180 and
	 * 270 are processed. For all other values the FACING property is returned
	 * unchanged.
	 * 
	 * @param sourceFacing
	 *            source FACING property.
	 * @param orientation
	 *            orientation in degrees to rotate the property.
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
	 * @param state
	 *            block state to test.
	 * 
	 * @return true if block state has the FACING property defined.
	 */
	public static boolean hasFacingProperty(IBlockState state) {
		ImmutableMap properties = state.getProperties();
		return properties.containsKey(FACING);
	}

	/**
	 * Returns true if the set of blocks is all of type air.
	 * 
	 * @param blocks
	 *            set of blocks to query.
	 * @return true if the set of blocks is all of type air.
	 */
	public static boolean containsAirBlocksOnly(Iterable blocks, WorldQuery worldQuery) {
		for (Object blockPos : blocks) {

			// type cast
			BlockPos typedBlockPos = (BlockPos) blockPos;

			// get block
			Block block = getBlockFromPosition(typedBlockPos, worldQuery);

			// exit if block isn't of type air
			if (block != Blocks.air)
				return false;
		}
		return true;
	}

	/**
	 * Add temporary block.
	 * 
	 * @param world
	 *            world object.
	 * @param pos
	 *            position where temporary block should be spawned.
	 * @param tempBlock
	 *            temporary block to set.
	 * @param duration
	 *            duration in game ticks for temporary block to exist.
	 */
	public static void setTemporaryBlock(World world, BlockPos pos, Block tempBlock, int duration) {

		// create temporary block
		BlockDirective tempDirective = new BlockDirective(pos, tempBlock, DONT_HARVEST);
		Block block = BlockUtils.getBlockFromPosition(pos, world);

		// create original block
		BlockDirective orgDirective = new BlockDirective(pos, block, DONT_HARVEST);

		// create temporary block
		TemporaryBlock temporaryBlock = DefaultTemporaryBlock.getInstance(duration, tempDirective, orgDirective);
		TemporaryBlockRepository tempBlockRepository = getBassebombeCraft().getTemporaryBlockRepository();
		tempBlockRepository.add(temporaryBlock);
	}

}
