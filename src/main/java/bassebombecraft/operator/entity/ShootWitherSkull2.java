package bassebombecraft.operator.entity;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots a
 * projectile.
 */
public class ShootWitherSkull2 implements Operator2 {

	/**
	 * Acceleration modifier.
	 */
	static final double ACCELERATION_MODIFIER = 0.5D;

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 */
	public ShootWitherSkull2(Function<Ports, LivingEntity> fnGetInvoker) {
		this.fnGetInvoker = fnGetInvoker;
	}

	@Override
	public Ports run(Ports ports) {

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// get world
		World world = ports.getWorld();

		// calculate acceleration
		Vec3d accelerationVector = invoker.getLook(1).scale(ACCELERATION_MODIFIER);

		// create and spawn projectile
		WitherSkullEntity projectile = EntityType.WITHER_SKULL.create(world);
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		projectile.setMotion(accelerationVector);

		// add acceleration to avoid glitches
		projectile.accelerationX = accelerationVector.x;
		projectile.accelerationY = accelerationVector.y;
		projectile.accelerationZ = accelerationVector.z;

		world.addEntity(projectile);

		return ports;
	}

}
