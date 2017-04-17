package bassebombecraft.item.action.build.tower;

import net.minecraft.util.math.BlockPos;

/**
 * Tower room.
 */
public class Room {

	/**
	 * Room type.
	 * 
	 * Room #1: -x,-z => NW Room #2: +x,-z => NE Room #3: -x,+z => SW Room #4:
	 * +x,+z => SE
	 */
	enum RoomType {
		NW, NE, SW, SE
	}

	/**
	 * Local room offset.
	 */
	BlockPos offset;

	/**
	 * Room size.
	 */
	BlockPos size;

	/**
	 * Room material.
	 */
	BuildMaterial material;

	/**
	 * Room type.
	 */
	RoomType type;

	/**
	 * Northern wall.
	 */
	Wall northernWall;

	/**
	 * Southern wall.
	 */
	Wall southernWall;

	/**
	 * Western wall.
	 */
	Wall westernWall;

	/**
	 * Eastern wall.
	 */
	Wall easternWall;

	/**
	 * Room constructor.
	 * 
	 * @param roomType
	 *            room type.
	 * @param offset
	 *            room offset
	 * @param size
	 *            room size
	 * @param material
	 *            room material.
	 * 
	 */
	Room(RoomType type, BlockPos offset, BlockPos size, BuildMaterial material) {
		super();
		this.type = type;
		this.offset = offset;
		this.size = size;
		this.material = material;

		calculateWalls();
	}

	/**
	 * Calculate wall objects.
	 */
	void calculateWalls() {
		// create walls
		northernWall = Wall.createXAxisOrientedWall(offset, this);
		southernWall = Wall.createXAxisOrientedWall(offset.add(0, 0, size.getZ() - 1), this);
		westernWall = Wall.createZAxisOrientedWall(offset, this);
		easternWall = Wall.createZAxisOrientedWall(offset.add(size.getX() - 1, 0, 0), this);
	}

	public void resize(int width, int depth) {
		switch (type) {

		case NW: {
			offset = offset.add(-width, 0, -depth);
			size = size.add(width, 0, depth);
			calculateWalls();
			return;
		}

		case NE: {
			offset = offset.add(0, 0, -depth);
			size = size.add(width, 0, depth);
			calculateWalls();
			return;
		}

		case SW: {
			offset = offset.add(-width, 0, 0);
			size = size.add(width, 0, depth);
			calculateWalls();
			return;
		}

		case SE: {
			offset = offset.add(0, 0, 0);
			size = size.add(width, 0, depth);
			calculateWalls();
			return;
		}

		default:
		}
	}

	public Wall[] getExternalWalls() {
		switch (type) {

		case NW:
			return new Wall[] { northernWall, westernWall };

		case NE:
			return new Wall[] { northernWall, easternWall };

		case SW:
			return new Wall[] { southernWall, westernWall };

		case SE:
			return new Wall[] { southernWall, easternWall };

		default:
			return new Wall[] {};
		}
	}

	public Wall[] getInteriorWalls() {
		switch (type) {

		case NW:
			return new Wall[] { southernWall, easternWall };

		case NE:
			return new Wall[] { southernWall, westernWall };

		case SW:
			return new Wall[] { northernWall, easternWall };

		case SE:
			return new Wall[] { northernWall, westernWall };

		default:
			return new Wall[] {};
		}
	}

	/**
	 * Get room size.
	 * 
	 * @return room size.
	 */
	public BlockPos getSize() {
		return size;
	}

	/**
	 * Get room offset.
	 * 
	 * @return room offset.
	 */
	public BlockPos getOffset() {
		return offset;
	}

	/**
	 * Get room material.
	 * 
	 * @return room material.
	 */
	public BuildMaterial getMaterial() {
		return material;
	}

	/**
	 * Room #1 (NW) factory method.
	 * 
	 * @param offset
	 *            room offset
	 * 
	 * @param size
	 *            room size
	 * 
	 * @param material
	 *            room material.
	 * 
	 */
	public static Room createNWRoom(BlockPos offset, BlockPos size, BuildMaterial material) {
		return new Room(RoomType.NW, offset, size, material);
	}

	/**
	 * Room #2 (NE) factory method.
	 * 
	 * @param offset
	 *            room offset
	 * 
	 * @param size
	 *            room size
	 * 
	 * @param material
	 *            room material.
	 * 
	 */
	public static Room createNERoom(BlockPos offset, BlockPos size, BuildMaterial material) {
		return new Room(RoomType.NE, offset, size, material);
	}

	/**
	 * Room #3 (SW) factory method.
	 * 
	 * @param offset
	 *            room offset
	 * 
	 * @param size
	 *            room size
	 * 
	 * @param material
	 *            room material.
	 * 
	 */
	public static Room createSWRoom(BlockPos offset, BlockPos size, BuildMaterial material) {
		return new Room(RoomType.SW, offset, size, material);
	}

	/**
	 * Room (SE) #4 factory method.
	 * 
	 * @param offset
	 *            room offset
	 * 
	 * @param size
	 *            room size
	 * 
	 * @param material
	 *            room material.
	 * 
	 */
	public static Room createSERoom(BlockPos offset, BlockPos size, BuildMaterial material) {
		return new Room(RoomType.SE, offset, size, material);
	}

	/**
	 * Return the type of room.
	 * 
	 * @return the type of room.
	 */
	public RoomType getType() {
		return this.type;

	}

}