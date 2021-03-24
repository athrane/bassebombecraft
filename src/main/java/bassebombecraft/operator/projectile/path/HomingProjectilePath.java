package bassebombecraft.operator.projectile.path;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which computes random
 * path for the projectile.
 * 
 * The path is computed and updates the projectile motion vector.
 * 
 * The path is computed by a random rotation of the yaw (y-axis).
 * 
 * The computed motion vector is stored with the same length as the original
 * motion vector in order to only change the direction be not the speed of the
 * projectile.
 */
public class HomingProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = HomingProjectilePath.class.getSimpleName();

	/**
	 * Homing factor.
	 */
	static final double HOMING_FACTOR = 0.10D;

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Function to get entities.
	 */
	Function<Ports, Entity[]> fnGetEntities1;

	/**
	 * Find entities operator instance.
	 */
	Operator2 findEntitiesOp;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 * @param fnGetEntities1  function to get entities.
	 */
	public HomingProjectilePath(Function<Ports, Entity> fnGetProjectile, Function<Ports, Entity[]> fnGetEntities1) {
		this.fnGetProjectile = fnGetProjectile;
		this.fnGetEntities1 = fnGetEntities1;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * Instance configured with entity array #1 in the ports.
	 */
	public HomingProjectilePath() {
		this(getFnGetEntity1(), getFnGetEntities1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);
		Entity[] entities = applyV(fnGetEntities1, ports);

		// get motion vector
		Vec3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// get entity to home in on
		if (entities == null)
			return;

		// exit if target are found
		if (entities.length == 0)
			return;

		Entity target = entities[0];
		if (target == null)
			return;

		// get motion vector length to preserve speed
		double motionVectorLength = motionVector.length();

		// normalize motion vector vector
		Vec3d motionVectorNormalized = motionVector.normalize();

		// calculate direction vector
		Vec3d targetPos = target.getPositionVec();
		Vec3d projectilePos = projectile.getPositionVec();
		Vec3d desiredDirection = targetPos.subtract(projectilePos);

		// normalize direction vector
		Vec3d desiredDirectionNormalized = desiredDirection.normalize();

		// calculate steering vector
		Vec3d steeringForce = desiredDirectionNormalized.subtract(motionVectorNormalized);

		Vec3d steeringForceScaled = steeringForce.scale(HOMING_FACTOR);
		Vec3d newMotionVector = motionVectorNormalized.add(steeringForceScaled);

		// normalize and scale motion vector to preserve original length
		newMotionVector = newMotionVector.normalize();
		newMotionVector = newMotionVector.scale(motionVectorLength);

		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());
	}

}
