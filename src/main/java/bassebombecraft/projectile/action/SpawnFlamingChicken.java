package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
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
		try {
			ChickenEntity entity = EntityType.CHICKEN.create(world);
			entity.setGrowingAge(CHILD_AGE);
			entity.setLocationAndAngles(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ(),
					projectile.rotationYaw, PITCH);
			entity.setFire(3);

			// get owner
			LivingEntity owner = projectile.getThrower();

			// add entity to team
			getProxy().getTeamRepository(world).add(owner, entity);

			// set AI
			// NO-OP

			// spawn
			world.addEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
