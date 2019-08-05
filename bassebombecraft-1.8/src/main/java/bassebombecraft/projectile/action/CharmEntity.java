package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;

import bassebombecraft.entity.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which charm the entity
 * which has been hit.
 */
public class CharmEntity implements ProjectileAction {

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if no entity was hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// exit if entity can't be charmed, i.e isn't a living entity
		if (!EntityUtils.isLivingEntity(entity))
			return;

		// type cast
		LivingEntity entityLiving = (LivingEntity) entity;

		// get thrower
		LivingEntity thrower = projectile.getThrower();

		// register mob as charmed
		getBassebombeCraft().getCharmedMobsRepository().add(entityLiving, thrower);
	}

}
