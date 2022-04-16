package bassebombecraft.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.NULL_TILE_ENTITY;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.world.WorldUtils.isLogicalClient;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

import java.util.Optional;
import java.util.stream.Stream;

import bassebombecraft.event.block.temporary.DefaultTemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlock;
import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import bassebombecraft.geom.BlockDirective;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;

/**
 * Block utilities.
 */
public class BlockUtils {

	/**
	 * Send block change to clients.
	 */
	static final int SEND_CHANGE_CLIENTS = 2;

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
	 * The block is only processed if the world is at SERVER side.
	 * 
	 * The block is only harvested if the block directive specifies it.
	 * 
	 * @param blockDirective block directive for block to create.
	 */
	public static void createBlock(BlockDirective blockDirective) {

		// get world
		Level world = blockDirective.getWorld();

		// exit if handler is executed at client side
		if (isLogicalClient(world))
			return;

		// get block
		BlockPos blockPosition = blockDirective.getBlockPosition();
		Block block = getBlockFromPosition(blockDirective, world);

		// harvest block
		if (blockDirective.harvestBlock()) {
			BlockState blockState = getBlockStateFromPosition(blockPosition, world);

			ItemStack emptyItemStack = new ItemStack(block);
			Optional<Player> optPlayer = blockDirective.getPlayer();

			// will only harvest if player is defined
			if (optPlayer.isPresent()) {
				block.playerDestroy(world, optPlayer.get(), blockPosition, blockState, NULL_TILE_ENTITY, emptyItemStack);
			} else {
				// log warning if player was undefined
				String msg = new StringBuilder().append("Attempted to harvest block directive=").append(blockDirective)
						.append("but player wasn't defined as expected").toString();
				getBassebombeCraft().reportAndLogError(msg);
			}
		}

		// set block state
		world.setBlock(blockPosition, blockDirective.getState(), SEND_CHANGE_CLIENTS);
	}

	/**
	 * Get block from block position.
	 * 
	 * @param blockPosition position of the block.
	 * @param world         world object.
	 * @return block located at block position
	 */
	public static Block getBlockFromPosition(BlockPos blockPosition, Level world) {
		BlockState blockState = world.getBlockState(blockPosition);
		return blockState.getBlock();
	}

	/**
	 * Get block defined at position specified by block directive.
	 * 
	 * @param blockDirective directive to get the block from.
	 * @param world          world object.
	 * 
	 * @return block defined at position specified by block directive.
	 */
	public static Block getBlockFromPosition(BlockDirective blockDirective, Level world) {
		BlockPos blockPosition = blockDirective.getBlockPosition();
		return getBlockFromPosition(blockPosition, world);
	}

	/**
	 * Get block state from block position.
	 * 
	 * @param blockPosition position of the block.
	 * @param world         world object.
	 * 
	 * @return block state located at block position
	 */
	public static BlockState getBlockStateFromPosition(BlockPos blockPosition, Level world) {
		return world.getBlockState(blockPosition);
	}

	/**
	 * Rotate block state FACING property.
	 * 
	 * If block state doesn't have the FACING property defined then the source state
	 * is returned unchanged.
	 * 
	 * The facing property is defined in multiple times in MC, i.e. in
	 * {@linkplain BlockStateProperties} and in {@linkplain StairsBlock} where is it
	 * a redefinition of {@linkplain BlockStateProperties.HORIZONTAL_FACING}
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
			Direction direction = sourceState.getValue(FACING);

			// calculate new orientation
			Direction newDirection = calculateFacingProperty(direction, orientation);

			// create now rotated state
			BlockState rotatedState = sourceState.setValue(FACING, newDirection);

			return rotatedState;
		}

		// rotate if block state have the HORIZONTAL_FACING property defined.
		if (hasHorizontalFacingProperty(sourceState)) {

			// get direction
			Direction direction = sourceState.getValue(HORIZONTAL_FACING);

			// calculate new orientation
			Direction newDirection = calculateFacingProperty(direction, orientation);

			// create now rotated state
			BlockState rotatedState = sourceState.setValue(HORIZONTAL_FACING, newDirection);

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
			return direction.getClockWise();
		}

		if (orientation == 180) {
			Direction d0 = direction.getClockWise();
			return d0.getClockWise();
		}

		if (orientation == 270) {
			Direction d0 = direction.getClockWise();
			Direction d1 = d0.getClockWise();
			return d1.getClockWise();
		}

		return direction;
	}

	/**
	 * Returns true if block state has the {@linkplain BlockStateProperties} .FACING
	 * property defined.
	 * 
	 * @param state block state to test.
	 * 
	 * @return true if block state has the FACING property defined.
	 */
	public static boolean hasFacingProperty(BlockState state) {
		return state.hasProperty(FACING);
	}

