package bassebombecraft.operator.block;

import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which calculates the
 * next position in a expanding spiral.
 */
public class CalculateSpiralPosition2 implements Operator2 {

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Function to get block position from ports.
	 * 
	 * Contains the centre of the spiral.
	 */
	Function<Ports, BlockPos> fnGetBlockPos;

	/**
	 * Function to set block position to ports.
	 * 
	 * Used to set new calculated position in the spiral.
	 */
	BiConsumer<Ports, BlockPos> bcSetBlockPos;

	/**
	 * Constructor.
	 * 
	 * @param spiralCoordinates list of coordinates which constitutes the spiral.
	 * @param fnGetBlockPos     function to get the centre of the spiral.
	 * @param bcSetBlockPos     function to set next calculate position in the
	 *                          spiral.
	 */
	public CalculateSpiralPosition2(List<BlockPos> spiralCoordinates, Function<Ports, BlockPos> fnGetBlockPos,
			BiConsumer<Ports, BlockPos> bcSetBlockPos) {
		this.spiralCoordinates = spiralCoordinates;
		this.fnGetBlockPos = fnGetBlockPos;
		this.bcSetBlockPos = bcSetBlockPos;
	}

	@Override
	public Ports run(Ports ports) {

		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(ports.getCounter());

		// get spiral centre
		BlockPos center = fnGetBlockPos.apply(ports);

		// calculate ground coordinates
		int x = center.getX() + spiralCoord.getX();
		int y = center.getY();
		int z = center.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x, y, z);

		// get world
		World world = ports.getWorld();

		// locate ground block
		BlockPos position = locateGroundBlockPos(groundCandidate, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// add spiral position to ports
		bcSetBlockPos.accept(ports, position);

		return ports;
	}
}
