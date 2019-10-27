package bassebombecraft.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NULL_TILE_ENTITY;
import static net.minecraft.state.properties.BlockStateProperties.*;

import java.util.Optional;
import java.util.stream.Stream;

import bassebombecraft.event.block.temporary.DefaultTemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * Block utilities.
 */
public class BlockUtils {

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
	 * The facing property is defined in multiple times in MC, i.e. in {@linkplain BlockStateProperties}
	 * and in {@linkplain StairsBlock} where is it a redefinition of {@linkplain BlockStateProperties.HORIZONTAL_FACING}
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
		
		// rotate if block state have the FACING property defined.
		if (hasFacingProperty(sourceState)) {
			
			// get direction 
			Direction direction = sourceState.get(FACING);
			
			// calculate new orientation
			Direction newDirection = calculateFacingProperty(direction, orientation);
			
			// create now rotated state
			BlockState rotatedState = sourceState.with(FACING, newDirection);

			return rotatedState;			
		}

		// rotate if block state have the HORIZONTAL_FACING property defined.
		if (hasHorizontalFacingProperty(sourceState)) {
			
			// get direction 
			Direction direction = sourceState.get(HORIZONTAL_FACING);
			
			// calculate new orientation
			Direction newDirection = calculateFacingProperty(direction, orientation);
			
			// create now rotated state
			BlockState rotatedState = sourceState.with(HORIZONTAL_FACING, newDirection);
			
			return rotatedState;			
		}
		
		// return block state unchanged
		return sourceState;		
	}

	
	/**
	 * Calculate direction property from orientation and source property.
	 * 
	 * The orientation is defined in degrees and only the values 0, 90, 180 and 270
	 * are processed. For all other values the FACING property is returned
	 * unchanged.
	 * 
	 * @param direction   source FACING direction.
	 * @param orientation orientation in degrees to rotate the property.
	 * @return rotated facing property.
	 */
	public static Direction calculateFacingProperty(Direction direction, double orientation) {
		if (orientation == 0)
			return direction;

		if (orientation == 90) {
			return direction.rotateY();
		}

		if (orientation == 180) {
			Direction d0 = direction.rotateY();
			return d0.rotateY();
		}

		if (orientation == 270) {
			Direction d0 = direction.rotateY();
			Direction d1 = d0.rotateY();
			return d1.rotateY();
		}

		
		return direction;
	}

	/**
	 * Returns true if block state has the {@linkplain BlockStateProperties} .FACING property defined.
	 * 
	 * @param state block state to test.
	 * 
	 * @return true if block state has the FACING property defined.
	 */
	public static boolean hasFacingProperty(BlockState state) {
		return state.has(FACING);
	}

	/**
	 * Returns true if block state has the {@linkplain BlockStateProperties} .HORIZONTAL_FACING property defined.
	 * 
	 * @param state block state to test.
	 * 
	 * @return true if block state has the HORIZONTAL_FACING property defined.
	 */
	public static boolean hasHorizontalFacingProperty(BlockState state) {
		return state.has(HORIZONTAL_FACING);
	}
	
	/**
	 * Returns true if the set of blocks are all of type air.
	 * 
	 * @param stream of blocks to query.
	 * 
	 * @return true if the blocks are all of type air.
	 */
	public static boolean containsAirBlocksOnly(Stream<BlockPos> blocks, WorldQuery worldQuery) {
		return blocks.map(bp -> getBlockFromPosition(bp, worldQuery)).anyMatch(b -> b != Blocks.AIR);
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

	/**
	 * Return true if object is a {@linkplain BlockDirective}.
	 * 
	 * @param obj object to test.
	 * 
	 * @return true if entity is a {@linkplain BlockDirective}.
	 */
	public static boolean isTypeBlockDirective(Object obj) {
		Optional<Object> oe = Optional.ofNullable(obj);
		if (oe.isPresent())
			return oe.get() instanceof BlockDirective;
		return false;
	}
	
}
