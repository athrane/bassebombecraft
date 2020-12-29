package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which hits a mob with
 * a vertical force.
 * 
 * If a block is hit then NO-OP.
 */
public class EmitVerticalForce2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = EmitVerticalForce2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public EmitVerticalForce2(Function<Ports, RayTraceResult> fnGetRayTraceResult) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 */
	public EmitVerticalForce2() {
		this(getFnGetRayTraceResult1());
	}

	@Override
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);
		if (result == null)
			return ports;

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// if entity is hit emit force
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return ports;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// calculate push vector
			Vec3d motionVec = new Vec3d(0, emitVerticalForceStrength.get(), 0);
			entity.move(MoverType.SELF, motionVec);
		}

		return ports;
	}

}
