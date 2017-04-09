package bassebombecraft.item.action.build.tower;

import static bassebombecraft.item.action.build.BuildUtils.addOakFencedDoorEntryFront;
import static bassebombecraft.item.action.build.BuildUtils.addOakFencedDoorEntryFrontSideways;
import static bassebombecraft.item.action.build.BuildUtils.addSolidStairsUp;
import static bassebombecraft.item.action.build.BuildUtils.addSpiralStairsUp;
import static bassebombecraft.item.action.build.BuildUtils.createInstance;
import static bassebombecraft.item.action.build.BuildUtils.selectFloorMaterial;
import static bassebombecraft.item.action.build.BuildUtils.selectWindowMaterial;
import static bassebombecraft.structure.ChildStructure.createAirStructure;

import java.util.Random;

import bassebombecraft.item.action.build.BuildUtils;
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
	final static IBlockState STAIRS_STATE = Blocks.STONE_BRICK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING,
			EnumFacing.SOUTH);

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
		stairsMaterial = createInstance(STAIRS_STATE, Blocks.STONE_BRICK_STAIRS, Blocks.STONEBRICK);
	}

	@Override
	public void buildRoom(Room room, Structure structure) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();
		BuildMaterial material = room.getMaterial();

		int height = size.getY();
		int width = size.getX();
		int depth = size.getZ();
		int xoffset = offset.getX();
		int yoffset = offset.getY();
		int zoffset = offset.getZ();
		BlockPos roomOffset = new BlockPos(xoffset, yoffset, zoffset);
		BlockPos roomSize = new BlockPos(width, height, depth);
		structure.add(new ChildStructure(roomOffset, roomSize, material.getBlock(), material.getState()));

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
	public void buildFloor(Room room, Structure structure) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();

		BlockPos floorSize = size.add(-2, 1 - size.getY(), -2);
		BlockPos floorOffset = offset.add(1, 0, 1);

		BuildMaterial material = selectFloorMaterial(random);
		structure.add(new ChildStructure(floorOffset, floorSize, material.getBlock(), material.getState()));
	}

	
	
	@Override
	public void buildMobSpawner(Room room, Structure structure) {
		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();

		// exit if room is small
		if(size.getX() < 5) return;
		if(size.getZ() < 5) return;
		
		BlockPos location = offset.add( size.getX() / 2 , 1,  size.getZ() / 2);		
		BuildUtils.addMobSpawner(structure, location);
	}

	@Override
	public void buildStairs(Room room, Structure structure, Structure postStructure) {

		BlockPos size = room.getSize();
		BlockPos offset = room.getOffset();

		int roomHeight = size.getY();
		BlockPos stairOffset = new BlockPos(offset.getX() + 1, offset.getY(), offset.getZ() + 2);

		// select stairs type based on available space
		if (hasSpaceForStairs(size)) {
			addSolidStairsUp(roomHeight, stairsMaterial, structure, postStructure, stairOffset);
			return;
		}

		addSpiralStairsUp(roomHeight, stairsMaterial, structure, postStructure, stairOffset);

	}

	/**
	 * Returns true if there is space for a solid staircase.
	 * 
	 * @param size
	 *            room size.
	 * 
	 * @return true if there is space for a solid staircase.
	 */
	boolean hasSpaceForStairs(BlockPos size) {
		int requiredZSpace = size.getY() + 3;
		return requiredZSpace < size.getZ();
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

		// calculate window offset
		int windowXZOffset = calculateWindowOffset(wall, roomSize);
		int windowYOffset = roomSize.getY() - windowYSize - 1;

		// get material
		BuildMaterial material = selectWindowMaterial(random);

		// place window
		switch (wall.getOrientation()) {
		case X: {
			int randomFactor = roomSize.getX() - windowXZSize - windowXZOffset;
			int windowXOffset = windowXZOffset + random.nextInt(randomFactor);
			BlockPos windowOffset = offset.add(windowXOffset, windowYOffset, 0);
			BlockPos windowSize = new BlockPos(windowXZSize, windowYSize, 1);
			structure.add(new ChildStructure(windowOffset, windowSize, material.getBlock(), material.getState()));
			return;
		}

		case Z: {
			int randomFactor = roomSize.getZ() - windowXZSize - windowXZOffset;
			int windowZOffset = windowXZOffset + random.nextInt(randomFactor);
			BlockPos windowOffset = offset.add(0, windowYOffset, windowZOffset);
			BlockPos windowSize = new BlockPos(1, windowYSize, windowXZSize);
			structure.add(new ChildStructure(windowOffset, windowSize, material.getBlock(), material.getState()));
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
		int minRoomSize = 4;
		int doorXZSize = 4;
		int doorYSize = 4;

		BlockPos offset = wall.getOffset();
		BlockPos roomSize = wall.getRoom().getSize();

		// exit if room is to small for door
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

		// place door
		switch (wall.getOrientation()) {
		case X: {
			int doorXOffset = doorXZOffset;
			int randomFactor = roomSize.getX() - doorXZSize - doorXZOffset - doorXZOffset;
			if (randomFactor > 0)
				doorXOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(doorXOffset, 1, 0);
			BlockPos doorSize = new BlockPos(doorXZSize, doorYSize, 1);
			addOakFencedDoorEntryFront(structure, doorOffset);
			return;
		}

		case Z: {
			int doorZOffset = doorXZOffset;
			int randomFactor = roomSize.getZ() - doorXZSize - doorXZOffset - doorXZOffset;
			if (randomFactor > 0)
				doorZOffset = doorXZOffset + random.nextInt(randomFactor);
			BlockPos doorOffset = offset.add(0, 1, doorZOffset);
			BlockPos doorSize = new BlockPos(1, doorYSize, doorXZSize);
			addOakFencedDoorEntryFrontSideways(structure, doorOffset);
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
			if (roomSize.getX() == 5)
				return 0;
			if (roomSize.getX() == 6)
				return 1;
			if (roomSize.getX() > 6)
				return 2;
		}
		case Z: {
			if (roomSize.getZ() == 5)
				return 0;
			if (roomSize.getZ() == 6)
				return 1;
			if (roomSize.getZ() > 6)
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
