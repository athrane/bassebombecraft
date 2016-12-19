package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which charm the entity
 * which has been hit.
 */
public class CharmEntity implements ProjectileAction {

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// exit if no mob was hit
		if (movObjPos.entityHit == null)
			return;

		// skip if entity can't be charmed, i.e is a living entity
		if (!(movObjPos.entityHit instanceof EntityLiving))
			return;
		EntityLiving entityLiving = (EntityLiving) movObjPos.entityHit;

		// register mob as charmed
		getBassebombeCraft().getCharmedMobsRepository().add(entityLiving);
	}

}
