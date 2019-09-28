package bassebombecraft.projectile;

import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Utility class for projectile calculations.
 */
public class ProjectileUtils {

	/**
	 * Return true if block was hit by ray trace result.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if block was hit.
	 */
	public static boolean isBlockHit(RayTraceResult result) {
		if (result == null)
			return false;
	    return (result.getType() == RayTraceResult.Type.BLOCK);
	}
	
	/**
	 * Return true if entity was hit by ray trace result.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if entity was hit.
	 */
	public static boolean isEntityHit(RayTraceResult result) {
		if (result == null)
			return false;
	    return (result.getType() == RayTraceResult.Type.ENTITY);
	}

	/**
	 * Return true if ray trace result missed.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result was a miss.
	 */
	public static boolean isNothingHit(RayTraceResult result) {
		if (result == null)
			return false;
	    return (result.getType() == RayTraceResult.Type.MISS);
	}
	
	/**
	 * Return true if ray trace result a {@linkplain EntityRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain EntityRayTraceResult}.
	 */
	public static boolean isTypeEntityRayTraceResult(RayTraceResult result) {
		if (result == null)
			return false;
		return result instanceof EntityRayTraceResult;
	}

	/**
	 * Return true if ray trace result a {@linkplain BlockRayTraceResult}.
	 * 
	 * @param result result to test.
	 * 
	 * @return true if result is a {@linkplain BlockRayTraceResult}.
	 */
	public static boolean isTypeBlockRayTraceResult(RayTraceResult result) {
		if (result == null)
			return false;
		return result instanceof BlockRayTraceResult;
	}
	
}
