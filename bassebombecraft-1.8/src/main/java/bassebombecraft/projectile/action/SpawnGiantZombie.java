package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a giant zombie.
 */
public class SpawnGiantZombie implements ProjectileAction {

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		EntityLivingBase owner = projectile.getThrower();

		EntityGiantZombie entity = new EntityGiantZombie(world);
		entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
				projectile.rotationPitch);
		world.spawnEntity(entity);

		// add entity to team
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		teamRepository.add(owner, entity);

		// set AI
		clearAiTasks(entity);
		buildCharmedMobAi(entity, owner);
	}

}