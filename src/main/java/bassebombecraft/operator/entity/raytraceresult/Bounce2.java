package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.entity.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which bounces
 * projectile on impact on block.
 */
public class Bounce2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Bounce2.class.getSimpleName();

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 * @param fnGetProjectile   function to get projectile entity.
	 */
	public Bounce2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, Entity> fnGetProjectile) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 */
	public Bounce2() {
		this(getFnGetRayTraceResult1(), getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		RayTraceResult result = applyV(fnGetRayTraceResult, ports);
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vector3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// bounce projectile motion
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get impact info
			Direction impactFace = blockResult.getFace();
			Axis impactAxis = impactFace.getAxis();

			// calculate bounced motion vector
			Vector3d bouncedVector = bounceMotionVector(impactAxis, motionVector);

			// set bounced motion
			projectile.setMotion(bouncedVector);
		}
	}

	/**
	 * Calculate bounced motion vector.
	 * 
	 * @param impactAxis   impact axis.
	 * @param motionVector motion vector.
	 * 
	 * @return bounced motion vector
	 */
	Vector3d bounceMotionVector(Axis impactAxis, Vector3d motionVector) {
		switch (impactAxis) {
		case X:
			return motionVector.mul(-1, 1, 1);
		case Y:
			return motionVector.mul(1, -1, 1);
		case Z:
			return motionVector.mul(1, 1, -1);
		}
		return motionVector;
	}

}
