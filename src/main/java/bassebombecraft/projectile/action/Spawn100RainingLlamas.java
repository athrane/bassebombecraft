package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns 100 raining
 * llamas.
 */
public class Spawn100RainingLlamas implements ProjectileAction {

	static final int SPAWN_SIZE = 20;
	static final int Y_SPAWN_OFFSET = 5;
	static final int Y_SPAWN_SIZE = 5;
	static final int NUMBER_LLAMAS = 100;
	static final float PITCH = 0.0F;
	static final int AGE = 0;

	@Override
	public void execute(ThrowableProjectile projectile, Level world, HitResult movObjPos) {
		for (int i = 0; i < NUMBER_LLAMAS; i++) {
			Llama entity = EntityType.LLAMA.create(world);
			entity.setAge(AGE);

			Random random = getBassebombeCraft().getRandom();
			int randomX = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);
			int randomY = random.nextInt(Y_SPAWN_SIZE) + (Y_SPAWN_OFFSET);
			int randomZ = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);

			double positionX = projectile.getX() + randomX;
			double positionY = projectile.getY() + randomY;
			double positionZ = projectile.getZ() + randomZ;

			entity.moveTo(positionX, positionY, positionZ, projectile.yRot, PITCH);

			// spawn
			world.addFreshEntity(entity);
		}
	}

}
