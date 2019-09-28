package bassebombecraft.event.block;

import java.util.List;

import bassebombecraft.geom.BlockDirective;

/**
 * Interface for continuous processing of {@linkplain BlockDirective}.
 */
public interface BlockDirectivesRepository {

	/**
	 * Add block directive for processing.
	 * 
	 * @param directive
	 *            block directive which is processed.
	 */
	public void add(BlockDirective directive);

	/**
	 * Add multiple block directives for processing.
	 * 
	 * The directives are processing in the order which they have in the list.
	 * 
	 * @param directive
	 *            list of block directive which is processed.
	 */
	public void addAll(List<BlockDirective> directives);

	/**
	 * Returns true of repository contains more directives.
	 * 
	 * @return true of repository contains more directives.
	 */
	public boolean containsDirectives();

	/**
	 * Return next directive. If repository doesn't contain any directives then
	 * null is returned.
	 * 
	 * @return next directive.
	 * @throws Exception
	 *             if retrieval fails.
	 */
	public BlockDirective getNext() throws Exception;
}
