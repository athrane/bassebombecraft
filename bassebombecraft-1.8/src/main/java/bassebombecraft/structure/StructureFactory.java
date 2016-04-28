package bassebombecraft.structure;

import bassebombecraft.geom.WorldQuery;
import net.minecraft.block.Block;

/**
 * Interface for structure factory.
 */
public interface StructureFactory {
	
	/**
	 * Create structure.
	 * 
	 * @param isGroundBlock set to true if structure should be created based on
	 * the interaction with a ground block.
	 * @param sourceBlock the source block whose destruction triggered the structure.
	 * @param worldQuery world query object.
	 * 
	 * @return structure which defines functionality of an item.
	 */
	public Structure getInstance(boolean isGroundBlock, Block sourceBlock, WorldQuery worldQuery);		
	
	/**
	 * Returns true if coordinates in returned structure should
	 * should use player feet position as global offset or
	 * the coordinates of the destroyed block should be used as offset.

	 * @return true if coordinates in returned structure should
	 * should use player feet position as global offset or
	 * the coordinates of the destroyed block should be used as offset.
	 */
	public boolean calculateOffsetFromPlayerFeet();
}
