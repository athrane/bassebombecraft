package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

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
			LivingEntity owner = projectile.getThrower();
			GiantEntity entity = EntityType.GIANT.create(world);
			entity.setLocationAndAngles(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(),
					projectile.rotationYaw, projectile.rotationPitch);

			// add entity to team
			getProxy().getTeamRepository(world).add(owner, entity);

			// set AI
			clearAllAiGoals(entity);
			buildCharmedMobAi(entity, owner);

			// spawn
			world.addEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
