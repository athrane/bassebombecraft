package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.config.ModConfiguration.emitHorizontalForceStrength;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which hits a mob with
 * a horizontal force.
 * 
 * If a block is hit then NO-OP.
 */
public class EmitHorizontalForce2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = EmitHorizontalForce2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to get entity (projectile) which caused the hit in the ray trace
	 * result.
	 */
	Function<Ports, Entity> fnGetRayOriginator;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult  function to get ray trace result.
	 * @param fnGetRayOriginator function to get ray originator.
	 */
	public EmitHorizontalForce2(Function<Ports, RayTraceResult> fnGetRayTraceResult,
			Function<Ports, Entity> fnGetRayOriginator) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetRayOriginator = fnGetRayOriginator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with entity #1 from ports.
	 */
	public EmitHorizontalForce2() {
		this(getFnGetRayTraceResult1(), getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		RayTraceResult result = applyV(fnGetRayTraceResult, ports);
		Entity originator = applyV(fnGetRayOriginator, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// if entity is hit emit force
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// calculate push vector
			Vector3d motion = originator.getMotion();
			Vector3d motionVec = new Vector3d(motion.getX(), motion.getY(), motion.getZ());
			double x = motionVec.x * emitHorizontalForceStrength.get();
			double y = motionVec.y * emitHorizontalForceStrength.get();
			double z = motionVec.z * emitHorizontalForceStrength.get();
			Vector3d motionVecForced = new Vector3d(x, y, z);
			entity.move(MoverType.SELF, motionVecForced);
		}
	}

}
