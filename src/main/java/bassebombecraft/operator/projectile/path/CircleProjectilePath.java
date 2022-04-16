package bassebombecraft.operator.projectile.path;

import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which move the
 * projectile in a circle around the caster..
 * 
 * The projectile motion vector is updated.
 */
public class CircleProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = CircleProjectilePath.class.getSimpleName();

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public CircleProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * The operator also uses the port counter.
	 */
	public CircleProjectilePath() {
		this(getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vec3 motionVector = projectile.getDeltaMovement();
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
		Vec3 newMotionVector = motionVector.yRot(angleRadians);

		// update motion
		projectile.setDeltaMovement(newMotionVector.x(), newMotionVector.y(), newMotionVector.z());
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

		// make a sharp 90D turn
		if (x == 5)
			return 90;

		// do small rotations
		return 7;
	}

}
