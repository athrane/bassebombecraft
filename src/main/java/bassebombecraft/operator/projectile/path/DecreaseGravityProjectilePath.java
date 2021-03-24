package bassebombecraft.operator.projectile.path;

import static bassebombecraft.config.ModConfiguration.decreaseGravityProjectilePathFactor;
import static bassebombecraft.operator.DefaultPorts.getFnGetDouble1;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which increases the
 * gravity of the projectile along its path. The gravity of the projectile is
 * decreased with some factor.
 * 
 * The projectile motion vector is updated (i.e. the y-coordinate).
 */
public class DecreaseGravityProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = DecreaseGravityProjectilePath.class.getSimpleName();

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Function to get gravity.
	 */
	Function<Ports, Double> fnGetGravity;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 * @param fnGetGravity    function to get gravity.
	 */
	public DecreaseGravityProjectilePath(Function<Ports, Entity> fnGetProjectile,
			Function<Ports, Double> fnGetGravity) {
		this.fnGetProjectile = fnGetProjectile;
		this.fnGetGravity = fnGetGravity;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 * 
	 * Instance is configured with double #1 as gravity from ports.
	 */
	public DecreaseGravityProjectilePath() {
		this(getFnGetEntity1(), getFnGetDouble1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);

		// get gravity
		double gravity = fnGetGravity.apply(ports);

		// get motion vector
		Vec3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// update motion
		double gravityDecrease = decreaseGravityProjectilePathFactor.get() * gravity;
		projectile.setMotion(motionVector.getX(), motionVector.getY() + gravityDecrease, motionVector.getZ());
	}

}
