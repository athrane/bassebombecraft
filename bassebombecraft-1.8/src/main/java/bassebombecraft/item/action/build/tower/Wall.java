package bassebombecraft.item.action.build.tower;

import net.minecraft.util.math.BlockPos;

/**
 * Tower wall.
 */
public class Wall {

	/**
	 * Wall orientation.
	 */
	enum WallOrientation {
		X, Z,
	}

	/**
	 * Wall orientation.
	 */
	WallOrientation orientation;

	/**
	 * Room
	 */
	Room room;

	/**
	 * Wall offset.
	 */
	BlockPos offset;

	/**
	 * Wall constructor.
	 * 
	 * @param orientation
	 *            wall orientation.
	 * @param offset
	 *            wall offset.
	 * @param room
	 *            parent room.
	 */
	public Wall(WallOrientation orientation, BlockPos offset, Room room) {
		super();
		this.orientation = orientation;
		this.offset = offset;
		this.room = room;
	}

	/**
	 * Factory method for wall orientated along the x-axis.
	 * 
	 * @param offset
	 *            wall offset.
	 * @param room
	 *            parent room.
	 * @return
	 */
	public static Wall createXAxisOrientedWall(BlockPos offset, Room room) {
		return new Wall(WallOrientation.X, offset, room);
	}

	/**
	 * Factory method for wall orientated along the z-axis.
	 * 
	 * @param offset
	 *            wall offset.
	 * @param room
	 *            parent room.
	 * @return
	 */
	public static Wall createZAxisOrientedWall(BlockPos offset, Room room) {
		return new Wall(WallOrientation.Z, offset, room);
	}

	/**
	 * Get wall orientation.
	 * 
	 * @return wall orientation.
	 */
	public WallOrientation getOrientation () {
		return orientation;
	}

	/**
	 * Get wall offset.
	 * 
	 * @return wall offset.
	 */
	public BlockPos getOffset() {
		return offset;
	}

	/**
	 * Get parent room.
	 * 
	 * @return parent room.
	 */
	public Room getRoom() {
		return room;
	}
	
}
