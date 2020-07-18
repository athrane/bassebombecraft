package bassebombecraft.operator.entity;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.operator.DefaultPorts.getFnGetLivingEntity1;
import static bassebombecraft.operator.DefaultPorts.getFnGetRayTraceResult1;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain Operator2} interface which teleports the
 * invoker entity to hit block / entity.
 */
public class TeleportInvoker2 implements Operator2 {

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get ray trace result.
	 */
	Function<Ports, RayTraceResult> fnGetRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param fnGetInvoker      function to get invoker entity.
	 * @param splRayTraceResult function to get ray trace result.
	 */
	public TeleportInvoker2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, RayTraceResult> fnGetRayTraceResult) {
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
	public TeleportInvoker2() {
		this(getFnGetLivingEntity1(), getFnGetRayTraceResult1());
	}

	@Override
	public Ports run(Ports ports) {

		// get invoker
		LivingEntity invoker = fnGetInvoker.apply(ports);

		// get ray trace result
		RayTraceResult result = fnGetRayTraceResult.apply(ports);

		// exit if nothing was hit
		if (isNothingHit(result))
			return ports;

		// declare
		BlockPos teleportPosition = null;

		// teleport to hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result;
			if (!isTypeEntityRayTraceResult(result))
				return ports;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position
			teleportPosition = entity.getPosition();
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return ports;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get block position
			teleportPosition = calculatePosition(blockResult);
		}

		// teleport
		invoker.setPositionAndUpdate(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());

		return ports;
	}

}
