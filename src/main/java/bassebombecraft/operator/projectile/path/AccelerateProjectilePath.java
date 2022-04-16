package bassebombecraft.operator.projectile.path;

import static bassebombecraft.config.ModConfiguration.accelerateProjectilePathAcceleration;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

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
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public AccelerateProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
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
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get motion vector
		Vec3 motionVector = projectile.getDeltaMovement();
		if (motionVector == null)
			return;

		// get length
		double length = motionVector.length();

		// calculate target motion
		double targetLength = length * accelerateProjectilePathAcceleration.get();
		Vec3 newMotionVector = motionVector.normalize().scale(targetLength);

		// update motion
		projectile.setDeltaMovement(newMotionVector.x(), newMotionVector.y(), newMotionVector.z());
	}

}
