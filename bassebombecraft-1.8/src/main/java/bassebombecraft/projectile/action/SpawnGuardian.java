package bassebombecraft.projectile.action;

import static bassebombecraft.entity.ai.AiUtils.buildCharmedMobAi;
import static bassebombecraft.entity.ai.AiUtils.clearAiTasks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a guardian
 * golem.
 */
public class SpawnGuardian implements ProjectileAction {

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		EntityLivingBase owner = projectile.getThrower();				
		EntityIronGolem entity= new EntityIronGolem(world);
		entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw, projectile.rotationPitch);
		world.spawnEntity(entity);
		clearAiTasks(entity);
		buildCharmedMobAi(entity, owner);		
	}

}
