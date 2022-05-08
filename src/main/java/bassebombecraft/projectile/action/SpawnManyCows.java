package bassebombecraft.projectile.action;

import static bassebombecraft.entity.EntityUtils.setRandomSpawnPosition;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns many cows.
 */
public class SpawnManyCows implements ProjectileAction {

	static final int NUMBER_COWS = 20;
	static final float PITCH = 0.0F;
	static final int GROWN_AGE = 1000;
	static final int SPAWN_SIZE = 1;
	
	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult result) {
		for (int i = 0; i < NUMBER_COWS; i++) {
			
			// create cow
			Cow entity = EntityType.COW.create(world);
			entity.setAge(GROWN_AGE);
			
			// calculate random spawn position
			setRandomSpawnPosition(projectile.blockPosition(), projectile.getYRot(), SPAWN_SIZE, entity);

			world.addFreshEntity(entity);
		}
	}

}
