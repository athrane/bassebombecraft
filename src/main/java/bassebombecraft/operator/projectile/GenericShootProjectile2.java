package bassebombecraft.operator.projectile;

import static bassebombecraft.operator.DefaultPorts.getBcSetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetVectors1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.BiConsumer;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Generic implementation of the {@linkplain Operator2} interface which shoots a
 * projectile(s) from the invoker position.
 */
abstract public class GenericShootProjectile2 implements Operator2 {

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get orientation vectors.
	 */
	Function<Ports, Vec3d[]> fnGetOrientation;

	/**
	 * Function to set projectiles.
	 */
	BiConsumer<Ports, Entity[]> bcSetProjectiles;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker     function to get invoker entity.
	 * @param fnGetOrientation function to get orientation vectors.
	 * @param bcSetProjectiles function to set projectiles.
	 */
	public GenericShootProjectile2(Function<Ports, LivingEntity> fnGetInvoker,
			Function<Ports, Vec3d[]> fnGetOrientation, BiConsumer<Ports, Entity[]> bcSetProjectiles) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetOrientation = fnGetOrientation;
		this.bcSetProjectiles = bcSetProjectiles;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with vector array #1 as orientation vector from ports.
	 * 
	 * Instance sets created projecties as entity array #1 in the ports.
	 */
	public GenericShootProjectile2() {
		this(getFnGetLivingEntity1(), getFnGetVectors1(), getBcSetEntities1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		Vec3d[] vectors = applyV(fnGetOrientation, ports);

		// get world
		World world = invoker.getEntityWorld();

		// create new array for storage of projectiles
		Entity[] projectiles = new Entity[vectors.length];

		// create index for storage of projectiles
		int index = 0;

		// create projectiles
		for (Vec3d orientation : vectors) {

			// create and spawn projectile
			Entity projectile = createProjectile(invoker, orientation);

			// spawn projectile
			world.addEntity(projectile);

			// store projectile
			projectiles[index] = projectile;

			index++;
		}

		// store projectiles
		bcSetProjectiles.accept(ports, projectiles);
	}

	/**
	 * Create and configure projectile with motion and invoker.
	 * 
	 * @param invoker     entity shooting the projectile.
	 * @param orientation orientation vector for the projectile.
	 * 
	 * @return created projectile
	 */
	abstract Entity createProjectile(LivingEntity invoker, Vec3d orientation);

}
