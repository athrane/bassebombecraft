package bassebombecraft.projectile.action;

import java.util.Random;

import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.projectile.EntityThrowable;
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
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {
		for (int i = 0; i < NUMBER_LLAMAS; i++) {
			EntityLlama entity = new EntityLlama(world);
			entity.setGrowingAge(AGE);

			Random random = entity.getRNG();

			int randomX = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);
			int randomY = random.nextInt(Y_SPAWN_SIZE) + (Y_SPAWN_OFFSET);			
			int randomZ = random.nextInt(SPAWN_SIZE) - (SPAWN_SIZE / 2);

			double positionX = projectile.posX + randomX;
			double positionY = projectile.posY + randomY;
			double positionZ = projectile.posZ + randomZ;

			entity.setLocationAndAngles(positionX, positionY, positionZ, projectile.rotationYaw, PITCH);
			
			world.spawnEntity(entity);
		}
	}

}
