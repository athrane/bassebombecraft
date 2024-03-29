package bassebombecraft.geom;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.ORIGIN_BLOCK_POS;
import static bassebombecraft.block.BlockUtils.containsNonAirBlocks;
import static bassebombecraft.block.BlockUtils.getBlockFromPosition;
import static bassebombecraft.block.BlockUtils.getBlockStateFromPosition;
import static bassebombecraft.block.BlockUtils.rotateBlockStateWithFacingProperty;
import static bassebombecraft.geom.BlockDirective.getInstance;
import static bassebombecraft.player.PlayerDirection.East;
import static bassebombecraft.player.PlayerDirection.North;
import static bassebombecraft.player.PlayerDirection.South;
import static bassebombecraft.player.PlayerDirection.West;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static net.minecraft.world.level.block.Blocks.ALLIUM;
import static net.minecraft.world.level.block.Blocks.AZURE_BLUET;
import static net.minecraft.world.level.block.Blocks.BLUE_ORCHID;
import static net.minecraft.world.level.block.Blocks.CORNFLOWER;
import static net.minecraft.world.level.block.Blocks.DANDELION;
import static net.minecraft.world.level.block.Blocks.LILY_OF_THE_VALLEY;
import static net.minecraft.world.level.block.Blocks.ORANGE_TULIP;
import static net.minecraft.world.level.block.Blocks.OXEYE_DAISY;
import static net.minecraft.world.level.block.Blocks.PINK_TULIP;
import static net.minecraft.world.level.block.Blocks.POPPY;
import static net.minecraft.world.level.block.Blocks.RED_TULIP;
import static net.minecraft.world.level.block.Blocks.WHITE_TULIP;

import java.awt.geom.AffineTransform;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import bassebombecraft.player.PlayerDirection;
import bassebombecraft.player.PlayerUtils;
import bassebombecraft.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

/**
 * Utility for calculating geometric structures.
 */
public class GeometryUtils {

	public static final int DEGREES_0 = 0;
	public static final int DEGREES_90 = 90;
	public static final int DEGREES_180 = 180;
	public static final int DEGREES_270 = 270;

	/**
	 * Vertical blocks to query for a "ground block".
	 */
	public static final int ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK = 256;

	/**
	 * Capture block directives from set of blocks defined in the game world.
	 * 
	 * Block and block state are captured individually for each block position.
	 * 
	 * The block directive is configured not to harvest the target block when
	 * processed.
	 * 
	 * @param blocks         stream of {@linkplain BlockPos} to capture.
	 * @param world          world where block information is queried.
	 * @param destCollection destination collection where
	 *                       {@linkplain BlockDirective} is added to.
	 */
	public static void captureBlockDirectives(Stream<BlockPos> blocks, Level world,
			Collection<BlockDirective> destCollection) {
		blocks.map(p -> getInstance(p, getBlockFromPosition(p, world), getBlockStateFromPosition(p, world),
				DONT_HARVEST, world)).forEach(destCollection::add);
	}

	/**
	 * Calculate block directives from set of blocks (to support creation of the
	 * block in the game world).
	 * 
	 * Block and block state are created individually for each block position.
	 * 
	 * @param blocks     stream of {@linkplain BlockPos} to create block directives
	 *                   from.
	 * @param block      block to create all block directives with.
	 * @param blockState block state to create all block directives with.
	 * @param harvest    define whether existing blocks should be harvested.
	 * @param player     player which initiated the creation of the blocks
	 * 
	 * @return result list of calculated {@linkplain BlockDirective}.
	 */
	public static List<BlockDirective> calculateBlockDirectives(Stream<BlockPos> blocks, Block block,
			BlockState blockState, boolean harvest, Player player) {
		return blocks.map(p -> getInstance(p, block, blockState, harvest, player)).collect(Collectors.toList());
	}

	/**
	 * Calculate block directives from set of blocks (to support creation of the
	 * block in the game world).
	 * 
	 * Block and block state are created individually for each block position.
	 * 
	 * @param blocks     stream of {@linkplain BlockPos} to create block directives
	 *                   from.
	 * @param block      block to create all block directives with.
	 * @param blockState block state to create all block directives with.
	 * @param harvest    define whether existing blocks should be harvested.
	 * @param world      world where block information is queried.
	 * 
	 * @return result list of calculated {@linkplain BlockDirective}.
	 */
	public static List<BlockDirective> calculateBlockDirectives(Stream<BlockPos> blocks, Block block,
			BlockState blockState, boolean harvest, Level world) {
		return blocks.map(p -> getInstance(p, block, blockState, harvest, world)).collect(Collectors.toList());
	}

