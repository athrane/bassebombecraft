package bassebombecraft.operator.entity;

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
public class ShootFireball2 implements Operator2 {

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
	public ShootFireball2(Function<Ports, LivingEntity> fnGetInvoker) {
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
		SmallFireballEntity projectile = EntityType.SMALL_FIREBALL.create(world);
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
