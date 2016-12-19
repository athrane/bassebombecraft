package bassebombecraft.projectile.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a squid
 * which attacks the nearest mob.
 * 
 * If a block is hit then NO-OP.
 */
public class SpawnSquid implements ProjectileAction {

	private static final int Y_SPAWN_OFFSET = 2;
	private static final float PITCH = 0.0F;

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {
		
		// NO-OP if no entity was hit
		if (movObjPos.entityHit == null) {
			// NO-OP
			return;
		}
		
		// get entity position
		Entity entity = movObjPos.entityHit;

		Vec3d posVec = entity.getPositionVector();
		float height = entity.height;		
		EntitySquid squid = new EntitySquid(world);
		double lx = posVec.xCoord;
		double ly = posVec.yCoord + height +Y_SPAWN_OFFSET;
		double lz = posVec.zCoord;
		float yaw = projectile.rotationYaw;
		squid.setLocationAndAngles(lx, ly, lz, yaw, PITCH);
		entity.world.spawnEntity(projectile );
	}

}
