package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a cobweb
 * around the hit mob.
 * 
 * If a block is hit then a cobweb is spawned where the projectile hit.
 */
public class SpawnCobweb implements ProjectileAction {

	static final int DURATION = 400;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// spawn a cobweb if no entity was hit
		if (!isEntityHit(result)) {
			BlockPos spawnPosition = calculatePosition(world, result);
			setTemporaryBlock(world, spawnPosition, Blocks.COBWEB, DURATION);
			return;
		}

		// exit if result isn't entity ray trace result;
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();
		
		// encapsulate mob bounding box in web
		AxisAlignedBB aabb = entity.getBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			setTemporaryBlock(world, typedPos, Blocks.COBWEB, DURATION);
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
