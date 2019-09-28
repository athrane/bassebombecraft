package bassebombecraft.projectile.action;

import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;

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
	static final int SPAWN_SIZE = 1;
	
	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		for (int i = 0; i < NUMBER_COWS; i++) {
			
			// create cow
			CowEntity entity = EntityType.COW.create(world);
			entity.setGrowingAge(GROWN_AGE);
			
			// calculate random spawn position
			setRandomSpawnPosition(projectile.getPosition(), projectile.rotationYaw, SPAWN_SIZE, entity);

			world.addEntity(entity);
		}
	}

}
