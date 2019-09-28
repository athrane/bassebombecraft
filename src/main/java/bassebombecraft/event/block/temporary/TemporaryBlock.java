package bassebombecraft.event.block.temporary;

import bassebombecraft.geom.BlockDirective;

/**
 * Interface for temporary rendering of blocks.
 */
public interface TemporaryBlock {

	/**
	 * Get final block which is set when temporary block is expired.
	 * 
	 * @return final block which is set when temporary block is expired.
	 */
	BlockDirective getFinalBlock();

	/**
	 * Get temporary block.
	 * 
	 * @return temporary block.
	 */
	BlockDirective getTemporaryBlock();

	/**
	 * Update duration of block.
	 */
	void updateDuration();

	/**
	 * Return true if block duration is expired.
	 * 
	 * @return true if block duration is expired.
	 */
	boolean isExpired();

}
