package bassebombecraft.item.action.build.tower;

import bassebombecraft.structure.CompositeStructure;
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
	 * @param composite
	 *            composite structure to add structure to.
	 */
	void buildRoom(Room room, CompositeStructure composite);

	/**
	 * Build stairs.
	 * 
	 * @param room
	 *            room to build stairs in.
	 * @param composite
	 *            composite structure to add structure to.
	 */
	void buildStairs(Room room, CompositeStructure composite);

	/**
	 * Build windows in exterior wall.
	 * 
	 * @param wall
	 *            wall to build windows in.
	 * @param composite
	 *            composite structure to add structure to.
	 */
	void buildWindow(Wall wall, CompositeStructure composite);

	/**
	 * Build door in interior wall.
	 * 
	 * @param wall
	 *            wall to build door in.
	 * @param composite
	 *            composite structure to add structure to.
	 */	
	void buildDoor(Wall wall, CompositeStructure composite);
	
	/**
	 * Build tower top.
	 * 
	 * @param offset
	 *            top offset.
	 * @param material
	 *            top material.
	 * @param composite
	 *            composite structure to add structure to.
	 */
	void buildTop(BlockPos offset, Block material, CompositeStructure composite);


}
