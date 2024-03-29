package bassebombecraft.operator.entity.raytraceresult;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Implementation of the {@linkplain Operator2} interface which charms the
 * entity which has been hit.
 */
public class Charm2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Charm2.class.getSimpleName();

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker      function to get invoker entity.
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public Charm2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, HitResult> fnGetRayTraceResult) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetRayTraceResult = fnGetRayTraceResult;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with living entity #1 as invoker from ports.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 */
	public Charm2() {
		this(getFnGetLivingEntity1(), getFnGetRayTraceResult1());
	}

	@Override
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		HitResult result = applyV(fnGetRayTraceResult, ports);

		// exit if no entity was hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityHitResult) result).getEntity();

		// skip if entity can't be charmed, i.e. is a mob entity
		if (!isTypeMobEntity(entity))
			return;

		// type cast
		Mob mobEntity = (Mob) entity;

		// register mob as charmed
		getProxy().getServerCharmedMobsRepository().add(mobEntity, invoker);
	}

}
