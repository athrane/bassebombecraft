package bassebombecraft.projectile.action;

import static bassebombecraft.entity.EntityUtils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
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
		EnderDragonEntity entity = EntityType.ENDER_DRAGON.create(world);
		entity.setLocationAndAngles(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(), projectile.rotationYaw,
				projectile.rotationPitch);

		// get shooter
		Entity shooter = projectile.getShooter();

		// set phase 		
		entity.getPhaseManager().setPhase(PhaseType.STRAFE_PLAYER);
		
		// if shooter is a living entity then set it as target		
		if(isTypeLivingEntity(shooter)) {

			// type cast and set
			LivingEntity livingEntity = (LivingEntity) shooter;
			entity.getPhaseManager().getPhase(PhaseType.STRAFE_PLAYER).setTarget(livingEntity);			
		}

		// spawn
		world.addEntity(entity);
	}

}
