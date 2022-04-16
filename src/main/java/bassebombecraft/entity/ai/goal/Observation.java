package bassebombecraft.entity.ai.goal;

import net.minecraft.core.BlockPos;

/**
 * Interface for observation of a combat situation.
 */
public interface Observation {

	/**
	 * Get current observations as string.
	 * 
	 * @return current observations as string.
	 */
	public String getObservationAsString();

	/**
	 * Get entity position.
	 * 
	 * @return entity position
	 */
	public BlockPos getEntityPosition();

	/**
	 * Get target position.
	 * 
	 * @return target position
	 */
	public BlockPos getTargetPosition();

	/**
	 * Get target health.
	 * 
	 * @return target health.
	 */	
	public float getTargetHeatlh();
	
}
