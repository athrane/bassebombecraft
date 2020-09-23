package bassebombecraft.operator.projectile.path;

import static bassebombecraft.config.ModConfiguration.increaseGravityProjectilePathFactor;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;

import java.util.function.Function;

import bassebombecraft.config.ProjectileEntityConfig;
import bassebombecraft.entity.projectile.GenericCompositeProjectileEntity;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which increases the
 * gravity of the projectile alongs its path. The gravity of the projectile is
 * increased with 2x.
 * 
 * The projectile motion vector is updated (i.e. the y-coordinate).
 * 
 */
public class IncreaseGravityProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = IncreaseGravityProjectilePath.class.getSimpleName();

	/**
	 * Function to get proectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public IncreaseGravityProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 */
	public IncreaseGravityProjectilePath() {
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

		// exit if projectile isn't expected type
		if (!(projectile instanceof GenericCompositeProjectileEntity))
			return ports;

		// type cast
		GenericCompositeProjectileEntity typedProjectile = (GenericCompositeProjectileEntity) projectile;

		// get gravity
		ProjectileEntityConfig config = typedProjectile.getConfiguration();
		double gravity = config.gravity.get();

		// update motion
		double gravityIncrease = increaseGravityProjectilePathFactor.get() * gravity;
		projectile.setMotion(motionVector.getX(), motionVector.getY() - gravityIncrease, motionVector.getZ());

		return ports;
	}

}
