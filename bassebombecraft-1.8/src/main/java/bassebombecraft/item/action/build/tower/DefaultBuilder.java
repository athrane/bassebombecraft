package bassebombecraft.item.action.build.tower;

import static bassebombecraft.item.action.build.BuildUtils.addSolidStairUp;
import static bassebombecraft.item.action.build.BuildUtils.createInstance;
import static bassebombecraft.structure.ChildStructure.createAirStructure;

import java.util.Random;

import bassebombecraft.structure.ChildStructure;
import bassebombecraft.structure.Structure;
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
	public void buildRoom(Room room, Structure structure) {

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
		structure.add(new ChildStructure(roomOffset, roomSize, material));

		height = size.getY() - 2;
		width = size.getX() - 2;
		depth = size.getZ() - 2;
		xoffset = offset.getX() + 1;
		yoffset = offset.getY() + 1;
		zoffset = zoffset + 1;
		roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		roomSize = new BlockPos(width, height, depth);
		structure.add(createAirStructure(roomOffset, roomSize));
	}

	@Override
	public void buildStairs(Room room, Structure structure, Structure postStructure) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();

		// TODO: select stairs type based on available space

		int roomHeight = size.getY();
		BlockPos stairOffset = new BlockPos(offset.getX() + 1, offset.getY(), offset.getZ() + 2);
		addSolidStairUp(roomHeight, stairsMaterial, structure, postStructure, stairOffset);
	}

	@Override
	public void buildWindow(Wall wall, Structure structure) {

		BlockPos offset = wall.getOffset();
		BlockPos roomSize = wall.getRoom().getSize();

		// exit if room is to small for window
		int minRoomSize = 4;
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

		int windowXZSize = 2;
		int windowYSize = 2;

		// calculate door offset
		int windowXZOffset = calculateWindowOffset(wall, roomSize);
		int windowYOffset = roomSize.getY() - windowYSize - 1;

		// place window
		switch (wall.getOrientation()) {
		case X: {
			int randomFactor = roomSize.getX() - windowXZSize - windowXZOffset;
			int windowXOffset = windowXZOffset + random.nextInt(randomFactor);
			BlockPos windowOffset = offset.add(windowXOffset, windowYOffset, 0);
			BlockPos windowSize = new BlockPos(windowXZSize, windowYSize, 1);
			structure.add(new ChildStructure(windowOffset, windowSize, Blocks.GLASS_PANE));
			return;
		}

		case Z: {
			int randomFactor = roomSize.getZ() - windowXZSize - windowXZOffset;
			int windowZOffset = windowXZOffset + random.nextInt(randomFactor);
			BlockPos windowOffset = offset.add(0, windowYOffset, windowZOffset);
			BlockPos windowSize = new BlockPos(1, windowYSize, windowXZSize);
			structure.add(new ChildStructure(windowOffset, windowSize, Blocks.GLASS_PANE));
			return;
		}

		}
	}

	/**
	 * Calculate window offset.
	 * 
	 * @param wall
	 * @param roomSize
	 */
	int calculateWindowOffset(Wall wall, BlockPos roomSize) {

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
	public void buildDoor(Wall wall, Structure structure) {

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
		int doorXZSize = 2;

		// place door
		int doorYSize = 3;
		switch (wall.getOrientation()) {
		case X: {
			int randomFactor = roomSize.getX() - doorXZSize - doorXZOffset;
			int doorXOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(doorXOffset, 1, 0);
			BlockPos doorSize = new BlockPos(doorXZSize, doorYSize, 1);
			structure.add(new ChildStructure(doorOffset, doorSize, Blocks.AIR));
			return;
		}

		case Z: {
			int randomFactor = roomSize.getZ() - doorXZSize - doorXZOffset;
			int doorZOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(0, 1, doorZOffset);
			BlockPos doorSize = new BlockPos(1, doorYSize, doorXZSize);
			structure.add(new ChildStructure(doorOffset, doorSize, Blocks.AIR));
			return;
		}

		}

	}

	/**
	 * Calculate door offset.
	 * 
	 * @param wall
	 * @param roomSize
	 */
	int calculateDoorOffset(Wall wall, BlockPos roomSize) {

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
	public void buildTop(BlockPos offset, Block material, Structure structure) {

		BlockPos size = new BlockPos(10, 1, 10);
		offset = offset.add(-5, 0, -5);
		structure.add(new ChildStructure(offset, size, material));
	}

}
