package bassebombecraft.projectile.action;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a guardian
 * dragon.
 */
public class SpawnDragon implements ProjectileAction {

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {

		EntityDragon entity = new EntityDragon(world);
		entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
				projectile.rotationPitch);
		entity.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
		world.spawnEntity(entity);
	}

}
