package bassebombecraft.projectile.action;

import static bassebombecraft.entity.EntityUtils.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a guardian
 * dragon.
 */
public class SpawnDragon implements ProjectileAction {

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {
		EnderDragon entity = EntityType.ENDER_DRAGON.create(world);
		entity.moveTo(projectile.getX(), projectile.getY(), projectile.getZ(), projectile.getYRot(),
				projectile.getXRot());

		// get shooter
		Entity shooter = projectile.getOwner();

		// set phase 		
		entity.getPhaseManager().setPhase(EnderDragonPhase.STRAFE_PLAYER);
		
		// if shooter is a living entity then set it as target		
		if(isTypeLivingEntity(shooter)) {

			// type cast and set
			LivingEntity livingEntity = (LivingEntity) shooter;
			entity.getPhaseManager().getPhase(EnderDragonPhase.STRAFE_PLAYER).setTarget(livingEntity);			
		}

		// spawn
		world.addFreshEntity(entity);
	}

}
