package bassebombecraft.projectile.action;

import static bassebombecraft.ModConstants.LIGHTNING_NOT_EFFECT_ONLY;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a lightning
 * bolt a the hit mob.
 * 
 * If a block is hit then an lightning bolt is spawned where the projectile hit.
 */
public class SpawnLightningBolt implements ProjectileAction {

	static final int DURATION = 200;
	static final boolean DONT_HARVEST = false;

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// spawn a lightning bolt if no entity was hit
		if (movObjPos.entityHit == null) {
			BlockPos spawnPosition = calculatePosition(world, movObjPos);
			EntityLightningBolt bolt = new EntityLightningBolt(world, spawnPosition.getX(), spawnPosition.getY(),
					spawnPosition.getZ(), LIGHTNING_NOT_EFFECT_ONLY);
			world.addWeatherEffect(bolt);
			return;
		}

		// spawn lightning bolts around the hit mob
		Entity entity = movObjPos.entityHit;
		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			EntityLightningBolt bolt = new EntityLightningBolt(world, typedPos.getX(), typedPos.getY(), typedPos.getZ(),
					LIGHTNING_NOT_EFFECT_ONLY);
			world.addWeatherEffect(bolt);
		}

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
