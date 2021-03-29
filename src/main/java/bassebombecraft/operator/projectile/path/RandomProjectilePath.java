package bassebombecraft.operator.projectile.path;

import static bassebombecraft.geom.GeometryUtils.oscillateFloat;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.util.math.MathHelper.lerp;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

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
public class RandomProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = RandomProjectilePath.class.getSimpleName();

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public RandomProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 */
	public RandomProjectilePath() {
		this(getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vector3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// calculate angle using triangle wave function
		long x = ports.getCounter();

		// different values, then rotate the projectile
		double angleDegrees = calculateAngle(x);

		// exit if no rotation
		if (angleDegrees == 0)
			return;

		// rotate
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vector3d newMotionVector = motionVector.rotateYaw(angleRadians);

		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());
	}

	/**
	 * Calculate angle for next rotation.
	 * 
	 * @param x x parameter for the next function value.
	 * 
	 * @return angle for next rotation.
	 */
	double calculateAngle(long x) {

		// delay rotation to after n ticks
		if (x < 5)
			return 0;

		// calc rotation
		float oscValue = oscillateFloat(0, 1);
		return lerp(oscValue, -20, 20);
	}

}
