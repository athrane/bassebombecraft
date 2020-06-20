package bassebombecraft.event.block.temporary;

import bassebombecraft.geom.BlockDirective;

/**
 * Default implementation of {@linkplain TemporaryBlock}.
 */
public class DefaultTemporaryBlock implements TemporaryBlock {

	private int duration;
	private BlockDirective tempBlock;
	private BlockDirective finalBlock;

	/**
	 * DefaultTemporaryBlock constructor.
	 * 
	 * @param duration
	 *            duration of the temporary block.
	 * @param tempBlock
	 *            temporary block to set.
	 * @param finalBlock
	 *            final block to set after duration.
	 */
	private DefaultTemporaryBlock(int duration, BlockDirective tempBlock, BlockDirective finalBlock) {
		this.duration = duration;
		this.tempBlock = tempBlock;
		this.finalBlock = finalBlock;

	}

	@Override
	public BlockDirective getFinalBlock() {
		return finalBlock;
	}

	@Override
	public BlockDirective getTemporaryBlock() {
		return tempBlock;
	}

	@Override
	public void updateDuration() {
		if (duration > 0)
			duration--;
	}

	@Override
	public boolean isExpired() {
		return (duration == 0);
	}

	/**
	 * Factory method.
	 * 
	 * @param duration
	 *            duration of the temporary block.
	 * @param tempBlock
	 *            temporary block to set.
	 * @param finalBlock
	 *            final block to set after duration.
	 * 
	 * @return particle rendering object.
	 */
	public static DefaultTemporaryBlock getInstance(int duration, BlockDirective tempBlock, BlockDirective finalBlock) {
		return new DefaultTemporaryBlock(duration, tempBlock, finalBlock);
	}	
}
