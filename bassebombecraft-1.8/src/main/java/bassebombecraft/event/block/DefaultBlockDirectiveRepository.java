package bassebombecraft.event.block;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.geom.BlockDirective;

/**
 * Default implementation of the {@linkplain BlockDirectivesRepository}.
 */
public class DefaultBlockDirectiveRepository implements BlockDirectivesRepository {

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);
	
	BlockingQueue<BlockDirective> queue = new LinkedBlockingQueue<BlockDirective>();

	@Override
	public void add(BlockDirective directive) {
		if (directive == null)
			return;			
		
		try {
			queue.put(directive);
		} catch (InterruptedException e) {
			logger.error("Failed to add block directive: "+ directive+ " due to exception: " + e);
		}
	}

	@Override
	public void addAll(List<BlockDirective> directives) {
		if (directives == null)
			return;
		queue.addAll(directives);
	}

	@Override
	public boolean containsDirectives() {
		return (!queue.isEmpty());
	}

	@Override
	public BlockDirective getNext() throws Exception {
		if (queue.isEmpty())
			return null;
		return queue.take();
	}

	/**
	 * Factory method.
	 * 
	 * @return repository instance.
	 */
	public static BlockDirectivesRepository getInstance() {
		return new DefaultBlockDirectiveRepository();
	}

}
