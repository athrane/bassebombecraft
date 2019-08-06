package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
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
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare position
		Vec3d posVec = null;

		// spawn if entity was hit
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get entity position
			posVec = entity.getPositionVector();
			posVec = posVec.add(0, entity.getHeight(), 0);
		}

		// spawn if block was hit
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			BlockPos spawnPosition = calculatePosition(blockResult);
			posVec = new Vec3d(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
		}

		// spawn squid
		SquidEntity entity = EntityType.SQUID.create(world);
		double lx = posVec.x;
		double ly = posVec.y + Y_SPAWN_OFFSET;
		double lz = posVec.z;
		float yaw = projectile.rotationYaw;
		entity.setLocationAndAngles(lx, ly, lz, yaw, PITCH);
		world.addEntity(entity);
	}
}
