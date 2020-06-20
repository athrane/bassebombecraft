package bassebombecraft.operator.block;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Operator2} interface which resets the
 * spiral computation when an entity (the invoker) moves, i.e. the spiral center
 * is different from the invoker position.
 * 
 * Resetting the spiral computation consists of resetting the counter and the
 * centre of the spiral.
 * 
 * Sets the result port to true.
 * 
 * The state of the counter is maintained in the {@linkplain Ports} using its
 * counter.
 */
public class ResetSpiralOnMovement2 implements Operator2 {

	/**
	 * Function to get invoker position from ports.
	 */
	Function<Ports, BlockPos> fnGetInvokerPos;

	/**
	 * Function to get spiral center from ports.
	 */
	Function<Ports, BlockPos> fnGetCenterPos;

	/**
	 * Function to set spiral center in ports.
	 */
	BiConsumer<Ports, BlockPos> bcSetCenterPos;

	/**
	 * Minimum value for counter.
	 */
	int min;

	/**
	 * Constructor.
	 * 
	 * @param min             counter reset value.
	 * @param fnGetInvokerPos function to get invoker position.
	 * @param fnGetCenterPos  function to get spiral center position.
	 * @param bcSetCenterPos  function to set spiral center position.
	 */
	public ResetSpiralOnMovement2(int min, Function<Ports, BlockPos> fnGetInvokerPos,
			Function<Ports, BlockPos> fnGetCenterPos, BiConsumer<Ports, BlockPos> bcSetCenterPos) {
		this.min = min;
		this.fnGetInvokerPos = fnGetInvokerPos;
		this.fnGetCenterPos = fnGetCenterPos;
		this.bcSetCenterPos = bcSetCenterPos;
	}

	@Override
	public Ports run(Ports ports) {

		// clear result port
		ports.setResultAsSucces();

		// get positions
		BlockPos pos1 = fnGetInvokerPos.apply(ports);
		BlockPos pos2 = fnGetCenterPos.apply(ports);

		// reset if one position is null
		if ((pos1 == null) || (pos2 == null)) {
			ports.setCounter(min);
			bcSetCenterPos.accept(ports, pos1);
			return ports;
		}

		// exit if the positions are equal
		if (pos1.equals(pos2))
			return ports;

		// reset
		ports.setCounter(min);
		bcSetCenterPos.accept(ports, pos1);

		return ports;
	}
}
