package bassebombecraft.operator.projectile;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.projectile.egg.OperatorEggProjectile2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots egg
 * projectile(s) from the invoker position.
 * 
 * The projectile executes an operator on impact. The operator implements the
 * {@linkplain Operator2} interface.
 */
public class ShootOperatorEggProjectile2 extends GenericShootProjectile2 {

	/**
	 * Projectile inaccuracy.
	 */
	static final float PROJECTILE_INACCURACY = 1.0F;

	/**
	 * Projectile force.
	 */
	static final float PROJECTILE_FORCE = 15F;

	/**
	 * Operator to execute on impact.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker     function to get invoker entity.
	 * @param fnGetOrientation function to get orientation vectors.
	 * @param bcSetProjectiles function to set projectiles.
	 * @param operator         operator which is executed on impact.
	 */
	public ShootOperatorEggProjectile2(Function<Ports, LivingEntity> fnGetInvoker,
			Function<Ports, Vec3d[]> fnGetOrientation, BiConsumer<Ports, Entity[]> bcSetProjectiles,
			Operator2 operator) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetOrientation = fnGetOrientation;
		this.bcSetProjectiles = bcSetProjectiles;
		this.operator = operator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 * 
	 * Instance sets created projectiles as entity array #1 in the ports.
	 * 
	 * @param operator operator which is executed on impact.
	 */
	public ShootOperatorEggProjectile2(Operator2 operator) {
		this(getFnGetLivingEntity1(), getFnGetVectors1(), getBcSetEntities1(), operator);
	}

	@Override
	Entity createProjectile(LivingEntity invoker, Vec3d orientation) {
		OperatorEggProjectile2 projectile = new OperatorEggProjectile2(invoker, operator);
		projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
		float velocity = PROJECTILE_FORCE * (float) orientation.length();
		projectile.shoot(orientation.getX(), orientation.getY(), orientation.getZ(), velocity, PROJECTILE_INACCURACY);
		return projectile;
	}

}
