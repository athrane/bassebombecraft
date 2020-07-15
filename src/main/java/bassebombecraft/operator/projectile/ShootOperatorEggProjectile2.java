package bassebombecraft.operator.projectile;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots egg
 * projectile(s) from the invoker position.
 * 
 * The projectile executes an operator on impact. The operator implements the
 * {@linkplain Operator2} interface.
 */
public class ShootOperatorEggProjectile2 implements Operator2 {

	/**
	 * Projectile inaccuracy.
	 */
	static final float PROJECTILE_INACCURACY = 1.0F;

	/**
	 * Projectile force.
	 */
	static final float PROJECTILE_FORCE = 15F;

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get orientation vectors.
	 */
	Function<Ports, Vec3d[]> fnGetOrientation;

	/**
	 * Operator to execute on impact.
	 */
	Operator2 operator;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker     function to get invoker entity.
	 * @param fnGetOrientation function to get orientation vectors.
	 * @param operator         operator which is executed on impact.
	 */
	public ShootOperatorEggProjectile2(Function<Ports, LivingEntity> fnGetInvoker,
			Function<Ports, Vec3d[]> fnGetOrientation, Operator2 operator) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetOrientation = fnGetOrientation;
		this.operator = operator;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 * 
	 * @param operator operator which is executed on impact.
	 */
	public ShootOperatorEggProjectile2(Operator2 operator) {
		this(getFnGetLivingEntity1(), getFnGetVectors1(), operator);
	}

	@Override
	public Ports run(Ports ports) {

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// get orientation vectors
		Vec3d[] vectors = fnGetOrientation.apply(ports);

		// get world
		World world = ports.getWorld();

		for (Vec3d orientation : vectors) {

			// create and spawn projectile
			OperatorEggProjectile2 projectile = new OperatorEggProjectile2(invoker, operator);
			projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
			float velocity = PROJECTILE_FORCE * (float) orientation.length();
			projectile.shoot(orientation.getX(), orientation.getY(), orientation.getZ(), velocity,
					PROJECTILE_INACCURACY);

			world.addEntity(projectile);
		}

		return ports;
	}

}
