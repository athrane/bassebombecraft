package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a guardian
 * golem.
 */
public class SpawnGuardian implements ProjectileAction {

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult result) {
		try {
			Entity shooter = projectile.getOwner();
			IronGolem entity = EntityType.IRON_GOLEM.create(world);
			entity.moveTo(projectile.getX(), projectile.getY(), projectile.getZ(),
					projectile.yRot, projectile.xRot);

			// if shooter is a living entity then add entity to shooters team
			if (isTypeLivingEntity(shooter)) {
				getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
			}

			// if shooter is a living entity then configure AI
			if (isTypeLivingEntity(shooter)) {
				clearAllAiGoals(entity);
				buildCharmedMobAi(entity, (LivingEntity) shooter);
			}
			
			world.addFreshEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
