package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import bassebombecraft.event.entity.team.TeamRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a flaming
 * chicken.
 */
public class SpawnFlamingChicken implements ProjectileAction {

	private static final float PITCH = 0.0F;
	private static final int CHILD_AGE = -24000;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		EntityChicken chicken = new EntityChicken(world);
		chicken.setGrowingAge(CHILD_AGE);
		chicken.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw, PITCH);
		chicken.setFire(3);
		
		// get owner
		LivingEntity commander = projectile.getThrower();
		
		// add entity to team
		TeamRepository teamRepository = getBassebombeCraft().getTeamRepository();
		teamRepository.add(commander, chicken);

		// set AI
		// NO-OP
		
		// spawn		
		world.spawnEntity(chicken);
	}

}
