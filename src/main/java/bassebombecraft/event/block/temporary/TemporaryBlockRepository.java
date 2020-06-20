package bassebombecraft.event.block.temporary;

import java.util.List;

import bassebombecraft.geom.BlockDirective;

/**
 * Interface for repository for temporary rendering of blocks.
 */
public interface TemporaryBlockRepository {

	/**
	 * Register block for temporary rendering.
	 * 
	 * @param block temporary block to register.
	 */
	public void add(TemporaryBlock block);

	/**
	 * Remove block from rendering.
	 * 
	 * @param block
	 *            block to remove.
	 */
	public void remove(TemporaryBlock block);
	
	/**
	 * Get block directives to be rendered.
	 * 
	 * @return list of block directives to be rendered.
	 */
	List<BlockDirective> getBlockDirectives();

	/**
	 * Clear particles.
	 */
	public void clear();

	/**
	 * Update block duration.
	 */
	@Deprecated
	public void updateBlockDuration();

}
