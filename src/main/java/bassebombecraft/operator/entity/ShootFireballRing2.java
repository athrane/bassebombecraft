package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.shootSmallFireballRingFireballs;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots a small
 * fireball.
 */
public class ShootFireballRing2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = ShootFireballRing2.class.getSimpleName();

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.1D;

	/**
	 * Initial vector.
	 */
	static final Vec3d INITIAL_VECTOR = new Vec3d(1, 0, 0).scale(ACCELERATION_MODIFIER);

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Number of fireballs in ring.
	 */
	int number;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 */
	public ShootFireballRing2(Function<Ports, LivingEntity> fnGetInvoker) {
		this.fnGetInvoker = fnGetInvoker;

		number = shootSmallFireballRingFireballs.get();
	}

	@Override
	public Ports run(Ports ports) {
		int displacement = 360 / number;

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// get world
		World world = ports.getWorld();

		for (int index = 0; index < number; index++) {

			// calculate acceleration
			double yaw = (index * displacement) * 0.017453292F;
			Vec3d accelerationVector = INITIAL_VECTOR.rotateYaw((float) yaw);

			// create and spawn projectile
			SmallFireballEntity projectile = EntityType.SMALL_FIREBALL.create(world);
			projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
			projectile.setMotion(accelerationVector);

			// add acceleration to avoid glitches
			projectile.accelerationX = accelerationVector.x;
			projectile.accelerationY = accelerationVector.y;
			projectile.accelerationZ = accelerationVector.z;

			// spawn
			world.addEntity(projectile);
		}

		return ports;
	}

}
