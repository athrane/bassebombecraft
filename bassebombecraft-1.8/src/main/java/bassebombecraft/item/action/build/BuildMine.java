package bassebombecraft.item.action.build;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.UNITY_BLOCK_SIZE;
import static bassebombecraft.geom.GeometryUtils.calculateBlockDirectives;
import static bassebombecraft.item.action.build.BuildUtils.addOakFencedDoorEntryFront;
import static bassebombecraft.item.action.build.BuildUtils.addOakFencedDoorEntryFrontSideways;
import static bassebombecraft.player.PlayerUtils.calculatePlayerFeetPosititionAsInt;
import static bassebombecraft.player.PlayerUtils.getPlayerDirection;
import static bassebombecraft.player.PlayerUtils.isBelowPlayerYPosition;
import static bassebombecraft.structure.ChildStructure.createAirStructure;
import static bassebombecraft.structure.ChildStructure.createTorchStructure;
import static bassebombecraft.structure.ChildStructure.createWoodStructure;

import java.util.List;
import java.util.Random;

import bassebombecraft.event.block.BlockDirectivesRepository;
import bassebombecraft.geom.BlockDirective;
import bassebombecraft.item.action.BlockClickedItemAction;
import bassebombecraft.player.PlayerDirection;
import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import bassebombecraft.structure.Structure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain BlockClickedItemAction} which build a mine.
 */
public class BuildMine implements BlockClickedItemAction {

	static final EnumActionResult USED_ITEM = EnumActionResult.SUCCESS;
	static final EnumActionResult DIDNT_USED_ITEM = EnumActionResult.PASS;

	static final int STATE_UPDATE_FREQUENCY = 1; // Measured in ticks

	static final int X_SIZE = 3;
	static final int Y_SIZE = 1;
	static final int Z_SIZE = 10;
	static final int X_OFFSET = -1;
	static final int Y_OFFSET_DOWN = 0;
	static final Structure NULL_STRUCTURE = new CompositeStructure();

	/**
	 * Random generator.
	 */
	Random random = new Random();

	/**
	 * Ticks exists since first marker was set.
	 */
	int ticksExisted = 0;

	/**
	 * Process block directives repository.
	 */
	BlockDirectivesRepository repository;

	/**
	 * CreateRoad constructor.
	 */
	public BuildMine() {
		super();
		repository = getBassebombeCraft().getBlockDirectivesRepository();
	}

	@Override
	public EnumActionResult onItemUse(PlayerEntity player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (ticksExisted % STATE_UPDATE_FREQUENCY != 0)
			return DIDNT_USED_ITEM;

		// calculate if selected block is a ground block
		boolean isGroundBlock = isBelowPlayerYPosition(pos.getY(), player);

		// calculate structure
		Structure structure = createStructure(isGroundBlock);

		// calculate Y offset in structure
		int yOffset = calculatePlayerFeetPosititionAsInt(player);

		// get player direction
		PlayerDirection playerDirection = getPlayerDirection(player);

		// calculate set of block directives
		BlockPos offset = new BlockPos(pos.getX(), yOffset, pos.getZ());
		List<BlockDirective> directives = calculateBlockDirectives(offset, playerDirection, structure);

		// add directives
		repository.addAll(directives);

		return USED_ITEM;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

	}

	/**
	 * Create random structure.
	 * 
	 * @param isGroundBlock
	 *            boolean designates of player click on ground block.
	 * 
	 * @return random structure
	 */
	Structure createStructure(boolean isGroundBlock) {
		// create ground blocks
		if (isGroundBlock) {
			return createStairDown();
		}

		// create room
		int roomId = random.nextInt(8);
		if (roomId == 0)
			return createRoomWithThreeDoorways10x8x4();
		if (roomId == 1)
			return createRoomWithInitialPlateau8x8x9();
		if (roomId == 2)
			return createCorridor4x4x8();
		if (roomId == 3)
			return createCorridor2x4x12();
		if (roomId == 4)
			return createCorridorWithColumns4x4x8();
		if (roomId == 5)
			return createLavaAbyssWithColumns8x12x8();
		return createCorridor4x4x8();

		// createGreatHall();
		// return createCorridor();
	}

