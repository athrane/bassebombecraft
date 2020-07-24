package bassebombecraft.operator.block;

import static bassebombecraft.geom.GeometryUtils.ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK;
import static bassebombecraft.geom.GeometryUtils.calculateSpiral;
import static bassebombecraft.geom.GeometryUtils.locateGroundBlockPos;
import static bassebombecraft.operator.DefaultPorts.getFnGetBlockPosition1;
import static bassebombecraft.operator.DefaultPorts.*;

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
	 * list of coordinates which constitutes the spiral.
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
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld; 	
	
	/**
	 * Constructor.
	 * 
	 * @param spiralSize    size of the spiral.
	 * @param fnGetBlockPos function to get the centre of the spiral.
	 * @param bcSetBlockPos function to set next calculate position in the spiral.
	 * @param fnGetWorld function to get world.                   
	 */
	public CalculateSpiralPosition2(int spiralSize, Function<Ports, BlockPos> fnGetBlockPos,
			BiConsumer<Ports, BlockPos> bcSetBlockPos, Function<Ports, World> fnGetWorld) {
		this.fnGetBlockPos = fnGetBlockPos;
		this.bcSetBlockPos = bcSetBlockPos;
		this.fnGetWorld = fnGetWorld;		
		this.spiralCoordinates = calculateSpiral(spiralSize, spiralSize);		
	}

	/**
	 * Constructor.
	 * 
	 * @param spiralSize    size of the spiral.
	 * 
	 * Instance is configured with block position #1 (as the centre of the spiral) from ports.
	 * 
	 * Instance is configured to set block position #2 (next calculate position in the spiral) from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 */
	public CalculateSpiralPosition2(int spiralSize) {
		this(spiralSize, getFnGetBlockPosition1(), getBcSetBlockPosition2(), getFnWorld1());
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
		BlockPos candidate = new BlockPos(x, y, z);

		// get world
		World world = fnGetWorld.apply(ports);

		// locate ground block
		BlockPos position = locateGroundBlockPos(candidate, ITERATIONS_TO_QUERY_FOR_GROUND_BLOCK, world);

		// add spiral position to ports
		bcSetBlockPos.accept(ports, position);

		return ports;
	}
}
