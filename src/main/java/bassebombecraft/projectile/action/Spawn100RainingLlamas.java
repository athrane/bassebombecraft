package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Random;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < NUMBER_LLAMAS; i++) {
			LlamaEntity entity = EntityType.LLAMA.create(world);
			entity.setGrowingAge(AGE);

			Random random = getBassebombeCraft().getRandom();
			int randomX = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);
			int randomY = random.nextInt(Y_SPAWN_SIZE) + (Y_SPAWN_OFFSET);
			int randomZ = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);

			double positionX = projectile.getPosX() + randomX;
			double positionY = projectile.getPosY() + randomY;
			double positionZ = projectile.getPosZ() + randomZ;

			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);

			// spawn
			world.addEntity(entity);
		}
	}

}
