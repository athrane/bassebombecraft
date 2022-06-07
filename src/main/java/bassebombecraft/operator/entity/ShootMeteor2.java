package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity2;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots a meteor
 * at target entity.
 */
public class ShootMeteor2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = ShootMeteor2.class.getSimpleName();

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

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with living entity #2 as target from ports.
	 */
	public ShootMeteor2() {
		this(getFnGetLivingEntity1(), getFnGetLivingEntity2());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		LivingEntity target = applyV(fnGetTarget, ports);

		// get random
		Random random = getBassebombeCraft().getRandom();

		// get world
		Level world = invoker.getCommandSenderWorld();

		// calculate position
		double posX = target.getX() + (random.nextInt(10) - 5);
		double posY = invoker.getY() + 20 + (random.nextInt(10) - 5);
		double posZ = target.getZ() + (random.nextInt(10) - 5);

		// calculate acceleration
		double d0 = target.getX() - posX;
		double d1 = target.getBoundingBox().minY + (double) (target.getBbHeight() / 3.0F) - posY;
		double d2 = target.getZ() - posZ;

		// spawn projectile
		AbstractHurtingProjectile projectile = EntityType.FIREBALL.create(invoker.getCommandSenderWorld());

		// add acceleration
		projectile.xPower = d0;
		projectile.yPower = d1;
		projectile.zPower = d2;

		// set position
		projectile.setPos(posX, posY, posZ);
		world.addFreshEntity(projectile);
	}

}
