package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetEntities1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.DefaultPorts.getInstance;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;
import java.util.function.Supplier;

import bassebombecraft.entity.EntityUtils;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Operators2;
import bassebombecraft.operator.Ports;
import bassebombecraft.operator.Sequence2;
import bassebombecraft.operator.projectile.ShootCircleProjectile2;
import bassebombecraft.operator.projectile.formation.RandomSingleProjectileFormation2;
import bassebombecraft.operator.projectile.modifier.TagProjectileWithProjectileModifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Implementation of the {@linkplain Operator2} interface which teleports a hit
 * mob to a random location.
 * 
 * The {@linkplain Ports} object used to invoke the operator isn't used to
 * invoke the embedded operators. An separate {@linkplain Ports} instance is
 * used to invoked the embedded operators.
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
		Operator2 formationOp = new RandomSingleProjectileFormation2();
		Operator2 projectileOp = new ShootCircleProjectile2();
		Operator2 modifierOp = new TagProjectileWithProjectileModifier(getFnGetEntities1(), p -> TeleportInvoker2.NAME);
		return new Sequence2(formationOp, projectileOp, modifierOp);
	};

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public TeleportMob2(Function<Ports, HitResult> fnGetRayTraceResult) {
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
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// exit if no entity was hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get hit entity
		Entity entity = ((EntityHitResult) result).getEntity();

		// exit if entity isn't a living entity
		if (!EntityUtils.isTypeLivingEntity(entity))
			return;

		// type cast
		LivingEntity livingEntity = (LivingEntity) entity;

		// create ports
		Ports ports2 = getInstance();
		ports2.setLivingEntity1(livingEntity);

		// execute
		Operators2.run(ports2, splOp.get());
	}

}
