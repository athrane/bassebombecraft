package bassebombecraft.projectile.action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which teleports entity to
 * hit block / entity.
 */
public class TeleportEntity implements ProjectileAction {

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// teleport if no entity was hit
		if (movObjPos.entityHit == null) {
			BlockPos teleportPosition = calculatePosition(world, movObjPos);
			EntityLivingBase thrower = projectile.getThrower();
			thrower.setPositionAndUpdate(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());
			return;
		}

		// get entity position
		Entity entityHit = movObjPos.entityHit;
		BlockPos teleportPosition = entityHit.getPosition();
		EntityLivingBase thrower = projectile.getThrower();
		thrower.setPositionAndUpdate(teleportPosition.getX(), teleportPosition.getY(), teleportPosition.getZ());
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
