package bassebombecraft.operator.projectile.path;

import java.util.Random;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.entity.raytraceresult.TeleportInvoker2;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which computes random path for the projectile.
 * 
 * The path is computed and updates the project motion vector.
 * 
 * The path is computed by a random rotation of the yaw (y-axis). 
 * 
 * The computed motion vector is stored with the same length as the original motion vector
 * in order to only change the direction be not the speed of the projectile.
 */
public class RandomProjectilePath implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = TeleportInvoker2.class.getSimpleName();

	@Override
	public Ports run(Ports ports) {
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();

		// get projectile
		Entity projectile = ports.getEntity1();

		// get motion vector
		Vec3d motionVector = projectile.getMotion();

		// rotate
		float angleDegrees = random.nextInt(90) - 45;
		float angleRadians = (float) Math.toRadians(angleDegrees);
		Vec3d newMotionVector = motionVector.rotateYaw(angleRadians);
		
		// update motion
		projectile.setMotion(newMotionVector.getX(), newMotionVector.getY(), newMotionVector.getZ());

		return ports;
	}

}
