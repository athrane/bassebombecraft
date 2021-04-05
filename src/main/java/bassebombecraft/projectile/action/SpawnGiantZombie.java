package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a giant
 * zombie.
 */
public class SpawnGiantZombie implements ProjectileAction {

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		try {
			Entity shooter = projectile.getShooter();
			GiantEntity entity = EntityType.GIANT.create(world);
			entity.setLocationAndAngles(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(),
					projectile.rotationYaw, projectile.rotationPitch);

			// if shooter is a living entity then add entity to shooters team
			if (isTypeLivingEntity(shooter)) {
				getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
			}

			// if shooter is a living entity then configure AI
			if (isTypeLivingEntity(shooter)) {
				clearAllAiGoals(entity);
				buildCharmedMobAi(entity, (LivingEntity) shooter);				
			}
			
			// spawn
			world.addEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
