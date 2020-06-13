package bassebombecraft.operator.block.mist;

import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;

import java.util.List;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which calculates the
 * next position in a expanding spiral.
 * 
 * WARNING: This operation is STATEFULL.
 */
public class CalculateSpiralPosition2 implements Operator2 {

	/**
	 * Spiral counter. Exclude the centre of the spiral
	 */
	int spiralCounter = 1;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Spiral centre.
	 */
	BlockPos center;

	/**
	 * Constructor.
	 * 
	 * @param spiralCoordinates list of coordinates which constitutes the spiral.
	 * @param center            centre of the spiral.
	 */
	public CalculateSpiralPosition2(List<BlockPos> spiralCoordinates, BlockPos center) {
		this.spiralCoordinates = spiralCoordinates;
		this.center = center;
	}

	@Override
	public Ports run(Ports ports) {

		// get world
		World world = ports.getWorld();

		// exit if entire spiral is processed
		if (spiralCounter >= spiralCoordinates.size())
			return ports;

		// get next spiral coordinate
		BlockPos spiralCoord = spiralCoordinates.get(spiralCounter);

		// calculate ground coordinates
		int x = center.getX() + spiralCoord.getX();
		int y = center.getY();
		int z = center.getZ() + spiralCoord.getZ();
		BlockPos groundCandidate = new BlockPos(x, y, z);

		// locate ground block
		BlockPos mistPosition = locateGroundBlockPos(groundCandidate, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// add mist position to ports
		ports.setBlockPosition(mistPosition);

		spiralCounter++;

		return ports;
	}
}
