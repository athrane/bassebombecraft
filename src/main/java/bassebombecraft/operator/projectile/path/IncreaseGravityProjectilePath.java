package bassebombecraft.operator.projectile.path;

import static bassebombecraft.config.ModConfiguration.increaseGravityProjectilePathFactor;
import static bassebombecraft.operator.DefaultPorts.*;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which increases the
 * gravity of the projectile along its path. The gravity of the projectile is
 * increased with some factor.
 * 
 * The projectile motion vector is updated (i.e. the y-coordinate).
 */
public class IncreaseGravityProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = IncreaseGravityProjectilePath.class.getSimpleName();

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
	public IncreaseGravityProjectilePath(Function<Ports, Entity> fnGetProjectile,
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
	public IncreaseGravityProjectilePath() {
		this(getFnGetEntity1(), getFnGetDouble1());
	}

	@Override
	public void run(Ports ports) {
		Entity projectile = applyV(fnGetProjectile, ports);
		double gravity = applyV(fnGetGravity, ports);

		// get motion vector
		Vec3 motionVector = projectile.getDeltaMovement();
		if (motionVector == null)
			return;

		// update motion
		double gravityIncrease = increaseGravityProjectilePathFactor.get() * gravity;
		projectile.setDeltaMovement(motionVector.x(), motionVector.y() - gravityIncrease, motionVector.z());
	}

}
