package bassebombecraft.rendering;

/**
 * Interface for rendering info needed to render the HUD item.
 */
public interface RenderingInfo {

	/**
	 * Get render view entity X coordinate, modified with partial ticks. Used for
	 * translation of GL matrix prior to rendering.
	 * 
	 * @return render view entity X coordinate, modified with partial ticks.
	 */
	double getRveTranslatedViewX();

	/**
	 * Get render view entity Y coordinate, modified with partial ticks. Used for
	 * translation of GL matrix prior to rendering.
	 * 
	 * @return render view entity Y coordinate, modified with partial ticks.
	 */
	double getRveTranslatedViewY();

	/**
	 * Get render view entity Y coordinate, modified with partial ticks. Used for
	 * translation of GL matrix prior to rendering.
	 * 
	 * The translated Y coordinate is offset with player eye height.
	 * 
	 * @return render view entity Y coordinate, modified with partial ticks and
	 *         offset with player eye height.
	 */
	double getRveTranslatedViewYOffsetWithPlayerEyeHeight();

	/**
	 * Get render view entity Z coordinate, modified with partial ticks. Used for
	 * translation of GL matrix prior to rendering.
	 * 
	 * @return render view entity Z coordinate, modified with partial ticks.
	 */
	double getRveTranslatedViewZ();

	/**
	 * Get partial ticks.
	 * 
	 * @return partial ticks.
	 */
	float getPartialTicks();

}
