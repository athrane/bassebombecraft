package bassebombecraft.projectile.action;

import bassebombecraft.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which sets the spawn
 * point.
 * 
 * If a block is hit then the spawn point is set there . If a mob is hit the
 * spawn point will be set at the mobs position.
 */
public class SetSpawnPoint implements ProjectileAction {

	/**
	 * SEt forced spawn point.
	 */
	static final boolean FORCED = true;

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// exit if not a player
		LivingEntity thrower = projectile.getThrower();
		if (!PlayerUtils.isPlayerEntity(thrower))
			return;

		BlockPos spawnPosition = null;

		// spawn a temporary lava block if no entity was hit
		if (movObjPos.entityHit == null) {

			// calculate position
			spawnPosition = calculatePosition(world, movObjPos);

		} else {

			// spawn temporary ice block around the hit mob
			Entity entity = movObjPos.entityHit;

			// get position
			spawnPosition = entity.getPosition();
		}

		// type cast
		PlayerEntity player = (PlayerEntity) thrower;

		player.setSpawnPoint(spawnPosition, FORCED);
		return;
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
