package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.config.ModConfiguration.*;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
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
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public EmitVerticalForce2(Function<Ports, HitResult> fnGetRayTraceResult) {
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
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);

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
			Vec3 motionVec = new Vec3(0, emitVerticalForceStrength.get(), 0);
			entity.move(MoverType.SELF, motionVec);
		}
	}

}
