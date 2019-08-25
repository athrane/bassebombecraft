package bassebombecraft.event.block;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.Logger;

import bassebombecraft.geom.BlockDirective;

/**
 * Default implementation of the {@linkplain BlockDirectivesRepository}.
 */
public class DefaultBlockDirectiveRepository implements BlockDirectivesRepository {

	/**
	 * Queue of block directives to process.
	 */
	BlockingQueue<BlockDirective> queue = new LinkedBlockingQueue<BlockDirective>();

	@Override
	public void add(BlockDirective directive) {
		if (directive == null)
			return;
		try {
			queue.put(directive);
		} catch (InterruptedException e) {
			Logger logger = getBassebombeCraft().getLogger();
			logger.error("Failed to add block directive: " + directive + " due to exception: " + e);
		}
	}

	@Override
	public void addAll(List<BlockDirective> directives) {
		if (directives.isEmpty())
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
