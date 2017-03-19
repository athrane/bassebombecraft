package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.item.action.build.BuildUtils.addOakFencedDoorEntryFront;
import static bassebombecraft.item.action.build.tower.Room.createNERoom;
import static bassebombecraft.item.action.build.tower.Room.createNWRoom;
import static bassebombecraft.item.action.build.tower.Room.createSERoom;
import static bassebombecraft.item.action.build.tower.Room.createSWRoom;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.item.action.build.tower.Builder;
import bassebombecraft.item.action.build.tower.DefaultBuilder;
import bassebombecraft.item.action.build.tower.Room;
import bassebombecraft.item.action.build.tower.Wall;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build a
 * tower.
 */
public class BuildTower implements BlockClickedItemAction {

	static final EnumActionResult USED_ITEM = EnumActionResult.SUCCESS;

	/**
	 * Random generator.
	 */
	// Random random = new Random(1); // random #1 creates to small rooms
	Random random = new Random(2);

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository repository;

	/**
	 * Minimum size reduction per layer.
	 */
	int minSizeReductionPerLayer;

	/**
	 * Maximum size reduction per layer.
	 */
	int maxSizeReductionPerLayer;

	/**
	 * Number of layer in the tower.
	 */
	int numberLayers;

	/**
	 * Room height in blocks.
	 */
	int roomHeight;

	/**
	 * floor width.
	 */
	int floorWidth;

	/**
	 * floor height.
	 */
	int floorDepth;

	/**
	 * Maximum room X resize value.
	 */
	int maxRoomXResize;

	/**
	 * Maximum room Z resize value.
	 */
	int maxRoomZResize;

	/**
	 * Tower builder.
	 */
	Builder builder;

	/**
	 * BuildSmallHole constructor.
	 */
	public BuildTower() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
		builder = new DefaultBuilder(random);