	/**
	 * Rotate coordinates of a set of block directives in source set around the Y
	 * axis.
	 * 
	 * Creates a new set of block directives.
	 * 
	 * @param offset offset block position containing the X and Z coordinate to
	 *               rotate around.
	 * @param angle  angle to rotate coordinates.
	 * @param source list of source coordinates.
	 * 
	 * @return list containing the rotated coordinates
	 */
	public static List<BlockDirective> rotateCoordinatesAroundYAxis(BlockPos offset, double angle,
			List<BlockDirective> source) {
		List<BlockDirective> result = new ArrayList<BlockDirective>();
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(angle), offset.getX(),
				offset.getZ());

		for (BlockDirective sourceDirective : source) {
			double[] rotationPoint = { sourceDirective.getBlockPosition().getX(),
					sourceDirective.getBlockPosition().getZ() };
			transform.transform(rotationPoint, 0, rotationPoint, 0, 1);

			// create rotated directive
			BlockPos rotatedPosition = new BlockPos((int) rotationPoint[0], sourceDirective.getBlockPosition().getY(),
					(int) rotationPoint[1]);
			BlockDirective rotatedDirective = getInstance(sourceDirective, rotatedPosition);

			getInstance(rotatedPosition, sourceDirective.getBlock(), sourceDirective.harvestBlock(),
					sourceDirective.getWorld());

			// add block state to rotated directive
			BlockState sourceState = sourceDirective.getState();
			BlockState rotatedState = rotateBlockStateWithFacingProperty(sourceState, angle);
			rotatedDirective.setState(rotatedState);
			result.add(rotatedDirective);
		}
		return result;
	}

	/**
	 * Rotate coordinates around the Y axis at origin, e.g. X/Z origin coordinates.
	 * 
	 * @param angle  angle to rotate coordinates.
	 * @param source list of source coordinates.
	 * 
	 * @return list containing the rotated coordinates
	 */
	public static List<BlockDirective> rotateCoordinatesAroundYAxisAtOrigin(double angle, List<BlockDirective> source) {
		return rotateCoordinatesAroundYAxis(ORIGIN_BLOCK_POS, angle, source);
	}

	/**
	 * Rotate unit vector coordinates around the Y axis at origin.
	 * 
	 * @param angle  angle to rotate coordinates.
	 * @param vector vector which is rotated.
	 * 
	 * @return vector rotated around the Y-axis at origin.
	 */
	public static Vec3 rotateUnitVectorAroundYAxisAtOrigin(double angle, Vec3 vector) {
		double originX = 0;
		double originZ = 0;
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(angle), originX, originZ);

		double[] rotationPoint = { vector.x, vector.z };
		transform.transform(rotationPoint, 0, rotationPoint, 0, 1);
		return new Vec3(rotationPoint[0], vector.y, rotationPoint[1]);
	}

	/**
	 * Private method for calculation of list of {@linkplain BlockDirective} from a
	 * child structure.
	 * 
	 * One block directive is added for each block position.
	 * 
	 * The rectangle is defined by a global offset (x,z,y) and dimensions (width,
	 * height, depth).
	 * 
	 * The rectangle (e.g. list of directives) is rotated depending on the player
	 * direction.
	 * 
	 * @param offset    offset block position.
	 * @param player    player which initiated the creation of the blocks
	 * @param structure structure which defines the local offset, size and block
	 *                  characteristics of the created rectangle.
	 * @param harvest   define whether existing blocks should be harvested.
	 * 
	 * @return list of block directive (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	private static List<BlockDirective> calculateBlockDirectivesFromChildStructure(BlockPos offset, Player player,
			Structure structure, boolean harvest) {

		// exit if structure is a composite
		if (structure.isComposite())
			return new ArrayList<BlockDirective>();

		// calculate block positions
		BlockPos from = offset.offset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
		int xTo = structure.getSizeX() - 1;
		int yTo = structure.getSizeY() - 1;
		int zTo = structure.getSizeZ() - 1;
		BlockPos to = from.offset(xTo, yTo, zTo);
		Stream<BlockPos> blocks = BlockPos.betweenClosedStream(from, to);

		// calculate block directives
		List<BlockDirective> directives = calculateBlockDirectives(blocks, structure.getBlock(),
				structure.getBlockState(), harvest, player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		int rotationDegrees = calculateDegreesFromPlayerDirection(playerDirection);
		List<BlockDirective> rotatedDirectives = rotateCoordinatesAroundYAxis(offset, rotationDegrees, directives);
		return rotatedDirectives;
	}

	/**
	 * Private method for calculation of list of {@linkplain BlockDirective} from a
	 * child structure.
	 * 
	 * One block directive is added for each block position.
	 * 
	 * The rectangle is defined by a global offset (x,z,y) and dimensions (width,
	 * height, depth).
	 * 
	 * The rectangle (e.g. list of directives) is rotated depending on the player
	 * direction.
	 * 
	 * @param offset          offset block position.
	 * @param playerDirection player direction which controls the rotation of the
	 *                        coordinates.
	 * @param structure       structure which defines the local offset, size and
	 *                        block characteristics of the created rectangle.
	 * @param harvest         define whether existing blocks should be harvested.
	 * @param world           world where block directive are created.
	 * 
	 * @return list of block directive (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	private static List<BlockDirective> calculateBlockDirectivesFromChildStructure(BlockPos offset,
			PlayerDirection playerDirection, Structure structure, boolean harvest, Level world) {

		// exit if structure is a composite
		if (structure.isComposite())
			return new ArrayList<BlockDirective>();

		// calculate block positions
		BlockPos from = offset.offset(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
		int xTo = structure.getSizeX() - 1;
		int yTo = structure.getSizeY() - 1;
		int zTo = structure.getSizeZ() - 1;
		BlockPos to = from.offset(xTo, yTo, zTo);
		Stream<BlockPos> blocks = BlockPos.betweenClosedStream(from, to);

		// calculate block directives
		List<BlockDirective> directives = calculateBlockDirectives(blocks, structure.getBlock(),
				structure.getBlockState(), harvest, world);

		int rotationDegrees = calculateDegreesFromPlayerDirection(playerDirection);
		List<BlockDirective> rotatedDirectives = rotateCoordinatesAroundYAxis(offset, rotationDegrees, directives);
		return rotatedDirectives;
	}

	/**
	 * Calculate block directives (e.g. coordinates) from structure from global
	 * offset block position.
	 * 
	 * The structure is rotated depending on player direction.
	 * 
	 * @param offset    global offset.
	 * @param player    player which initiated the creation of the blocks
	 * @param structure structure which defines the size of the created rectangle.
	 * @param harvest   define whether existing blocks should be harvested.
	 * 
	 * @return list of block directives (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	public static List<BlockDirective> calculateBlockDirectives(BlockPos offset, Player player, Structure structure,
			boolean harvest) {

		// handle child structure
		if (!structure.isComposite()) {
			return calculateBlockDirectivesFromChildStructure(offset, player, structure, harvest);
		}

		// handle composite structure
		List<BlockDirective> compositeResult = new ArrayList<BlockDirective>();
		for (Structure child : structure.getChildren()) {
			compositeResult.addAll(calculateBlockDirectives(offset, player, child, harvest));
		}
		return compositeResult;
	}

	/**
	 * Calculate block directives (e.g. coordinates) from structure from global
	 * offset block position.
	 * 
	 * The structure is rotated depending on player direction.
	 * 
	 * @param offset          global offset.
	 * @param playerDirection player direction which controls the rotation of the
	 *                        coordinates.
	 * @param structure       structure which defines the size of the created
	 *                        rectangle.
	 * @param harvest         define whether existing blocks should be harvested.
	 * @param world           world where blocks are captured from.
	 * 
	 * @return list of block directives (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	public static List<BlockDirective> calculateBlockDirectives(BlockPos offset, PlayerDirection playerDirection,
			Structure structure, boolean harvest, Level world) {

		// handle child structure
		if (!structure.isComposite()) {
			return calculateBlockDirectivesFromChildStructure(offset, playerDirection, structure, harvest, world);
		}

		// handle composite structure
		List<BlockDirective> compositeResult = new ArrayList<BlockDirective>();
		for (Structure child : structure.getChildren()) {
			compositeResult.addAll(calculateBlockDirectives(offset, playerDirection, child, harvest, world));
		}
		return compositeResult;
	}

	/**
	 * Calculate degrees from player direction.
	 * 
	 * @param playerDirection player direction from which the degrees are
	 *                        calculated.
	 * 
	 * @return calculated degrees from player direction.
	 */
	public static int calculateDegreesFromPlayerDirection(PlayerDirection playerDirection) {
		switch (playerDirection) {

		case South:
			return DEGREES_0;
		case West:
			return DEGREES_90;
		case North:
			return DEGREES_180;
		case East:
			return DEGREES_270;
		default:
			return DEGREES_0;
		}
	}

	/**
	 * Convert {@linkplain Direction} to {@linkplain PlayerDirection}.
	 * 
	 * @param direction direction value
	 * 
	 * @return player direction value.
	 */
	public static PlayerDirection convertToPlayerDirection(Direction direction) {
		switch (direction) {
		case EAST:
			return East;
		case WEST:
			return West;
		case NORTH:
			return North;
		case SOUTH:
			return South;
		default:
			return East;
		}
	}

	/**
	 * Calculate y offset from block.
	 * 
	 * @param player
	 * @param blockPosition
	 * 
	 * @return y offset from block.
	 */
	public static int calculateYOffsetFromBlock(Player player, BlockPos blockPosition) {
		int yFeetPosition = PlayerUtils.calculatePlayerFeetPosititionAsInt(player);
		int offset = blockPosition.getY() - yFeetPosition;
		return offset;
	}

	/**
	 * Capture rectangle as list of {@linkplain BlockDirective}. One block directive
	 * is captured for each block position.
	 * 
	 * The rectangle is defined by a global offset (x,z,y) and dimensions (width,
	 * height, depth)
	 * 
	 * @param offset capture offset.
	 * @param size   capture size.
	 * @param world  world to capture the blocks from.
	 * 
	 * @return list of coordinates for the blocks in the structure.
	 */
	public static List<BlockDirective> captureRectangle(BlockPos offset, BlockPos size, Level world) {

		List<BlockDirective> result = new ArrayList<BlockDirective>();
		int yCounter = 0;

		while (true) {

			// calculate current y coordinate
			int currentY = yCounter + offset.getY();

			// calculate set of block positions for y-layer
			BlockPos from = new BlockPos(offset.getX(), currentY, offset.getZ());
			int layerXDelta = size.getX() - 1;
			int layerYDelta = 0;
			int layerZDelta = size.getZ() - 1;
			BlockPos to = from.offset(layerXDelta, layerYDelta, layerZDelta);
			Stream<BlockPos> blocks = BlockPos.betweenClosedStream(from, to);

			// exit if blocks is of type air only
			if (!containsNonAirBlocks(blocks, world))
				return result;

			// add blocks from this layer
			blocks = BlockPos.betweenClosedStream(from, to);
			captureBlockDirectives(blocks, world, result);

			// increase layer
			yCounter++;
		}
	}

	/**
	 * Translate set of block directives using translation vector.
	 * 
	 * @param translationVector translation vector
	 * @param directives        set of block directives.
	 * 
	 * @return translated list of block directives.
	 */
	public static List<BlockDirective> translate(BlockPos translationVector, List<BlockDirective> directives) {
		List<BlockDirective> translated = new ArrayList<BlockDirective>();

		for (BlockDirective directive : directives) {
			BlockDirective translatedDirective = getInstance(directive);
			translatedDirective.translate(translationVector);
			translated.add(translatedDirective);
		}
		return translated;
	}

	/**
	 * Calculate spiral.
	 * 
	 * @param maxX maximum x coordinate.
	 * @param maxY maximum y coordinate
	 * @return list of block positions with x/z coordinates defined. the Y
	 *         coordinate is 0 for all block positions.
	 */
	public static List<BlockPos> calculateSpiral(int maxX, int maxY) {
		List<BlockPos> coords = new ArrayList<BlockPos>();

		int x = 0, y = 0, dx = 0, dy = -1;
		int t = Math.max(maxX, maxY);
		int maxI = t * t;

		for (int i = 0; i < maxI; i++) {
			if ((-maxX / 2 <= x) && (x <= maxX / 2) && (-maxY / 2 <= y) && (y <= maxY / 2)) {

				// add x/z coordinate with null y coordinate
				BlockPos pos = new BlockPos(x, 0, y);
				coords.add(pos);
			}

			if ((x == y) || ((x < 0) && (x == -y)) || ((x > 0) && (x == 1 - y))) {
				t = dx;
				dx = -dy;
				dy = t;
			}
			x += dx;
			y += dy;
		}

		return coords;
	}

	/**
	 * Locate ground block, i.e. a block with air/water above and solid ground
	 * below.
	 * 
	 * If block is air/water block then move down until ground block is located. If
	 * block is ground block then move up until block above air/water.
	 * 
	 * @param target     block to process.
	 * @param iterations number of blocks to query before returning null. Should be
	 *                   a positive integer.
	 * 
	 * @param world      world object.
	 * @return ground block, i.e. a block with air/water above and solid ground
	 *         below. If no block was found then original block position is
	 *         returned.
	 */
	public static BlockPos locateGroundBlockPos(BlockPos target, int iterations, Level world) {
		if (iterations == 0)
			return target;
		if (iterations < 0)
			return target;

		int newIterations = iterations - 1;

		// if upper block isn't useful - then move up
		if (!isUsefullAirTypeBlock(target.above(), world)) {
			return locateGroundBlockPos(target.above(), newIterations, world);
		}

		// if upper is OK - but current isn't - then move down
		if (!isUsefulGroundBlock(target, world)) {
			return locateGroundBlockPos(target.below(), newIterations, world);
		}

		return target;
	}

	/**
	 * Returns true if block is a useful "ground" type block, i.e. NOT liquid, air,
	 * plants or grass.
	 * 
	 * @param target target block.
	 * @param world  world object
	 * @return
	 */
	static boolean isUsefulGroundBlock(BlockPos target, Level world) {
		Block block = getBlockFromPosition(target, world);
		BlockState defaultState = block.defaultBlockState();
		Material material = defaultState.getMaterial();
		return (material.isSolid());
	}

	/**
	 * Returns true if block is a useful "air" type block, i.e. liquid, air, plants
	 * or grass.
	 * 
	 * @param target target block.
	 * @param world  world object
	 * 
	 * @return true if block is a useful "air" type block.
	 */
	static boolean isUsefullAirTypeBlock(BlockPos target, Level world) {
		Block block = getBlockFromPosition(target, world);
		BlockState defaultState = block.defaultBlockState();
		Material material = defaultState.getMaterial();
		return (!material.isSolid());
	}

	/**
	 * Creates block directive with flower.
	 * 
	 * @param position block position for flower.
	 * @param world    world where block directive is located.
	 */
	public static BlockDirective createFlowerDirective(BlockPos position, Level world) {
		Random random = getBassebombeCraft().getRandom();
		Block blockType = selectFlower(random);
		return getInstance(position, blockType, DONT_HARVEST, world);
	}

	/**
	 * Select flower.
	 * 
	 * @return Select flower type.
	 */
	static Block selectFlower(Random random) {
		int flowerType = random.nextInt(12);

		switch (flowerType) {

		case 0:
			return ALLIUM;
		case 1:
			return AZURE_BLUET;
		case 2:
			return BLUE_ORCHID;
		case 3:
			return CORNFLOWER;
		case 4:
			return DANDELION;
		case 5:
			return LILY_OF_THE_VALLEY;
		case 6:
			return ORANGE_TULIP;
		case 7:
			return OXEYE_DAISY;
		case 8:
			return PINK_TULIP;
		case 9:
			return POPPY;
		case 10:
			return RED_TULIP;
		case 11:
			return WHITE_TULIP;

		default:
			return RED_TULIP;
		}
	}

	/**
	 * Calculate the squared distance between two block positions.
	 * 
	 * @param pos1 first position
	 * @param pos2 second position.
	 * 
	 * @return the squared distance between two block positions
	 */
	public static double calculateDistanceSq(BlockPos pos1, BlockPos pos2) {
		double d0 = pos1.getX() - (double) pos2.getX();
		double d1 = pos1.getY() - (double) pos2.getY();
		double d2 = pos1.getZ() - (double) pos2.getZ();
		return d0 * d0 + d1 * d1 + d2 * d2;
	}

	/**
	 * Calculate the distance between two block positions.
	 * 
	 * @param pos1 first position
	 * @param pos2 second position.
	 * 
	 * @return the distance between two block positions
	 */
	public static double calculateDistance(BlockPos pos1, BlockPos pos2) {
		return Math.sqrt(calculateDistanceSq(pos1, pos2));
	}

	/**
	 * Oscillate value.
	 * 
	 * @param min minimum value.
	 * @param max maximum value.
	 * 
	 * @return oscillated value between min and max.
	 */
	public static double oscillate(float min, float max) {
		long time = Instant.now().toEpochMilli() / 10;
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}

	/**
	 * Oscillate float value.
	 * 
	 * @param min minimum value.
	 * @param max maximum value.
	 * 
	 * @return oscillated float value between min and max.
	 */
	public static float oscillateFloat(float min, float max) {
		return (float) oscillate(min, max);
	}

	/**
	 * Oscillate value.
	 * 
	 * @param timeDelta value added to time.
	 * @param min       minimum value.
	 * @param max       maximum value.
	 * 
	 * @return oscillated value between min and max.
	 */
	public static double oscillateWithDeltaTime(long timeDelta, float min, float max) {
		long time = (Instant.now().toEpochMilli() / 10) + timeDelta;
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}

	/**
	 * Oscillate value.
	 * 
	 * @param time time value.
	 * @param min  minimum value.
	 * @param max  maximum value.
	 * 
	 * @return oscillated value between min and max.
	 */
	public static double oscillateWithFixedTime(long time, float min, float max) {
		return min + (Math.sin(Math.toRadians(time)) + 1) / 2 * (max - min);
	}

}
