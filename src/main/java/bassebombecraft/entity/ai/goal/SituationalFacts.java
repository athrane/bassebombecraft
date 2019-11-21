package bassebombecraft.entity.ai.goal;

import java.util.stream.Stream;

/**
 * Facts about a combat situation.
 */
public interface SituationalFacts {

	/**
	 * Update facts based on observations.
	 * 
	 * @param observations stream of observations.
	 */
	void update(Stream<Observation> observations);

	/**
	 * Return whether target is close, i.e. within the minimum attack range.
	 * 
	 * @return true if target is close.
	 */
	public boolean isTargetClose();

	/**
	 * Return whether the health of the target decreased between observations.
	 * 
	 * @return true if the health of the target decreased between observations.
	 */
	public boolean isTargetHealthDecreased();

	/**
	 * Get current facts as string.
	 * 
	 * @return current facts as string.
	 */
	public String getFactsAsString();

}
