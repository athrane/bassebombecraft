package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAllAiGoals;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a guardian
 * golem.
 */
public class SpawnGuardian implements ProjectileAction {

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		try {
			LivingEntity owner = projectile.getThrower();
			IronGolemEntity entity = EntityType.IRON_GOLEM.create(world);
			entity.setLocationAndAngles(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(),
					projectile.rotationYaw, projectile.rotationPitch);

			// add entity to team
			getProxy().getTeamRepository(world).add(owner, entity);

			// set AI
			clearAllAiGoals(entity);
			buildCharmedMobAi(entity, owner);

			world.addEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
