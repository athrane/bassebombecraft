package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.setTemporaryBlock;
import static bassebombecraft.entity.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.entity.projectile.ProjectileUtils.isTypeEntityRayTraceResult;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns an anvil
 * over the top of the hit entity.
 * 
 * If a block is hit then NO-OP.
 */
public class SpawnAnvil implements ProjectileAction {

	private static final int Y_SPAWN_OFFSET = 10;
	static final int DURATION = 10;
	static final boolean DONT_HARVEST = false;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {

		// exit if no entity was hit
		if (!isEntityHit(result))
			return;

		// exit if result isn't entity ray trace result;
		if (!isTypeEntityRayTraceResult(result))
			return;

		// get entity
		Entity entity = ((EntityRayTraceResult) result).getEntity();

		// get entity position
		Vec3d posVec = entity.getPositionVector();
		float height = entity.getHeight();
		double lx = posVec.x;
		double ly = posVec.y + height + Y_SPAWN_OFFSET;
		double lz = posVec.z;

		BlockPos blockpos = new BlockPos(lx, ly, lz);
		setTemporaryBlock(world, blockpos, Blocks.ANVIL, DURATION);
	}

}
