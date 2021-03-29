package bassebombecraft.operator.projectile.path;

import static bassebombecraft.geom.GeometryUtils.oscillate;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Implementation of the {@linkplain Operator2} interface which move the
 * projectile alongs its path in a sine wave pattern.
 */
public class SineProjectilePath implements Operator2 {

	/**
	 * Angle variation.
	 */
	static final int ANGLE = 3;

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SineProjectilePath.class.getSimpleName();

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public SineProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * The operator also uses the port counter.
	 */
	public SineProjectilePath() {
		this(getFnGetEntity1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vector3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// calculate angle
		double angleDegrees = oscillate(-ANGLE, ANGLE);

		// rotate
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vector3d newMotionVector = motionVector.rotateYaw(angleRadians);

		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());

		return;
	}

}
