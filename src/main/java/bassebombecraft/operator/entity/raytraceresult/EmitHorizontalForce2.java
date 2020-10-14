package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getFnWorld1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
	 * Function to get world from ports.
	 */
	Function<Ports, World> fnGetWorld;

	/**
	 * Function to get entity (projectile) which caused the hit in the ray trace
	 * result.
	 */
	Function<Ports, Entity> fnGetRayOriginator;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult  function to get ray trace result.
	 * @param fnGetWorld         function to get world.
	 * @param fnGetRayOriginator function to get ray originator.
	 */
	public EmitHorizontalForce2(Function<Ports, RayTraceResult> fnGetRayTraceResult, Function<Ports, World> fnGetWorld,
			Function<Ports, Entity> fnGetRayOriginator) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
		this.fnGetWorld = fnGetWorld;
		this.fnGetRayOriginator = fnGetRayOriginator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 * 
	 * Instance is configured with world #1 from ports.
	 * 
	 * Instance is configured with entity #1 from ports.
	 */
	public EmitHorizontalForce2() {
		this(getFnGetRayTraceResult1(), getFnWorld1(), getFnGetEntity1());
	}

	@Override
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);
		if (result == null)
			return ports;

		// get world
		World world = fnGetWorld.apply(ports);
		if (world == null)
			return ports;

		// get originator entity
		Entity originator = fnGetRayOriginator.apply(ports);
		if (originator == null)
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
			Vec3d motion = originator.getMotion();
			Vec3d motionVec = new Vec3d(motion.getX(), motion.getY(), motion.getZ());
			double x = motionVec.x * emitHorizontalForceStrength.get();
			double y = motionVec.y * emitHorizontalForceStrength.get();
			double z = motionVec.z * emitHorizontalForceStrength.get();
			Vec3d motionVecForced = new Vec3d(x, y, z);
			entity.move(MoverType.SELF, motionVecForced);
		}

		return ports;
	}

}
