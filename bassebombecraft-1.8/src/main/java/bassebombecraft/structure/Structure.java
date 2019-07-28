package bassebombecraft.structure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * Interface for a structure in the world.
 */
public interface Structure {

	/**
	 * Get structure width, i.e. the X size of the structure.
	 * 
	 * @return room depth.
	 */
	int getSizeX();

	/**
	 * Get structure height, i.e. the Y size of the structure.
	 * 
	 * @return structure depth.
	 */
	int getSizeY();

	/**
	 * Get structure depth, i.e. the Z size of the structure.
	 * 
	 * @return structure depth.
	 */
	int getSizeZ();

	/**
	 * return X offset. Legal values are [0.. width[.
	 * 
	 * @return X offset.
	 */
	int getOffsetX();

	/**
	 * return Z offset. Legal values are [0.. depth[.
	 * 
	 * @return Z offset.
	 */
	int getOffsetZ();

	/**
	 * return Y offset. Legal values are [0.. height[.
	 * 
	 * @return Y offset.
	 */
	int getOffsetY();

	/**
	 * Get block to build structure with.
	 * 
	 * @return block to build structure with.
	 */
	Block getBlock();

	/**
	 * Get block state to build structure with. If state wasn't defined for
	 * structure then the block default state is returned.
	 * 
	 * @return block state to build structure with. If state wasn't defined for
	 *         structure then the block default state is returned.
	 */
	BlockState getBlockState();

	/**
	 * Returns true if structure is composite.
	 * 
	 * @return true if structure is composite.
	 */
	boolean isComposite();

	/**
	 * Return children structures if structure is a composite. Otherwise an empty
	 * array is returned.
	 * 
	 * @return children structures.
	 */
	Structure[] getChildren();

	/**
	 * Add child structure to composite.
	 * 
	 * @param child child structure.
	 */
	void add(Structure child);

}
