package bassebombecraft.event.block.temporary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bassebombecraft.geom.BlockDirective;

/**
 * Default implementation of the {@linkplain TemporaryBlockRepository}.
 */
public class DefaultTemporaryBlockRepository implements TemporaryBlockRepository {

	static final List<BlockDirective> EMPTY_LIST = new ArrayList<BlockDirective> ();
	
	/**
	 * Block container.
	 */
	Set<TemporaryBlock> tempBlocks;

	/**
	 * Block directives container.
	 */
	List<BlockDirective> directives;
	
	/**
	 * DefaultTemporaryBlockRepository constructor.
	 */
	public DefaultTemporaryBlockRepository() {
		super();
		this.tempBlocks = Collections.synchronizedSet(new HashSet<TemporaryBlock>());
		this.directives = new ArrayList<BlockDirective>();
	}

	@Override
	public void add(TemporaryBlock block) {
		if (tempBlocks.contains(block))
			return;
				
		// add
		tempBlocks.add(block);
		
		// add temporary block for rendering
		directives.add(block.getTemporaryBlock());
	}

	@Override
	public void remove(TemporaryBlock block) {
		tempBlocks.remove(block);
	}

	@Override
	public List<BlockDirective> getBlockDirectives() {
		if(directives.isEmpty()) return EMPTY_LIST;
		
		List<BlockDirective> list = new ArrayList<BlockDirective>(directives);
		
		// clear directives now they are rendered.
		directives.clear();

		// return directives		
		return list;
	}

	@Override
	public void clear() {
		tempBlocks.clear();
	}

	@Override
	public void updateBlockDuration() {

		synchronized (tempBlocks) {

			for (Iterator<TemporaryBlock> it = tempBlocks.iterator(); it.hasNext();) {
				TemporaryBlock tempBlock = it.next();

				// update
				tempBlock.updateDuration();

				// remove for rendering if expired
				if (tempBlock.isExpired()) {
					it.remove();
					directives.add(tempBlock.getFinalBlock());
				}
				
			}
		}
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static TemporaryBlockRepository getInstance() {
		return new DefaultTemporaryBlockRepository();
	}
}
