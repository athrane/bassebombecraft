package bassebombecraft.operator.conditional;

import static bassebombecraft.entity.EntityUtils.*;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.operator.Operators2.applyV;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Implementation of the {@linkplain Operator2} interface which updates the
 * result port as successful if the ray trace result contains a hit entity AND
 * the entity is a {@linkplain LivingEntity}.
 */
public class IsLivingEntityHitInRaytraceResult2 implements Operator2 {

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, HitResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public IsLivingEntityHitInRaytraceResult2(Function<Ports, HitResult> fnGetRayTraceResult) {
		this.fnGetRayTraceResult = fnGetRayTraceResult;
	}

	/**
	 * Constructor.
	 * 
	 * Instance is configured with ray tracing result #1 from ports.
	 */
	public IsLivingEntityHitInRaytraceResult2() {
		this(getFnGetRayTraceResult1());
	}

	@Override
	public void run(Ports ports) {
		HitResult result = applyV(fnGetRayTraceResult, ports);

		// exit if no entity was hit
		if (!isEntityHit(result)) {
			ports.setResultAsFailed();
			return;
		}

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result)) {
			ports.setResultAsFailed();
			return;
		}

		// get entity
		Entity entity = ((EntityHitResult) result).getEntity();
		
		// skip if entity isn't a living entity
		if (!isTypeLivingEntity(entity)) {
			ports.setResultAsFailed();
			return;			
		}
		
		ports.setResultAsSucces();
		return;
	}
}
