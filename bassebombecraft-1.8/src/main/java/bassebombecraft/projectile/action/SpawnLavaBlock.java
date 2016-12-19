package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;

import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a lava floor
 * around the hit mob.
 * 
 * If a block is hit then several lava block is spawned where the projectile
 * hit.
 */
public class SpawnLavaBlock implements ProjectileAction {

	static final int DURATION = 200;
	static final boolean DONT_HARVEST = false;

	static TemporaryBlockRepository tempBlockRepository = getBassebombeCraft().getTemporaryBlockRepository();

	@Override
	public void execute(EntityThrowable projectile, World world, RayTraceResult movObjPos) {

		// spawn a temporary lava block if no entity was hit
		if (movObjPos.entityHit == null) {
			BlockPos spawnPosition = calculatePosition(world, movObjPos);
			setTemporaryBlock(world, spawnPosition, Blocks.LAVA, DURATION);
			return;
		}

		// spawn temporary ice block under around the hit mob
		Entity entity = movObjPos.entityHit;
		AxisAlignedBB aabb = entity.getEntityBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			setTemporaryBlock(world, typedPos, Blocks.LAVA, DURATION);
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
