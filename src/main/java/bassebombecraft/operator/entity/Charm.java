package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;

/**
 * Implementation of the {@linkplain Operator} interface which charm the entity
 * which has been hit.
 */
public class Charm implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = Charm.class.getSimpleName();

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
	public Charm(Supplier<LivingEntity> splEntity, Supplier<RayTraceResult> splRayTraceResult) {
		this.splEntity = splEntity;
		this.splRayTraceResult = splRayTraceResult;
	}

	@Override
	public void run() {

		try {

			// get entity
			LivingEntity livingEntity = splEntity.get();

			// get ray trace result
			RayTraceResult result = splRayTraceResult.get();

			// exit if no entity was hit
			if (!isEntityHit(result))
				return;

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// skip if entity can't be charmed, i.e. is a mob entity
			if (!isTypeMobEntity(entity))
				return;

			// type cast
			MobEntity mobEntity = (MobEntity) entity;

			// register mob as charmed
			getProxy().getCharmedMobsRepository(mobEntity.getEntityWorld()).add(mobEntity, livingEntity);
			
		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
