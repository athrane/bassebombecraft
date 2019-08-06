package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.world.WorldUtils.addLightning;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns a lightning
 * bolt a the hit mob.
 * 
 * If a block is hit then an lightning bolt is spawned where the projectile hit.
 */
public class SpawnLightningBolt implements ProjectileAction {

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// spawn a lightning bolt if a block was hit
		if (isBlockHit(result)) {

			// exit if result isn't block ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			BlockPos spawnPosition = calculatePosition(blockResult);
			LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(world);
			bolt.setPosition(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
			addLightning(bolt, world);
			return;
		}

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// spawn lightning bolts around the hit mob
		AxisAlignedBB aabb = entity.getBoundingBox();

		BlockPos min = new BlockPos(aabb.minX, aabb.minY, aabb.minZ);
		BlockPos max = new BlockPos(aabb.maxX, aabb.maxY, aabb.maxZ);
		for (Object pos : BlockPos.getAllInBox(min, max)) {
			BlockPos typedPos = (BlockPos) pos;
			LightningBoltEntity bolt = EntityType.LIGHTNING_BOLT.create(world);
			bolt.setPosition(typedPos.getX(), typedPos.getY(), typedPos.getZ());
			addLightning(bolt, world);
		}

	}

}
