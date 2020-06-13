package bassebombecraft.operator.block.mist;

import java.util.function.Function;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Operator2} interface which applies effect
 * from {@linkplain BlockMistActionStrategy}.
 */
public class ApplyEffectFromMistStrategy2 implements Operator2 {

	/**
	 * Mist strategy.
	 */
	BlockMistActionStrategy strategy;

	/**
	 * Function to get block position from ports.
	 */
	Function<Ports, BlockPos> fnBlockPos;

	/**
	 * Constructor.
	 * 
	 * @param strategy   mist strategy to invoke.
	 * @param fnBlockPos function to get block position where stratweg should be
	 *                   invoked.
	 */
	public ApplyEffectFromMistStrategy2(BlockMistActionStrategy strategy, Function<Ports, BlockPos> fnBlockPos) {
		this.strategy = strategy;
		this.fnBlockPos = fnBlockPos;
	}

	@Override
	public Ports run(Ports ports) {

		// get position
		BlockPos pos = fnBlockPos.apply(ports);

		// apply effect
		strategy.applyEffectToBlock(pos, ports.getWorld());
		return ports;
	}
}
