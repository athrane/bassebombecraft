package bassebombecraft.projectile.action;

import java.util.Random;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns 100
 * chickens.
 */
public class Spawn100Chickens implements ProjectileAction {

	static final int NUMBER_CHICKENS = 100;
	static final float PITCH = 0.0F;
	static final int CHILD_AGE = -24000;
	static final int SPAWN_SIZE = 20;
	static final int Y_SPAWN_OFFSET = 5;
	static final int Y_SPAWN_SIZE = 5;

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {
		for (int i = 0; i < NUMBER_CHICKENS; i++) {
			Chicken entity = EntityType.CHICKEN.create(world);
			entity.setAge(CHILD_AGE);

			Random random = entity.getRandom();
			int randomX = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);
			int randomY = random.nextInt(Y_SPAWN_SIZE) + (Y_SPAWN_OFFSET);
			int randomZ = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);

			double positionX = projectile.getX() + randomX;
			double positionY = projectile.getY() + randomY;
			double positionZ = projectile.getZ() + randomZ;

			entity.moveTo(positionX, positionY, positionZ, projectile.yRot, PITCH);
			world.addFreshEntity(entity);
		}
	}

}
