package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static net.minecraft.block.Blocks.LAVA;

import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
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

	static final int DURATION = 400;

	static TemporaryBlockRepository tempBlockRepository = getBassebombeCraft().getTemporaryBlockRepository();

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// spawn a temporary lava block if block was hit
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// spawn block
			BlockPos spawnPosition = calculatePosition(blockResult);
			setTemporaryBlock(world, spawnPosition, LAVA, DURATION);
			return;
		}

		// exit if entity isn't hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// spawn temporary ice block under around the hit mob
		AxisAlignedBB aabb = entity.getBoundingBox();
		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		BlockPos.getAllInBox(min, max).forEach(pos -> setTemporaryBlock(world, pos, LAVA, DURATION));
	}

}
