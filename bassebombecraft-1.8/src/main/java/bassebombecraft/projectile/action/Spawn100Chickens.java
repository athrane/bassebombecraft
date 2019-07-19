package bassebombecraft.projectile.action;

import java.util.Random;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {		
		for(int i=0; i< NUMBER_CHICKENS;i++) {
			EntityChicken entity = new EntityChicken(world);
			entity.setGrowingAge(CHILD_AGE);
			
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