	/**
	 * Create room with three doorways with bounding box (10,8,4).
	 * 
	 * @return room with three doorways.
	 */
	Structure createRoomWithThreeDoorways10x8x4() {
		CompositeStructure composite = new CompositeStructure();

		// main room of air
		BlockPos offset = new BlockPos(-4, 0, 0);
		BlockPos size = new BlockPos(8, 4, 8);
		composite.add(createAirStructure(offset, size));

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, 0, 8));

		// door entry - right
		addOakFencedDoorEntryFrontSideways(composite, new BlockPos(-5, 0, 2));

		// door entry - left
		addOakFencedDoorEntryFrontSideways(composite, new BlockPos(4, 0, 2));

		// add random torch
		BlockPos[] positions = new BlockPos[] { new BlockPos(4, 2, 6), new BlockPos(-5, 2, 6), new BlockPos(4, 2, 1),
				new BlockPos(-5, 2, 1) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// random floor details
		addFloorDetails(composite, offset, size);

		return composite;
	}

	/**
	 * Create corridor with bounding box (4,4,8).
	 * 
	 * @return corridor.
	 */
	Structure createCorridor4x4x8() {
		CompositeStructure composite = new CompositeStructure();

		// main room of air
		BlockPos offset = new BlockPos(-2, 0, 0);
		BlockPos size = new BlockPos(4, 4, 8);
		composite.add(createAirStructure(offset, size));

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, 0, 8));

		// add random torch
		BlockPos[] positions = new BlockPos[] { new BlockPos(2, 2, 6), new BlockPos(-3, 2, 6) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// random floor details
		addFloorDetails(composite, offset, size);

		return composite;
	}

	/**
	 * Create corridor with bounding box (4,2,12).
	 * 
	 * @return corridor.
	 */
	Structure createCorridor2x4x12() {
		CompositeStructure composite = new CompositeStructure();

		// main room of air
		BlockPos offset = new BlockPos(-1, 0, 0);
		BlockPos size = new BlockPos(2, 4, 12);
		composite.add(createAirStructure(offset, size));

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, 0, 12));

		// add random torch
		BlockPos[] positions = new BlockPos[] { new BlockPos(1, 2, 10), new BlockPos(-2, 2, 10) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// random floor details
		addFloorDetails(composite, offset, size);

		return composite;
	}

	/**
	 * Create room with initial plateau and lower door with bounding box
	 * (8,8,9).
	 * 
	 * @return structures.
	 */
	Structure createRoomWithInitialPlateau8x8x9() {
		CompositeStructure composite = new CompositeStructure();

		// upper space - initial tunnel
		BlockPos offset = new BlockPos(-2, 0, 0);
		BlockPos size = new BlockPos(5, 3, 1);
		composite.add(createAirStructure(offset, size));

		// upper space
		offset = new BlockPos(-4, 0, 1);
		size = new BlockPos(8, 3, 8);
		composite.add(createAirStructure(offset, size));

		// lower space
		offset = new BlockPos(-4, -4, 5);
		size = new BlockPos(8, 4, 4);
		composite.add(createAirStructure(offset, size));

		// stairs
		BlockPos[] positions = new BlockPos[] { new BlockPos(-4, -1, 4), new BlockPos(2, -1, 4) };
		BlockPos sOffset = selectionPosition(positions);
		addPlateauStairDown(composite, sOffset);

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, -4, 9));

		// add random torch - upper
		positions = new BlockPos[] { new BlockPos(4, 2, 1), new BlockPos(-5, 2, 1) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// add random torch - lower
		positions = new BlockPos[] { new BlockPos(4, -2, 7), new BlockPos(-5, -2, 7) };
		tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// lava
		// offset = new BlockPos(3, -5, 7);
		// size = new BlockPos(1, 1, 1);
		// composite.add(new ChildStructure(offset, size, Blocks.lava));

		return composite;
	}

	/**
	 * Create corridor with columns with bounding box (4,4,8).
	 * 
	 * @return corridor.
	 */
	Structure createCorridorWithColumns4x4x8() {
		CompositeStructure composite = new CompositeStructure();

		// upper space - initial tunnel
		BlockPos offset = new BlockPos(-2, 0, 0);
		BlockPos size = new BlockPos(5, 3, 1);
		composite.add(createAirStructure(offset, size));

		// main room of air
		offset = new BlockPos(-4, 0, 1);
		size = new BlockPos(8, 4, 8);
		composite.add(createAirStructure(offset, size));

		// add columns
		BlockPos[] positions = new BlockPos[] { new BlockPos(3, 0, 1), new BlockPos(-4, 0, 1) };
		BlockPos cOffset = selectionPosition(positions);
		addColumns(composite, cOffset, 4, size.getY());

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, 0, 9));

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 6), new BlockPos(-5, 2, 6) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 4), new BlockPos(-5, 2, 4) };
		tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 2), new BlockPos(-5, 2, 2) };
		tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// random floor details
		addFloorDetails(composite, offset, size);

		return composite;
	}

	/**
	 * Create corridor with columns with bounding box (8,12,8).
	 * 
	 * @return corridor.
	 */
	Structure createLavaAbyssWithColumns8x12x8() {
		CompositeStructure composite = new CompositeStructure();

		// upper space - initial tunnel
		BlockPos offset = new BlockPos(-2, 0, 0);
		BlockPos size = new BlockPos(5, 3, 1);
		composite.add(createAirStructure(offset, size));

		// lava floor
		offset = new BlockPos(-4, -9, 1);
		size = new BlockPos(8, 1, 8);
		composite.add(new ChildStructure(offset, size, Blocks.LAVA));

		// main room of air
		offset = new BlockPos(-4, -8, 1);
		size = new BlockPos(8, 12, 8);
		composite.add(createAirStructure(offset, size));

		// add columns
		BlockPos[] positions = new BlockPos[] { new BlockPos(3, -8, 1), new BlockPos(-4, -8, 1) };
		BlockPos cOffset = selectionPosition(positions);
		addColumns(composite, cOffset, 4, 12);

		// add bridge
		offset = new BlockPos(-1, -1, 1);
		size = new BlockPos(2, 1, 8);
		composite.add(new ChildStructure(offset, size, Blocks.BRICK_BLOCK));

		// door entry - back
		addOakFencedDoorEntryFront(composite, new BlockPos(0, 0, 9));

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 6), new BlockPos(-5, 2, 6) };
		BlockPos tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 4), new BlockPos(-5, 2, 4) };
		tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		// add random torch
		positions = new BlockPos[] { new BlockPos(4, 2, 2), new BlockPos(-5, 2, 2) };
		tOffset = selectionPosition(positions);
		addTorch(composite, tOffset);

		return composite;
	}

	/**
	 * Create stairs going down with bounding box (4,-11,12).
	 * 
	 * @return stairs going down .
	 */
	Structure createStairDown() {
		CompositeStructure composite = new CompositeStructure();

		final int roomHeight = 4;
		for (int index = 0; index < 10; index++) {
			int height = roomHeight;
			BlockPos offset = new BlockPos(0, -(index + 1), index);
			if (index < 5)
				height = index + 1;
			BlockPos size = new BlockPos(2, height, 1);
			composite.add(new ChildStructure(offset, size));
		}

		// space above ground
		BlockPos offset = new BlockPos(0, 0, 0);
		BlockPos size = new BlockPos(2, 1, 5);
		composite.add(new ChildStructure(offset, size, Blocks.AIR));
		offset = new BlockPos(-1, 1, 1);
		size = new BlockPos(4, 2, 5);
		composite.add(new ChildStructure(offset, size, Blocks.AIR));

		// stone fence - left side
		offset = new BlockPos(2, 0, 0);
		size = new BlockPos(1, 1, 6);
		composite.add(new ChildStructure(offset, size, Blocks.COBBLESTONE_WALL));
		offset = new BlockPos(2, 1, 0);
		size = new BlockPos(1, 1, 1);
		composite.add(createTorchStructure(offset, size));

		// stone fence - right side
		offset = new BlockPos(-1, 0, 0);
		size = new BlockPos(1, 1, 6);
		composite.add(new ChildStructure(offset, size, Blocks.COBBLESTONE_WALL));
		offset = new BlockPos(-1, 1, 0);
		size = new BlockPos(1, 1, 1);
		composite.add(createTorchStructure(offset, size));

		// stone fence - end
		offset = new BlockPos(0, 0, 5);
		size = new BlockPos(2, 1, 1);
		composite.add(new ChildStructure(offset, size, Blocks.COBBLESTONE_WALL));

		// bottom plateau
		offset = new BlockPos(0, -11, 10);
		size = new BlockPos(2, roomHeight, 2);
		composite.add(new ChildStructure(offset, size));

		// bottom torch in wall - right side
		BlockPos tOffset = new BlockPos(-1, -8, 10);
		addTorch(composite, tOffset);

		return composite;

	}

	/**
	 * Create great hall.
	 * 
	 * @return great hall.
	 */
	Structure createGreatHall() {
		CompositeStructure composite = new CompositeStructure();

		// torch left - set before air structure
		BlockDirective offset = new BlockDirective(9, 1, 1);
		BlockDirective size = new BlockDirective(1, 1, 1);
		composite.add(createTorchStructure(offset, size));

		// column
		offset = new BlockDirective(10, 0, 10);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(-10, 0, 10);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(4, 0, 10);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(-4, 0, 10);
		addGreatHallColumn(composite, offset);

		offset = new BlockDirective(10, 0, 20);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(-10, 0, 20);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(4, 0, 20);
		addGreatHallColumn(composite, offset);
		offset = new BlockDirective(-4, 0, 20);
		addGreatHallColumn(composite, offset);

		// main room
		offset = new BlockDirective(-10, 0, 0);
		size = new BlockDirective(20, 10, 30);
		composite.add(createAirStructure(offset, size));

		return composite;
	}

	/**
	 * Create room with a left walkway with bounding box (4,8,8).
	 * 
	 * @return stairs going down
	 */
	Structure createRoomWithLeftWalkway() {
		CompositeStructure composite = new CompositeStructure();

		// torch left - set before air structure
		BlockDirective offset = new BlockDirective(-2, 1, 3);
		BlockDirective size = new BlockDirective(1, 1, 1);
		composite.add(createTorchStructure(offset, size));

		// main room
		offset = new BlockDirective(-2, -4, 0);
		size = new BlockDirective(2, 4, 4);
		composite.add(createAirStructure(offset, size));

		offset = new BlockDirective(-2, 0, 0);
		size = new BlockDirective(4, 4, 4);
		composite.add(createAirStructure(offset, size));

		// door entry - back
		addDoorEntryFront(composite, new BlockDirective(1, 0, 4));
		return composite;
	}

	/**
	 * Create shaft with stairs with bounding box (4,4,8).
	 * 
	 * @return corridor.
	 */
	Structure createShaftwithStairs() {
		CompositeStructure composite = new CompositeStructure();

		// main room
		BlockDirective offset = new BlockDirective(-2, -8, 0);
		BlockDirective size = new BlockDirective(4, 8, 4);
		composite.add(createAirStructure(offset, size));

		return composite;
	}

	/**
	 * Add doorway entry turning front.
	 * 
	 * @param structure
	 *            structure where door is added to.
	 * @param globalOffset
	 *            global offset.
	 */
	@Deprecated
	void addDoorEntryFront(Structure structure, BlockDirective globalOffset) {
		BlockDirective offset = new BlockDirective(-1 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		BlockDirective size = new BlockDirective(2, 3, 1);
		structure.add(createAirStructure(offset, size));
		offset = new BlockDirective(-2 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		size = new BlockDirective(1, 3, 1);
		structure.add(createWoodStructure(offset, size));
		offset = new BlockDirective(1 + globalOffset.getX(), globalOffset.getY(), globalOffset.getZ());
		size = new BlockDirective(1, 3, 1);
		structure.add(createWoodStructure(offset, size));
		offset = new BlockDirective(-2 + globalOffset.getX(), 3 + globalOffset.getY(), globalOffset.getZ());
		size = new BlockDirective(4, 1, 1);
		structure.add(createWoodStructure(offset, size));
	}

	/**
	 * Add stair down from plateau .
	 * 
	 * @param structure
	 *            structure where stair is added to.
	 * @param globalOffset
	 *            global offset.
	 */

	void addPlateauStairDown(Structure structure, BlockPos globalOffset) {
		BlockPos offset = new BlockPos(globalOffset);
		BlockPos size = new BlockPos(2, 1, 1);
		structure.add(new ChildStructure(offset, size, Blocks.BRICK_STAIRS));
		offset = new BlockPos(globalOffset.getX(), globalOffset.getY() - 3, globalOffset.getZ() + 1);
		size = new BlockPos(2, 3, 1);
		structure.add(new ChildStructure(offset, size, Blocks.BRICK_STAIRS));
		offset = new BlockPos(globalOffset.getX(), globalOffset.getY() - 3, globalOffset.getZ() + 2);
		size = new BlockPos(2, 2, 1);
		structure.add(new ChildStructure(offset, size, Blocks.BRICK_STAIRS));
		offset = new BlockPos(globalOffset.getX(), globalOffset.getY() - 3, globalOffset.getZ() + 3);
		size = new BlockPos(2, 1, 1);
		structure.add(new ChildStructure(offset, size, Blocks.BRICK_STAIRS));
	}

	/**
	 * Create great hall column.
	 * 
	 * @param structure
	 *            structure where column is added to.
	 * @param globalOffset
	 *            global offset.
	 */
	void addGreatHallColumn(Structure structure, BlockDirective globalOffset) {

		// torch low
		BlockDirective offset = new BlockDirective(globalOffset.getX() - 2, globalOffset.getY() + 7,
				globalOffset.getZ());
		BlockDirective size = new BlockDirective(1, 1, 1);
		structure.add(createTorchStructure(offset, size));

		// torch high
		offset = new BlockDirective(globalOffset.getX() + 1, globalOffset.getY() + 4, globalOffset.getZ());
		structure.add(createTorchStructure(offset, size));

		// column
		offset = new BlockDirective(globalOffset.getX() - 1, globalOffset.getY(), globalOffset.getZ() - 1);
		size = new BlockDirective(2, 9, 2);
		structure.add(new ChildStructure(offset, size, Blocks.QUARTZ_BLOCK));

		offset = new BlockDirective(globalOffset.getX() - 2, globalOffset.getY() + 9, globalOffset.getZ() - 2);
		size = new BlockDirective(4, 1, 4);
		structure.add(new ChildStructure(offset, size, Blocks.QUARTZ_BLOCK));
	}

	/**
	 * Add torch.
	 * 
	 * @param structure
	 *            structure where torch is added to.
	 * @param globalOffset
	 *            global offset.
	 */
	void addTorch(Structure structure, BlockPos globalOffset) {
		BlockPos offset = globalOffset;
		structure.add(new ChildStructure(offset, UNITY_BLOCK_SIZE, Blocks.TORCH));

		offset = globalOffset.add(0, -1, 0);
		structure.add(new ChildStructure(offset, UNITY_BLOCK_SIZE, Blocks.OAK_FENCE));
	}

	/**
	 * Add columns
	 * 
	 * @param structure
	 *            structure where torch is added to.
	 * @param globalOffset
	 *            global offset.
	 * @param columns
	 *            number of columns.
	 * @param height
	 *            height of columns.
	 */
	void addColumns(Structure structure, BlockPos globalOffset, int columns, int height) {
		BlockPos offset = globalOffset;
		BlockPos size = new BlockPos(1, height, 1);

		for (int i = 0; i < columns; i++) {
			offset = new BlockPos(globalOffset.getX(), globalOffset.getY(), globalOffset.getZ() + (i * 2));
			structure.add(new ChildStructure(offset, size, Blocks.BRICK_BLOCK));
		}
	}

	/**
	 * Add floor details.
	 * 
	 * @param structure
	 *            structure where column is added to.
	 * @param offset
	 *            global offset.
	 * @param size
	 *            room size.
	 */
	void addFloorDetails(Structure structure, BlockPos offset, BlockPos size) {

		// build tracks
		if (random.nextBoolean()) {
			return;
		}

		// add slabs
		if (random.nextBoolean()) {
			return;
		}

	}

	/**
	 * Select a position form the array.
	 * 
	 * @param positions
	 *            position to select from.
	 * 
	 * @return selected position.
	 */
	BlockPos selectionPosition(BlockPos[] positions) {
		int range = positions.length;
		int index = random.nextInt(range);
		return positions[index];
	}
}