	/**
	 * Returns true if block state has the {@linkplain BlockStateProperties}
	 * .HORIZONTAL_FACING property defined.
	 * 
	 * @param state block state to test.
	 * 
	 * @return true if block state has the HORIZONTAL_FACING property defined.
	 */
	public static boolean hasHorizontalFacingProperty(BlockState state) {
		return state.hasProperty(HORIZONTAL_FACING);
	}

	/**
	 * Returns true if at least one block in the stream isn't of type air.
	 * 
	 * @param stream of blocks to query.
	 * @param world  world where query should be processed.
	 * 
	 * @return true if if at least one block in the stream isn't of type air.
	 */
	public static boolean containsNonAirBlocks(Stream<BlockPos> blocks, Level world) {
		return blocks.map(bp -> getBlockFromPosition(bp, world)).anyMatch(b -> b != Blocks.AIR);
	}

	/**
	 * Calculate block position that was hit by ray tracing result.
	 * 
	 * @param result ray trace result.
	 * 
	 * @return block position that was hit.
	 */
	public static BlockPos calculatePosition(BlockHitResult result) {

		switch (result.getDirection()) {

		case UP:
			return result.getBlockPos().above();
		case DOWN:
			return result.getBlockPos().below();

		case SOUTH:
			return result.getBlockPos().south();

		case NORTH:
			return result.getBlockPos().north();

		case EAST:
			return result.getBlockPos().east();

		case WEST:
			return result.getBlockPos().west();

		default:
			return result.getBlockPos().above();
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
	public static void setTemporaryBlock(Level world, BlockPos pos, Block tempBlock, int duration) {
		BlockDirective tempDirective = getInstance(pos, tempBlock, DONT_HARVEST, world);
		setTemporaryBlock(tempDirective, duration);
	}

	/**
	 * Add temporary block.
	 * 
	 * @param tempDirective temporary block directive where temporary block should
	 *                      be spawned.
	 * @param duration      duration in game ticks for temporary block to exist.
	 */
	public static void setTemporaryBlock(BlockDirective tempDirective, int duration) {

		// get world
		Level world = tempDirective.getWorld();

		// create original block
		Block block = BlockUtils.getBlockFromPosition(tempDirective.getBlockPosition(), world);
		BlockDirective orgDirective = getInstance(tempDirective.getBlockPosition(), block, DONT_HARVEST, world);

		// create temporary block
		TemporaryBlock temporaryBlock = DefaultTemporaryBlock.getInstance(duration, tempDirective, orgDirective);
		TemporaryBlockRepository repository = getProxy().getServerTemporaryBlockRepository();
		repository.add(temporaryBlock);
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
			return Blocks.MAGENTA_WOOL.defaultBlockState();
		case 1:
			return Blocks.PURPLE_WOOL.defaultBlockState();
		case 2:
			return Blocks.BLUE_WOOL.defaultBlockState();
		case 3:
			return Blocks.LIGHT_BLUE_WOOL.defaultBlockState();
		case 4:
			return Blocks.LIME_WOOL.defaultBlockState();
		case 5:
			return Blocks.YELLOW_WOOL.defaultBlockState();
		case 6:
			return Blocks.ORANGE_WOOL.defaultBlockState();
		case 7:
			return Blocks.RED_WOOL.defaultBlockState();
		default:
			return Blocks.WHITE_WOOL.defaultBlockState();
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
			return Blocks.PINK_WOOL.defaultBlockState();
		case 1:
			return Blocks.MAGENTA_WOOL.defaultBlockState();
		default:
			return Blocks.PINK_WOOL.defaultBlockState();
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

	/**
	 * Return true if block is a water block.
	 * 
	 * @param position block position
	 * @param world    world object.
	 * 
	 * @return true if block is a water block.
	 */
	public static boolean hasWater(BlockPos position, Level world) {
		BlockState state = world.getBlockState(position);
		return (state.getBlock() == Blocks.WATER);
	}

}
