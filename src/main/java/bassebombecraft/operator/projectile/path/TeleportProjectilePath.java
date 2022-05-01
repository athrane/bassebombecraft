package bassebombecraft.operator.projectile.path;

import static bassebombecraft.geom.GeometryUtils.oscillateFloat;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntity1;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.util.Mth.lerp;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Implementation of the {@linkplain Operator2} interface which teleport the
 * projectile .
 * 
 * The path is computed and updates the projectile motion vector.
 * 
 * The path is computed by a random rotation of the yaw (y-axis).
 * 
 * The computed motion vector is stored with the same length as the original
 * motion vector in order to only change the direction be not the speed of the
 * projectile.
 */
public class TeleportProjectilePath implements Operator2 {

	/**
	 * Rotation angle.
	 */
	static final int ANGLE = 45;

	/**
	 * Operator identifier.
	 */
	public static final String NAME = TeleportProjectilePath.class.getSimpleName();

	/**
	 * Function to get projectile entity.
	 */
	Function<Ports, Entity> fnGetProjectile;

	/**
	 * Constructor.
	 * 
	 * @param fnGetProjectile function to get projectile entity.
	 */
	public TeleportProjectilePath(Function<Ports, Entity> fnGetProjectile) {
		this.fnGetProjectile = fnGetProjectile;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with entity #1 as projectile from ports.
	 */
	public TeleportProjectilePath() {
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

		// rotate the projectile
		float oscValue = oscillateFloat(0, 1);		
		double angleDegrees = calculateAngle(x, oscValue);
		
		// exit if no rotation
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		if (random.nextInt(5) < 4 )
			return;

		// rotate
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vec3 newMotionVector = motionVector.yRot(angleRadians);

		// update motion
		projectile.setDeltaMovement(newMotionVector.x(), newMotionVector.y(), newMotionVector.z());

		// calculate position delta vector
		float length = lerp(oscValue, 1, 3);
		Vec3 deltaVector = newMotionVector.normalize().scale(length);
		Vec3 newPosVector = projectile.position().add(deltaVector);

		// update position
		projectile.teleportTo(newPosVector.x(), newPosVector.y(), newPosVector.z());
	}

	/**
	 * Calculate angle for next rotation.
	 * 
	 * @param x parameter for the next function value.
	 * @param oscValue oscillating value.
	 * 
	 * @return angle for next rotation.
	 */
	double calculateAngle(long x, float oscValue) {

		// delay rotation to after n ticks
		if (x < 5)
			return 0;

		// calc rotation
		return lerp(oscValue, -ANGLE, ANGLE);
	}

}
