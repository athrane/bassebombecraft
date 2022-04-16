package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.DONT_HARVEST;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.item.action.build.BuildUtils.addMainEntranceFront;
import static bassebombecraft.item.action.build.BuildUtils.selectWallMaterial;
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
import bassebombecraft.item.action.build.tower.BuildMaterial;
import bassebombecraft.item.action.build.tower.Builder;
import bassebombecraft.item.action.build.tower.DefaultBuilder;
import bassebombecraft.item.action.build.tower.Room;
import bassebombecraft.item.action.build.tower.Wall;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build a
 * tower.
 */
public class BuildTower implements BlockClickedItemAction {

	static final InteractionResult USED_ITEM = InteractionResult.SUCCESS;
	static final InteractionResult DIDNT_USED_ITEM = InteractionResult.PASS;

	/**
	 * Random generator.
	 */
	Random random;

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
	 * Initial room height in blocks.
	 */
	int initialRoomHeight;

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
	 * BuildTower constructor.
	 */
	public BuildTower() {
		super();
		
		random = getBassebombeCraft().getRandom();
		
		builder = new DefaultBuilder(random);

		floorWidth = 35;
		floorDepth = 35;
		minSizeReductionPerLayer = 1;
		maxSizeReductionPerLayer = 4;
		maxRoomXResize = 3;
		maxRoomZResize = 3;
		numberLayers = 20;
		initialRoomHeight = 10;
		roomHeight = 7;
	}

	@Override
	public InteractionResult onItemUse(UseOnContext context) {
		// calculate if selected block is a ground block
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
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

		// get world
		Level world = context.getLevel();
		
		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure, DONT_HARVEST, world);

		// add directives
		BlockDirectivesRepository repository = getProxy().getServerBlockDirectivesRepository();		
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		// NO-OP
	}

	/**
	 * Create horizontal structure.
	 * 
	 * @return created structure.
	 */
	Structure createStructure() {

		// create structure
		CompositeStructure structure = new CompositeStructure();

		// initialize postprocessing composite
		CompositeStructure postComposite = new CompositeStructure();

		// initialize variables
		BlockPos offset = new BlockPos(0, 0, 0);
		int currentFloorWidth = this.floorWidth;
		int currentFloorDepth = this.floorDepth;

		for (int layer = 0; layer < numberLayers; layer++) {
			int height = calculateRoomHeight(layer);

			// calculate layer size
			currentFloorWidth = calculateLayerSize(currentFloorWidth);
			currentFloorDepth = calculateLayerSize(currentFloorDepth);

			// exit if top criteria has been reached
			if (hasReachedTop(currentFloorWidth, currentFloorDepth)) {
				BuildMaterial material = selectWallMaterial(random);
				builder.buildTop(offset, material.getBlock(), structure);
				break;
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
			BuildMaterial material = selectWallMaterial(random);
			Room room1 = createNWRoom(room1Offset, room1Size, material);
			room1.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #2
			BlockPos room2Offset = new BlockPos(floorXCenter - 1, offset.getY(), 0);
			BlockPos room2Size = new BlockPos(currentFloorWidth - floorXCenter, height, floorZCenter);
			material = selectWallMaterial(random);
			Room room2 = createNERoom(room2Offset, room2Size, material);
			room2.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #3
			BlockPos room3Offset = new BlockPos(0, offset.getY(), floorZCenter - 1);
			BlockPos room3Size = new BlockPos(floorXCenter, height, currentFloorDepth - floorZCenter);
			material = selectWallMaterial(random);
			Room room3 = createSWRoom(room3Offset, room3Size, material);
			room3.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// setup room #4
			BlockPos room4Offset = new BlockPos(floorXCenter - 1, offset.getY(), floorZCenter - 1);
			BlockPos room4Size = new BlockPos(currentFloorWidth - floorXCenter, height,
					currentFloorDepth - floorZCenter);
			material = selectWallMaterial(random);
			Room room4 = createSERoom(room4Offset, room4Size, material);
			room4.resize(random.nextInt(maxRoomXResize), random.nextInt(maxRoomZResize));

			// build rooms
			builder.buildRoom(room1, structure);
			builder.buildRoom(room2, structure);
			builder.buildRoom(room3, structure);
			builder.buildRoom(room4, structure);

			// add main entrance in room #1 in layer #1
			if (layer == 0) {
				BlockPos doorOffset = new BlockPos(floorXCenter - floorWidthDiv4, 0, room1.getOffset().getZ());
				addMainEntranceFront(structure, doorOffset);
			}

			// build floors
			builder.buildFloor(room1, structure);
			builder.buildFloor(room2, structure);
			builder.buildFloor(room3, structure);
			builder.buildFloor(room4, structure);

			// build stair up in room #1 or #4
			if (placeStairsInRoom1(layer)) {
				builder.buildStairs(room1, structure, postComposite);
			} else
				builder.buildStairs(room4, structure, postComposite);

			// build doors for room #1 and #4
			for (Wall wall : room1.getInteriorWalls())
				builder.buildDoor(wall, structure);
			for (Wall wall : room4.getInteriorWalls())
				builder.buildDoor(wall, structure);

			// build windows
			for (Wall wall : room1.getExternalWalls())
				builder.buildWindow(wall, structure);
			for (Wall wall : room2.getExternalWalls())
				builder.buildWindow(wall, structure);
			for (Wall wall : room3.getExternalWalls())
				builder.buildWindow(wall, structure);
			for (Wall wall : room4.getExternalWalls())
				builder.buildWindow(wall, structure);

			// build mob spawner
			builder.buildMobSpawner(room1, structure);
			builder.buildMobSpawner(room2, structure);
			builder.buildMobSpawner(room3, structure);
			builder.buildMobSpawner(room4, structure);

			// calculate offset etc for next iteration
			offset = new BlockPos(0, offset.getY() + height, 0);
		}

		// add post composite to composite
		structure.add(postComposite);

		return structure;
	}

	/**
	 * Calculate the room height from the layer number.
	 * 
	 * @param layer layer number.
	 * @return room height calculated from the layer number.
	 */
	int calculateRoomHeight(int layer) {
		if (layer == 0)
			return initialRoomHeight;
		return roomHeight;
	}

	/**
	 * Returns true if top criteria for tower has been reached.
	 * 
	 * @param currentFloorWidth current floor width.
	 * @param currentFloorDepth current floor depth.
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
	 * @param layer current tower layer.
	 * @return true if stairs should be place in room #1
	 */
	boolean placeStairsInRoom1(int layer) {
		return (layer % 2) == 1;
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
