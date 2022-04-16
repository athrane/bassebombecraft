package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.world.phys.HitResult;

/**
 * Implementation of the {@linkplain Operator} interface which executes the
 * embedded operator if a entity was hit by ray trace result.
 * 
 * @deprecated Replace usage with {@linkplain IsLivingEntityHitInRaytraceResult2}
 */
@Deprecated
public class IfEntityWasHit implements Operator {

	/**
	 * Embedded operator.
	 */
	Operator operator;

	/**
	 * RayTraceResult supplier.
	 */
	Supplier<HitResult> splRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult projectile ray trace result.
	 * @param operator          embedded operator which is executed if effect is
	 *                          active.
	 */
	public IfEntityWasHit(Supplier<HitResult> splRayTraceResult, Operator operator) {
		this.operator = operator;
		this.splRayTraceResult = splRayTraceResult;
	}

	@Override
	public void run() {

		// get ray trace result
		HitResult result = splRayTraceResult.get();

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
