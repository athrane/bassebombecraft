package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getInstance;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootOperatorEggProjectile2;
import bassebombecraft.operator.projectile.formation.RandomSingleProjectileFormation2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain Operator2} interface which teleports a hit
 * mob to a random location.
 */
public class TeleportMob2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = TeleportMob2.class.getSimpleName();

	/**
	 * Create operators.
	 */
	static Supplier<Operator2> splOp = () -> {
		Operator2 projectileLogicOp = new TeleportInvoker2();
		Operator2 formationOp = new RandomSingleProjectileFormation2();
		Operator2 projectileOp = new ShootOperatorEggProjectile2(projectileLogicOp);
		return new Sequence2(formationOp, projectileOp);
	};

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public TeleportMob2(Function<Ports, RayTraceResult> fnGetRayTraceResult) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 */
	public TeleportMob2() {
		this(getFnGetRayTraceResult1());
	}

	@Override
	public Ports run(Ports ports) {

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);
		if (result == null)
			return ports;

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// exit if no entity was hit
		if (!isEntityHit(result))
			return ports;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return ports;

		// get hit entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// exit if entity isn't a living entity
		if (!EntityUtils.isTypeLivingEntity(entity))
			return ports;

		// type cast
		LivingEntity livingEntity = (LivingEntity) entity;

		// create ports
		Ports ports2 = getInstance();
		ports2.setLivingEntity1(livingEntity);

		// execute
		Operators2.run(ports2, splOp.get());

		return ports;
	}

}
