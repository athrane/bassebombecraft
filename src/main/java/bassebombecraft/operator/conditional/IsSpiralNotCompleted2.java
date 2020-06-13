package bassebombecraft.operator.conditional;

import java.util.List;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.util.math.BlockPos;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the spiral isn't completed.
 * 
 * WARNING: This operator is STATEFULL.
 */
public class IsSpiralNotCompleted2 implements Operator2 {

	/**
	 * Spiral counter. Exclude the centre of the spiral
	 */
	int spiralCounter = 1;

	/**
	 * Spiral coordinates.
	 */
	List<BlockPos> spiralCoordinates;

	/**
	 * Constructor.
	 * 
	 * @param spiralCoordinates list of coordinates which constitutes the spiral.
	 * @param center            centre of the spiral.
	 */
	public IsSpiralNotCompleted2(List<BlockPos> spiralCoordinates) {
		this.spiralCoordinates = spiralCoordinates;
	}

	@Override
	public Ports run(Ports ports) {

		if (spiralCounter < spiralCoordinates.size())
			ports.setResultAsSucces();
		else
			ports.setResultAsFailed();
					
		spiralCounter++;
		
		return ports;
	}
}
