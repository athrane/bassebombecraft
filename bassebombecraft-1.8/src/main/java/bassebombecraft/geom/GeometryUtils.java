package bassebombecraft.geom;

import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.ModConstants.HARVEST;
import static bassebombecraft.ModConstants.ORIGIN_BLOCK_POS;
import static bassebombecraft.block.BlockUtils.containsAirBlocksOnly;
import static bassebombecraft.block.BlockUtils.getBlockFromPosition;
import static bassebombecraft.block.BlockUtils.getBlockStateFromPosition;
import static bassebombecraft.block.BlockUtils.rotateBlockStateWithFacingProperty;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bassebombecraft.player.PlayerDirection;
import bassebombecraft.player.PlayerUtils;
import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
	 * @param blockPosSet
	 *            set of {@linkplain BlockPos} to capture.
	 * @param worldQuery
	 *            world query object.
	 * @param result
	 *            list of captured {@linkplain BlockDirective}.
	 */
	public static List<BlockDirective> captureBlockDirectives(Iterable blockPosSet, WorldQuery worldQuery) {

		List<BlockDirective> result = new ArrayList<BlockDirective>();

		// capture directives
		for (Object blockPos : blockPosSet) {

			// type cast
			BlockPos typedBlockPos = (BlockPos) blockPos;

			// create directive
			Block block = getBlockFromPosition(typedBlockPos, worldQuery);
			IBlockState blockState = getBlockStateFromPosition(typedBlockPos, worldQuery);
			BlockDirective directive = new BlockDirective(typedBlockPos, block, DONT_HARVEST);
			directive.setState(blockState);
			result.add(directive);
		}

		return result;
	}

	/**
	 * Calculate block directives from set of blocks (to support creation of the
	 * block in the game world).
	 * 
	 * Block and block state are created individually for each block position.
	 * 
	 * The block directive is configured to harvest the target block when
	 * processed.
	 * 
	 * @param blockPosSet
	 *            set of {@linkplain BlockPos} to create block directives from..
	 * @param block
	 *            block to create all block directives with.
	 * @param blockState
	 *            block state to create all block directives with.
	 * @param result
	 *            list of calculated {@linkplain BlockDirective}.
	 */
	public static List<BlockDirective> calculateBlockDirectives(Iterable blockPosSet, Block block,
			IBlockState blockState) {

		List<BlockDirective> result = new ArrayList<BlockDirective>();

		// create directives
		for (Object blockPos : blockPosSet) {

			// type cast
			BlockPos typedBlockPos = (BlockPos) blockPos;

			// create directive
			BlockDirective directive = new BlockDirective(typedBlockPos, block, HARVEST);
			directive.setState(blockState);
			result.add(directive);
		}

		return result;
	}

	/**
	 * Rotate coordinates of a set of block directives in source set around the
	 * Y axis.
	 * 
	 * Creates a new set of block directives.
	 * 
	 * @param offset
	 *            offset block position containing the X and Z coordinate to
	 *            rotate around.
	 * @param angle
	 *            angle to rotate coordinates.
	 * @param source
	 *            list of source coordinates.
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
			BlockPos RotatedPosition = new BlockPos((int) rotationPoint[0], sourceDirective.getBlockPosition().getY(),
					(int) rotationPoint[1]);
			BlockDirective rotatedDirective = new BlockDirective(RotatedPosition, sourceDirective.block);

			// add block state to rotated directive
			IBlockState sourceState = sourceDirective.getState();
			IBlockState rotatedState = rotateBlockStateWithFacingProperty(sourceState, angle);

			rotatedDirective.setState(rotatedState);
			result.add(rotatedDirective);
		}
		return result;
	}

	/**
	 * Rotate coordinates around the Y axis at origin, e.g. X/Z origin
	 * coordinates.
	 * 
	 * @param angle
	 *            angle to rotate coordinates.
	 * @param source
	 *            list of source coordinates.
	 * 
	 * @return list containing the rotated coordinates
	 */
	public static List<BlockDirective> rotateCoordinatesAroundYAxisAtOrigin(double angle, List<BlockDirective> source) {
		return rotateCoordinatesAroundYAxis(ORIGIN_BLOCK_POS, angle, source);
	}

	/**
	 * Rotate unit vector coordinates around the Y axis at origin.
	 * 
	 * @param offset
	 *            offset block position containing the X and Z coordinate to
	 *            rotate around.
	 * @param angle
	 *            angle to rotate coordinates.
	 * @param vector
	 *            vector which is rotated.
	 * 
	 * @return vector rotated around the Y-axis at origin.
	 */
	public static Vec3d rotateUnitVectorAroundYAxisAtOrigin(double angle, Vec3d vector) {
		double originX = 0;
		double originZ = 0;
		AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(angle), originX, originZ);

		double[] rotationPoint = { vector.xCoord, vector.zCoord };
		transform.transform(rotationPoint, 0, rotationPoint, 0, 1);
		return new Vec3d(rotationPoint[0], vector.yCoord, rotationPoint[1]);
	}

	/**
	 * Private method for calculation of list of {@linkplain BlockDirective}
	 * from a child structure.
	 * 
	 * One block directive is added for each block position.
	 * 
	 * The rectangle is defined by a global offset (x,z,y) and dimensions
	 * (width, height, depth).
	 * 
	 * The rectangle (e.g. list of directives) is rotated depending on the
	 * player direction.
	 * 
	 * @param offset
	 *            offset block position.
	 * @param playerDirection
	 *            player direction which controls the rotation of the
	 *            coordinates.
	 * @param structure
	 *            structure which defines the local offset, size and block
	 *            characteristics of the created rectangle.
	 * @return list of block directive (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	private static List<BlockDirective> calculateBlockDirectivesFromChildStructure(BlockPos offset,
			PlayerDirection playerDirection, Structure structure) {

		// exit if structure is a composite
		if (structure.isComposite())
			return new ArrayList<BlockDirective>();

		// calculate block positions
		BlockPos from = offset.add(structure.getOffsetX(), structure.getOffsetY(), structure.getOffsetZ());
		int xTo = structure.getSizeX() - 1;
		int yTo = structure.getSizeY() - 1;
		int zTo = structure.getSizeZ() - 1;
		BlockPos to = from.add(xTo, yTo, zTo);

		Iterable blockPosSet = BlockPos.getAllInBox(from, to);

		// calculate block directives
		List<BlockDirective> directives = calculateBlockDirectives(blockPosSet, structure.getBlock(),
				structure.getBlockState());

		// rotate directives to player orientation
		// for (BlockDirective c : directives) System.out.println("rect c=" +
		// c);
		int rotationDegrees = calculateDegreesFromPlayerDirection(playerDirection);
		List<BlockDirective> rotatedDirectives = rotateCoordinatesAroundYAxis(offset, rotationDegrees, directives);
		// for (BlockDirective c : rotatedDirectives)
		// System.out.println("rotated rect c=" + c);
		return rotatedDirectives;
	}

	/**
	 * Calculate block directives (e.g. coordinates) from structure from global
	 * offset block position.
	 * 
	 * The structure is rotated depending on player direction.
	 * 
	 * @param offset
	 *            global offset.
	 * @param playerDirection
	 *            player direction which controls the rotation of the
	 *            coordinates.
	 * @param structure
	 *            structure which defines the size of the created rectangle.
	 * @return list of block directives (e.g. coordinates) for the blocks in the
	 *         structure.
	 */
	public static List<BlockDirective> calculateBlockDirectives(BlockPos offset, PlayerDirection playerDirection,
			Structure structure) {

		// handle child structure
		if (!structure.isComposite()) {
			return calculateBlockDirectivesFromChildStructure(offset, playerDirection, structure);
		}

		// handle composite structure
		List<BlockDirective> compositeResult = new ArrayList<BlockDirective>();
		for (Structure child : structure.getChildren()) {
			compositeResult.addAll(calculateBlockDirectives(offset, playerDirection, child));
		}
		return compositeResult;
	}

	/**
	 * Calculate degrees from player direction.
	 * 
	 * @param playerDirection
	 *            player direction from which the degrees are calculated.
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
	 * Calculate y offset from block.
	 * 
	 * @param player
	 * @param blockPosition
	 * 
	 * @return y offset from block.
	 */
	public static int calculateYOffsetFromBlock(EntityPlayer player, BlockPos blockPosition) {
		int yFeetPosition = PlayerUtils.calculatePlayerFeetPosititionAsInt(player);
		int offset = blockPosition.getY() - yFeetPosition;
		return offset;
	}

	/**
	 * Capture rectangle as list of {@linkplain BlockDirective}. One block
	 * directive is captured for each block position.
	 * 
	 * The rectangle is defined by a global offset (x,z,y) and dimensions
	 * (width, height, depth)
	 * 
	 * @param offset
	 *            capture offset.
	 * @param size
	 *            capture size.
	 * @param worldQuery
	 *            world query object.
	 * 
	 * @return list of coordinates for the blocks in the structure.
	 */
	public static List<BlockDirective> captureRectangle(BlockPos offset, BlockPos size, WorldQuery worldQuery) {

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
			BlockPos to = from.add(layerXDelta, layerYDelta, layerZDelta);
			Iterable blockPosSet = BlockPos.getAllInBox(from, to);

			// exit if blocks is of type air
			if (containsAirBlocksOnly(blockPosSet, worldQuery))
				return result;

			// add blocks from this layer
			result.addAll(captureBlockDirectives(blockPosSet, worldQuery));

			// increase layer
			yCounter++;
		}
	}

	/**
	 * Translate set of block directives using translation vector.
	 * 
	 * @param translationVector
	 *            translation vector
	 * @param directives
	 *            set of block directives.
	 * 
	 * @return translated list of block directives.
	 */
	public static List<BlockDirective> translate(BlockPos translationVector, List<BlockDirective> directives) {
		List<BlockDirective> translated = new ArrayList<BlockDirective>();

		for (BlockDirective directive : directives) {
			BlockDirective translatedDirective = new BlockDirective(directive);
			translatedDirective.translate(translationVector);
			translated.add(translatedDirective);
		}
		return translated;
	}

	/**
	 * Calculate spiral.
	 * 
	 * @param maxX
	 *            maximum x coordinate.
	 * @param maxY
	 *            maximum y coordinate
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
	 * If block is air/water block then move down until ground block is located.
	 * If block is ground block then move up until block above air/water.
	 * 
	 * @param target
	 *            block to process.
	 * @param iterations
	 *            number of blocks to query before returning null. Should be a
	 *            positive integer.
	 * 
	 * @param world
	 *            world object.
	 * @return ground block, i.e. a block with air/water above and solid ground
	 *         below. If no block was found then original block position is
	 *         returned.
	 */
	public static BlockPos locateGroundBlockPos(BlockPos target, int iterations, World world) {
		if (iterations == 0)
			return target;
		if (iterations < 0)
			return target;

		int newIterations = iterations - 1;

		// if upper block isn't useful - then move up
		if (!isUsefullAirTypeBlock(target.up(), world)) {
			// System.out.println("upper isn't useful air block - go up.");
			return locateGroundBlockPos(target.up(), newIterations, world);
		}

		// if upper is OK - but current isn't - then move down
		if (!isUsefulGroundBlock(target, world)) {
			// System.out.println("useful air block - not useful ground block -
			// go down.");
			return locateGroundBlockPos(target.down(), newIterations, world);
		}

		return target;
	}

	/**
	 * Returns true if block is a useful "ground" type block, i.e. NOT liquid,
	 * air, plants or grass.
	 * 
	 * @param target
	 *            target block.
	 * @param world
	 *            world object
	 * @return
	 */
	static boolean isUsefulGroundBlock(BlockPos target, World world) {
		Block block = getBlockFromPosition(target, world);
		IBlockState defaultState = block.getDefaultState();
		Material material = defaultState.getMaterial();
		return (material.isSolid());
	}

	/**
	 * Returns true if block is a useful "air" type block, i.e. liquid, air,
	 * plants or grass.
	 * 
	 * @param target
	 *            target block.
	 * @param world
	 *            world object
	 * 
	 * @return true if block is a useful "air" type block.
	 */
	static boolean isUsefullAirTypeBlock(BlockPos target, World world) {
		Block block = getBlockFromPosition(target, world);
		IBlockState defaultState = block.getDefaultState();
		Material material = defaultState.getMaterial();
		return (!material.isSolid());
	}

	/**
	 * Creates block directive with flower.
	 * 
	 * @param position
	 *            block position for flower.
	 * @param random
	 *            random generator.
	 * @return block directive with flower.
	 */
	public static BlockDirective createFlowerDirective(BlockPos position, Random random) {
		int flowerType = random.nextInt(6);

		switch (flowerType) {

		case 0:
			BlockDirective yellow = new BlockDirective(position, Blocks.YELLOW_FLOWER, DONT_HARVEST);
			return yellow;
		case 1:
		default:			
			BlockDirective red = new BlockDirective(position, Blocks.RED_FLOWER, DONT_HARVEST);
			red.setState(selectRedFlowerType(random));
			return red;
		}			
	}

	/**
	 * Select flower.
	 * 
	 * @return Select flower type.
	 */
	static IBlockState selectRedFlowerType(Random random) {
		int flowerType = random.nextInt(7);

		switch (flowerType) {

		case 0:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.ALLIUM);
		case 1:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.BLUE_ORCHID);
		case 2:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.HOUSTONIA);
		case 3:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.ORANGE_TULIP);
		case 4:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.OXEYE_DAISY);
		case 5:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.PINK_TULIP);
		case 6:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.POPPY);
		case 7:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.WHITE_TULIP);
		default:
			return Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(),
					EnumFlowerType.POPPY);
		}

	}

}
