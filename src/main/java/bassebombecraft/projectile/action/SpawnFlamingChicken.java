package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;

import static bassebombecraft.entity.EntityUtils.*;
import net.minecraft.entity.Entity;
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

			// get shooter
			Entity shooter = projectile.getShooter();

			// if shooter is a living entity then add entity to shooters team
			if (isTypeLivingEntity(shooter)) {
				getProxy().getServerTeamRepository().add((LivingEntity) shooter, entity);
			}

			// set AI
			// NO-OP

			// spawn
			world.addEntity(entity);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

}
