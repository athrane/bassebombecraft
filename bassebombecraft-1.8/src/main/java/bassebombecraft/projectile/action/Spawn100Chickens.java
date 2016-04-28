package bassebombecraft.projectile.action;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns 100
 * chickens.
 */
public class Spawn100Chickens implements ProjectileAction {

	static final int NUMBER_CHICKENS = 100;
	static final float PITCH = 0.0F;
	static final int CHILD_AGE = -24000;

	@Override
	public void execute(EntityThrowable projectile, World world, MovingObjectPosition movObjPos) {		
		for(int i=0; i< NUMBER_CHICKENS;i++) {
			EntityChicken chicken = new EntityChicken(world);
			chicken.setGrowingAge(CHILD_AGE);
			chicken.setLocationAndAngles(projectile.posX, projectile.posY, projectile.posZ, projectile.rotationYaw, PITCH);
			world.spawnEntityInWorld(chicken);			
		}
	}

}
