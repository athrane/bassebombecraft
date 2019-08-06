package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import bassebombecraft.event.entity.team.TeamRepository;
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

		LivingEntity owner = projectile.getThrower();
		IronGolemEntity entity = EntityType.IRON_GOLEM.create(world);
		entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
				projectile.rotationPitch);
		world.addEntity(entity);

		// add entity to team
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		teamRepository.add(owner, entity);

		// set AI
		clearAiTasks(entity);
		buildCharmedMobAi(entity, owner);
	}

}