		floorWidth = 35;
		floorDepth = 35;
		minSizeReductionPerLayer = 1;
		maxSizeReductionPerLayer = 4;
		maxRoomXResize = 3;
		maxRoomZResize = 3;
		numberLayers = 20;
		roomHeight = 7;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), player);

		// exit if not click on ground block
		if (!isGroundBlock)
			return USED_ITEM;

		// calculate structure
		Structure structure = createStructure();

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure, DONT_HARVEST);

		// add directives
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

	/**
	 * Create horizontal structure.
	 * 
	 * @return created structure.
	 */
	Structure createStructure() {
		CompositeStructure composite = new CompositeStructure();

		BlockPos offset = new BlockPos(0, 0, 0);
		int currentFloorWidth = this.floorWidth;
		int currentFloorDepth = this.floorDepth;

		for (int layer = 0; layer < numberLayers; layer++) {
			int height = roomHeight;

			// calculate layer size
			currentFloorWidth = calculateLayerSize(currentFloorWidth);
			currentFloorDepth = calculateLayerSize(currentFloorDepth);
						
			// exit if top criteria has been reached
			if (hasReachedTop(currentFloorWidth, currentFloorDepth)) {
				builder.buildTop(offset, selectMaterial(), composite);
				return composite;
			}

			// calculate floor center
			int floorWidthDiv2 = currentFloorWidth / 2;
			int floorWidthDiv4 = currentFloorWidth / 4;
						
			int floorXCenter = floorWidthDiv4 + random.nextInt(floorWidthDiv2);

			int floorDepthDiv2 = currentFloorDepth / 2;
			int floorDepthDiv4 = currentFloorDepth / 4;
			int floorZCenter = floorDepthDiv4 + random.nextInt(floorDepthDiv2);

			// setup room #1
			BlockPos room1Offset = new BlockPos(0, offset.getY(), 0);
			BlockPos room1Size = new BlockPos(floorXCenter, height, floorZCenter);
			Room room1 = createNWRoom(room1Offset, room1Size, selectMaterial());
			room1.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #2
			BlockPos room2Offset = new BlockPos(floorXCenter - 1, offset.getY(), 0);
			BlockPos room2Size = new BlockPos(currentFloorWidth - floorXCenter, height, floorZCenter);
			Room room2 = createNERoom(room2Offset, room2Size, selectMaterial());
			room2.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #3
			BlockPos room3Offset = new BlockPos(0, offset.getY(), floorZCenter - 1);
			BlockPos room3Size = new BlockPos(floorXCenter, height, currentFloorDepth - floorZCenter);
			Room room3 = createSWRoom(room3Offset, room3Size, selectMaterial());
			room3.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #4
			BlockPos room4Offset = new BlockPos(floorXCenter - 1, offset.getY(), floorZCenter - 1);
			BlockPos room4Size = new BlockPos(currentFloorWidth - floorXCenter, height,
					currentFloorDepth - floorZCenter);
			Room room4 = createSERoom(room4Offset, room4Size, selectMaterial());
			room4.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// build rooms
			builder.buildRoom(room1, composite);
			builder.buildRoom(room2, composite);
			builder.buildRoom(room3, composite);
			builder.buildRoom(room4, composite);

			// build door in room #1 in layer #1
			if (layer == 0) {
				BlockPos doorOffset = new BlockPos(floorXCenter - floorWidthDiv4, 0, 0);
				addOakFencedDoorEntryFront(composite, doorOffset);
			}

			// build stair up in room #1 or #4
			if (placeStairsInRoom1(layer))
				builder.buildStairs(room1, composite);
			else
				builder.buildStairs(room4, composite);

			// build doors for room #1 and #4
			for (Wall wall : room1.getInteriorWalls())
				builder.buildDoor(wall, composite);
			for (Wall wall : room4.getInteriorWalls())
				builder.buildDoor(wall, composite);

			// build windows
			for (Wall wall : room1.getExternalWalls())
				builder.buildWindow(wall, composite);
			for (Wall wall : room2.getExternalWalls())
				builder.buildWindow(wall, composite);
			for (Wall wall : room3.getExternalWalls())
				builder.buildWindow(wall, composite);
			for (Wall wall : room4.getExternalWalls())
				builder.buildWindow(wall, composite);

			// calculate offset etc for next iteration
			offset = new BlockPos(0, offset.getY() + height, 0);
		}

		return composite;
	}

	/**
	 * Returns true if top criteria for tower has been reached.
	 * 
	 * @param currentFloorWidth
	 *            current floor width.
	 * @param currentFloorDepth
	 *            current floor depth.
	 * 
	 * @return true if top criteria for tower has been reached.
	 */
	boolean hasReachedTop(int currentFloorWidth, int currentFloorDepth) {
		if (currentFloorWidth <= 2)
			return true;
		if (currentFloorDepth <= 2)
			return true;
		return false;
	}

	/**
	 * Returns true if stairs should be place in room #1.
	 * 
	 * @param layer
	 *            current tower layer.
	 * @return true if stairs should be place in room #1
	 */
	boolean placeStairsInRoom1(int layer) {
		return (layer % 2) == 0;
	}

	/**
	 * Select random material.
	 * 
	 * @return random material
	 */
	Block selectMaterial() {
		return Blocks.SANDSTONE;

		/**
		 * int selection = random.nextInt(9); switch(selection) {
		 * 
		 * case 0: return Blocks.BEDROCK; case 1: return Blocks.BRICK_BLOCK;
		 * case 2: return Blocks.NETHER_BRICK; case 3: return Blocks.SANDSTONE;
		 * case 4: return Blocks.STONE; case 5: return Blocks.STONEBRICK; case
		 * 6: return Blocks.MOSSY_COBBLESTONE; case 7: return
		 * Blocks.COBBLESTONE; default: return Blocks.OBSIDIAN; }
		 **/
	}

	/**
	 * Calculate layer size in one dimension.
	 * 
	 * @param currentSize
	 * 
	 * @return layer size in one dimension
	 */
	int calculateLayerSize(int currentSize) {
		int delta = minSizeReductionPerLayer + random.nextInt(maxSizeReductionPerLayer - minSizeReductionPerLayer);
		return currentSize - delta;
	}

}
