package bassebombecraft.operator.entity;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain Operator} interface which teleports the
 * entity to hit block / entity.
 */
public class Teleport implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Teleport.class.getSimpleName();

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * RayTraceResult supplier.
	 */
	Supplier<RayTraceResult> splRayTraceResult;

	/**
	 * Constructor.
	 * 
	 * @param splEntity         invoker entity supplier.
	 * @param splRayTraceResult projectile ray trace result.
	 */
	public Teleport(Supplier<LivingEntity> splEntity, Supplier<RayTraceResult> splRayTraceResult) {
		this.splEntity = splEntity;
		this.splRayTraceResult = splRayTraceResult;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// get ray trace result
		RayTraceResult result = splRayTraceResult.get();

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare
		BlockPos teleportPosition = null;

		// teleport to hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result;
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position
			teleportPosition = entity.getPosition();
		}

		// teleport to hit block
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get block position
			teleportPosition = calculatePosition(blockResult);
		}

		// teleport
		livingEntity.setPositionAndUpdate(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());
	}

}
