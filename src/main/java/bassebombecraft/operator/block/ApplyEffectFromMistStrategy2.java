package bassebombecraft.operator.block;

import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.item.action.mist.block.BlockMistActionStrategy;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

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
	 * Function to get world from ports.
	 */
	Function<Ports, Level> fnGetWorld;

	/**
	 * Constructor.
	 * 
	 * @param strategy   mist strategy to invoke.
	 * @param fnBlockPos function to get block position where strategy is invoked.
	 * @param fnGetWorld function to get world.
	 */
	public ApplyEffectFromMistStrategy2(BlockMistActionStrategy strategy, Function<Ports, BlockPos> fnBlockPos,
			Function<Ports, Level> fnGetWorld) {
		this.strategy = strategy;
		this.fnBlockPos = fnBlockPos;
		this.fnGetWorld = fnGetWorld;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with block position #1 (as the block position where
	 * strategy is invoked) from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 * 
	 * @param strategy mist strategy to invoke.
	 */
	public ApplyEffectFromMistStrategy2(BlockMistActionStrategy strategy) {
		this(strategy, getFnGetBlockPosition1(), getFnWorld1());
	}

	@Override
	public void run(Ports ports) {
		BlockPos pos = applyV(fnBlockPos, ports);
		Level world = applyV(fnGetWorld, ports);

		// apply effect
		strategy.applyEffectToBlock(pos, world);
	}
}
