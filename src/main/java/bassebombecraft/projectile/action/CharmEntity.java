package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isTypeMobEntity;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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

		// skip if entity can't be charmed, i.e. is a mob entity
		if (!isTypeMobEntity(entity))
			return;

		// type cast
		MobEntity mobEntity = (MobEntity) entity;

		// get thrower
		LivingEntity thrower = projectile.getThrower();

		// register mob as charmed
		getBassebombeCraft().getCharmedMobsRepository().add(mobEntity, thrower);
	}

}
