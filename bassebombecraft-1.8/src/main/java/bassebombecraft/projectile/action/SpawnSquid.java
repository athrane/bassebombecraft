package bassebombecraft.projectile.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a squid
 * which attacks the nearest mob.
 * 
 * If a block is hit then a squid is spawned where the projectile hit.
 */
public class SpawnSquid implements ProjectileAction {

	private static final int Y_SPAWN_OFFSET = 2;
	private static final float PITCH = 0.0F;

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {
		Vec3d posVec = null;

		// spawn a cobweb if no entity was hit
		if (movObjPos.entityHit == null) {
			BlockPos spawnPosition = calculatePosition(world, movObjPos);
			posVec = new Vec3d(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
		} else {

			// get entity position
			Entity entity = movObjPos.entityHit;
			posVec = entity.getPositionVector();
			posVec = posVec.addVector(0, entity.height, 0);
		}

		// spawn squid
		EntitySquid squid = new EntitySquid(world);
		double lx = posVec.x;
		double ly = posVec.y + Y_SPAWN_OFFSET;
		double lz = posVec.z;
		float yaw = projectile.rotationYaw;
		squid.setLocationAndAngles(lx, ly, lz, yaw, PITCH);
		world.spawnEntity(squid);
	}

	/**
	 * Calculate position.
	 * 
	 * @param world
	 *            world object.
	 * 
	 * @param movObjPos
	 *            hit object.
	 * 
	 * @return position where block should be spawned.
	 */
	BlockPos calculatePosition(World world, RayTraceResult movObjPos) {
		switch (movObjPos.sideHit) {

		case UP:
			return movObjPos.getBlockPos().up();

		case DOWN:
			return movObjPos.getBlockPos().down();

		case SOUTH:
			return movObjPos.getBlockPos().south();

		case NORTH:
			return movObjPos.getBlockPos().north();

		case EAST:
			return movObjPos.getBlockPos().east();

		case WEST:
			return movObjPos.getBlockPos().west();

		default:
			return movObjPos.getBlockPos().up();
		}
	}
}
