package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns an ice block
 * around the hit mob.
 * 
 * If a block is hit then an ice block is spawned where the projectile hit.
 */
public class SpawnIceBlock implements ProjectileAction {

	static final int DURATION = 200;
	static final boolean DONT_HARVEST = false;

	@Override
	public void execute(EntityThrowable projectile, World world, MovingObjectPosition movObjPos) {

		// spawn a temporary lava block if no entity was hit
		if (movObjPos.entityHit == null) {
			BlockPos spawnPosition = calculatePosition(world, movObjPos);
			setTemporaryBlock(world, spawnPosition, Blocks.ice, DURATION);
			return;
		}

		// spawn temporary ice block under around the hit mob
		Entity entity = movObjPos.entityHit;
		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			setTemporaryBlock(world, typedPos, Blocks.ice, DURATION);
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
	BlockPos calculatePosition(World world, MovingObjectPosition movObjPos) {
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
