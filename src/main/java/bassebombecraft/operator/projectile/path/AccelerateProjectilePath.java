package bassebombecraft.operator.projectile.path;

import static bassebombecraft.operator.DefaultPorts.*;

import java.util.function.Function;

import static bassebombecraft.config.ModConfiguration.*;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which accelerate the
 * projectile alongs its path.
 * 
 * The projectile motion vector is updated.
 * 
 * The motion vector is stored with a different length to increase the speed of
 * the projectile.
 */
public class AccelerateProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = AccelerateProjectilePath.class.getSimpleName();

	/**
	 * Function to get proectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;
	
	/**
	 * Acceleration.
	 */
	Double acceleration;
	
	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public AccelerateProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
		acceleration = accelerateProjectilePathAcceleration.get();
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 */
	public AccelerateProjectilePath() {
		this(getFnGetEntity1());
	}

	@Override
	public Ports run(Ports ports) {

		// get projectile
		Entity projectile = fnGetProjectile.apply(ports);
		if (projectile == null)
			return ports;

		// get motion vector
		Vec3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return ports;

		// get length
		double length = motionVector.length();

		// calculate target motion
		double targetLength = length * acceleration;
		Vec3d newMotionVector = motionVector.normalize().scale(targetLength);

		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());

		return ports;
	}

}
