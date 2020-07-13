package bassebombecraft.operator.projectile;

import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which shoots arrow
 * projectile(s) from the invoker position.
 */
public class ArrowProjectile2 implements Operator2 {

	static final float INACCURACY = 1.0F;

	static final float ARROW_FORCE = 15F;
	
	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get orientation vectors.
	 */
	Function<Ports, Vec3d[]> fnGetOrientation;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker      function to get invoker entity.
	 * @param fnGetOrientation function to get orientation vectors.
	 */
	public ArrowProjectile2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, Vec3d[]> fnGetOrientation) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetOrientation = fnGetOrientation;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from
	 * ports.
	 */
	public ArrowProjectile2() {
		this(getFnGetLivingEntity1(), getFnGetVectors1());
	}

	@Override
	public Ports run(Ports ports) {

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// get acceleration vectors
		Vec3d[] vectors = fnGetOrientation.apply(ports);

		// get world
		World world = ports.getWorld();

		for (Vec3d orientation : vectors) {

			// create and spawn projectile
			ArrowEntity projectile = EntityType.ARROW.create(world);
			projectile.setPosition(invoker.getPosX(), invoker.getPosY() + invoker.getEyeHeight(), invoker.getPosZ());
			float velocity = ARROW_FORCE * (float) orientation.length();
			projectile.shoot(orientation.getX(), orientation.getY(), orientation.getZ(), velocity, INACCURACY);

			world.addEntity(projectile);
		}

		return ports;
	}

}
