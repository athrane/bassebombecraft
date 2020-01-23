package bassebombecraft.rendering;

import net.minecraft.util.math.RayTraceResult;

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

	/**
	 * Get ray trace result.
	 * 
	 * @return ray trace result
	 */
	RayTraceResult getResult();

	/**
	 * Returns true if ray trace result is defined.
	 * 
	 * @return true if ray trace result is defined
	 */
	boolean isRayTraceResultDefined();

}
