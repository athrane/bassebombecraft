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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

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
	Function<Ports, HitResult> fnGetRayTraceResult;

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
	public EmitHorizontalForce2(Function<Ports, HitResult> fnGetRayTraceResult,
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
		HitResult result = applyV(fnGetRayTraceResult, ports);
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
			Entity entity = ((EntityHitResult) result).getEntity();

			// calculate push vector
			Vec3 motion = originator.getDeltaMovement();
			Vec3 motionVec = new Vec3(motion.x(), motion.y(), motion.z());
			double x = motionVec.x * emitHorizontalForceStrength.get();
			double y = motionVec.y * emitHorizontalForceStrength.get();
			double z = motionVec.z * emitHorizontalForceStrength.get();
			Vec3 motionVecForced = new Vec3(x, y, z);
			entity.move(MoverType.SELF, motionVecForced);
		}
	}

}
