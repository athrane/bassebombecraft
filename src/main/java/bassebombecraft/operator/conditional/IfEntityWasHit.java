package bassebombecraft.operator.conditional;

import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if a entity was hit by ray trace result.
 */
public class IfEntityWasHit implements Operator {

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * RayTraceResult supplier.
	 */
	Supplier<RayTraceResult> splRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult projectile ray trace result.
	 * @param operator          embedded operator which is executed if effect is
	 *                          active.
	 */
	public IfEntityWasHit(Supplier<RayTraceResult> splRayTraceResult, Operator operator) {
		this.operator = operator;
		this.splRayTraceResult = splRayTraceResult;
	}

	@Override
	public void run() {

		// get ray trace result
		RayTraceResult result = splRayTraceResult.get();

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// exit if an entity wasn't hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		operator.run();
	}

}
