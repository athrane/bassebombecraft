package bassebombecraft.item.action.build.tower;

import static bassebombecraft.item.action.build.BuildUtils.addSolidStairUp;
import static bassebombecraft.item.action.build.BuildUtils.createInstance;
import static bassebombecraft.structure.ChildStructure.createAirStructure;

import java.util.Random;

import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.CompositeStructure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Default implementation of the {@linkplain Builder} interface.
 */
public class DefaultBuilder implements Builder {

	/**
	 * Stairs material.
	 */
	StairsMaterial stairsMaterial;

	/**
	 * Random generator.
	 */
	Random random;

	/**
	 * DefaultBuilder constructor.
	 * 
	 * @param random
	 *            random generator.
	 */
	public DefaultBuilder(Random random) {
		this.random = random;
		IBlockState state = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING,
				EnumFacing.SOUTH);
		stairsMaterial = createInstance(state, Blocks.STONE_BRICK_STAIRS, Blocks.STONEBRICK);
	}

	@Override
	public void buildRoom(Room room, CompositeStructure composite) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();
		Block material = room.getMaterial();

		int height = size.getY();
		int width = size.getX();
		int depth = size.getZ();
		int xoffset = offset.getX();
		int yoffset = offset.getY();
		int zoffset = offset.getZ();
		BlockPos roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		BlockPos roomSize = new BlockPos(width, height, depth);
		composite.add(new ChildStructure(roomOffset, roomSize, material));

		height = size.getY() - 2;
		width = size.getX() - 2;
		depth = size.getZ() - 2;
		xoffset = offset.getX() + 1;
		yoffset = offset.getY() + 1;
		zoffset = zoffset + 1;
		roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		roomSize = new BlockPos(width, height, depth);
		composite.add(createAirStructure(roomOffset, roomSize));
	}

	@Override
	public void buildStairs(Room room, CompositeStructure composite) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();

		// TODO: select stairs type based on available space

		int roomHeight = size.getY();
		BlockPos stairOffset = new BlockPos(offset.getX() + 1, offset.getY(), offset.getZ() + 2);
		addSolidStairUp(roomHeight, stairsMaterial, composite, stairOffset);
	}

	@Override
	public void buildWindow(Wall wall, CompositeStructure composite) {

		BlockPos offset = wall.getOffset();
		BlockPos roomSize = wall.getRoom().getSize();

		// exit if room is to small for window
		int minWindowSize = 2;
		switch (wall.getOrientation()) {

		case X: {
			if (roomSize.getX() <= minWindowSize)
				return;
			if (roomSize.getY() <= minWindowSize)
				return;
		}

		case Z: {
			if (roomSize.getY() <= minWindowSize)
				return;
			if (roomSize.getZ() <= minWindowSize)
				return;
		}
		}

		int windowXZSize = 2;
		int windowXZSizeDiv2 = windowXZSize / 2;
		int windowYSize = 2;
		int windowYSizeDiv2 = windowYSize / 2;

		// place window
		int windowYOffset = windowYSizeDiv2 + random.nextInt(roomSize.getY() - windowYSize);

		switch (wall.getOrientation()) {

		case X: {
			int windowXOffset = windowXZSizeDiv2 + random.nextInt(roomSize.getX() - windowXZSize);
			BlockPos windowOffset = offset.add(windowXOffset, windowYOffset, 0);
			BlockPos windowSize = new BlockPos(windowXZSize, windowYSize, 1);
			composite.add(new ChildStructure(windowOffset, windowSize, Blocks.GLASS_PANE));
			return;
		}

		case Z: {
			int windowZOffset = windowXZSizeDiv2 + random.nextInt(roomSize.getZ() - windowXZSize);
			BlockPos windowOffset = offset.add(0, windowYOffset, windowZOffset);
			BlockPos windowSize = new BlockPos(1, windowYSize, windowXZSize);
			composite.add(new ChildStructure(windowOffset, windowSize, Blocks.GLASS_PANE));
			return;
		}

		}

	}

	@Override
	public void buildDoor(Wall wall, CompositeStructure composite) {

		BlockPos offset = wall.getOffset();
		BlockPos roomSize = wall.getRoom().getSize();

		// exit if room is to small for door
		int minRoomSize = 3;
		switch (wall.getOrientation()) {
		case X: {
			if (roomSize.getX() <= minRoomSize)
				return;
		}

		case Z: {
			if (roomSize.getZ() <= minRoomSize)
				return;
		}
		}

		// calculate door offset
		int doorXZOffset = calculateDoorOffset(wall, roomSize);
		System.out.println("doorXZOffset=" + doorXZOffset);

		int doorXZSize = 2;

		// place door
		int doorYSize = 3;
		switch (wall.getOrientation()) {
		case X: {
			int randomFactor = roomSize.getX() - doorXZSize - doorXZOffset;
			int doorXOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(doorXOffset, 1, 0);
			BlockPos doorSize = new BlockPos(doorXZSize, doorYSize, 1);
			composite.add(new ChildStructure(doorOffset, doorSize, Blocks.AIR));
			return;
		}

		case Z: {
			int randomFactor = roomSize.getZ() - doorXZSize - doorXZOffset;
			int doorZOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(0, 1, doorZOffset);
			BlockPos doorSize = new BlockPos(1, doorYSize, doorXZSize);
			composite.add(new ChildStructure(doorOffset, doorSize, Blocks.AIR));
			return;
		}

		}

	}

	/**
	 * Calculate door displacement.
	 * 
	 * @param wall
	 * @param roomSize
	 */
	int calculateDoorOffset(Wall wall, BlockPos roomSize) {

		// calculate displacement for door in wall with size 4
		switch (wall.getOrientation()) {
		case X: {
			if (roomSize.getX() == 4)
				return 1;
			if (roomSize.getX() > 4)
				return 2;
		}

		case Z: {
			if (roomSize.getZ() == 4)
				return 1;
			if (roomSize.getZ() > 4)
				return 2;
		}
		}

		return 0;
	}

	@Override
	public void buildTop(BlockPos offset, Block material, CompositeStructure composite) {

		BlockPos size = new BlockPos(10, 1, 10);
		offset = offset.add(-5, 0, -5);
		composite.add(new ChildStructure(offset, size, material));
	}

}
