package bassebombecraft.item.action.build.tower;

import bassebombecraft.structure.Structure;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

/**
 * Interface for building a tower.
 */
public interface Builder {

	/**
	 * Build room.
	 * 
	 * @param room
	 *            room to build from.
	 * @param structure
	 *            structure to add structure to.
	 */
	void buildRoom(Room room, Structure structure);

	/**
	 * Build stairs.
	 * 
	 * @param room
	 *            room to build stairs in.
	 * @param structure
	 *            structure to add structure to.
	 * @param postStructure
	 *            structure to add structure for post processing.
	 */
	void buildStairs(Room room, Structure structure, Structure postStructure);

	/**
	 * Build windows in exterior wall.
	 * 
	 * @param wall
	 *            wall to build windows in.
	 * @param structure
	 *            structure to add structure to.
	 */
	void buildWindow(Wall wall, Structure structure);

	/**
	 * Build door in interior wall.
	 * 
	 * @param wall
	 *            wall to build door in.
	 * @param structure
	 *            structure to add structure to.
	 */
	void buildDoor(Wall wall, Structure structure);

	/**
	 * Build tower top.
	 * 
	 * @param offset
	 *            top offset.
	 * @param material
	 *            top material.
	 * @param structure
	 *            structure to add structure to.
	 */
	void buildTop(BlockPos offset, Block material, Structure structure);

}
