package bassebombecraft.operator.projectile.path;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Random;
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

		// get random
		Random random = getBassebombeCraft().getRandom();

		// get motion vector
		Vec3d motionVector = projectile.getMotion();
		if (motionVector == null)
			return;

		// rotate
		float angleDegrees = random.nextInt(90) - 45;
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vec3d newMotionVector = motionVector.rotateYaw(angleRadians);

		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());
	}

}
