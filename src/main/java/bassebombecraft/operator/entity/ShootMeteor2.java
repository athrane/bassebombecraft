package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots a meteor
 * at target entity.
 */
public class ShootMeteor2 implements Operator2 {

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker function to get invoker entity.
	 * @param fnGetTarget  function to get target entity.
	 */
	public ShootMeteor2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, LivingEntity> fnGetTarget) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetTarget = fnGetTarget;
	}

	@Override
	public Ports run(Ports ports) {
		Random random = getBassebombeCraft().getRandom();

		// get entities
		LivingEntity invoker = fnGetInvoker.apply(ports);
		LivingEntity target = fnGetTarget.apply(ports);

		// get world
		World world = ports.getWorld();

		// calculate position
		double posX = target.getPosX() + (random.nextInt(10) - 5);
		double posY = invoker.getPosY() + 20 + (random.nextInt(10) - 5);
		double posZ = target.getPosZ() + (random.nextInt(10) - 5);

		// calculate acceleration
		double d0 = target.getPosX() - posX;
		double d1 = target.getBoundingBox().minY + (double) (target.getHeight() / 3.0F) - posY;
		double d2 = target.getPosZ() - posZ;

		// spawn projectile
		FireballEntity projectile = new FireballEntity(world, invoker, d0, d1, d2);
		projectile.setPosition(posX, posY, posZ);
		world.addEntity(projectile);

		return ports;
	}

}
