package bassebombecraft.projectile.action;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns many cows.
 */
public class SpawnManyCows implements ProjectileAction {

	static final int NUMBER_COWS = 20;
	static final float PITCH = 0.0F;
	static final int GROWN_AGE = 1000;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < NUMBER_COWS; i++) {
			CowEntity entity = EntityType.COW.create(world);
			entity.setGrowingAge(GROWN_AGE);
			entity.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw,
					PITCH);
			world.addEntity(entity);
		}
	}

}
