package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;

import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which spawns an
 * anvil over the top of the hit entity.
 * 
 * If a block is hit then NO-OP.
 */
public class SpawnAnvil implements ProjectileAction {

	private static final int Y_SPAWN_OFFSET = 10;
	static final int DURATION = 10;
	static final boolean DONT_HARVEST = false;

	static TemporaryBlockRepository tempBlockRepository = getBassebombeCraft().getTemporaryBlockRepository();
	
	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult movObjPos) {
		
		// NO-OP if no entity was hit
		if (movObjPos.entityHit == null) {
			// NO-OP
			return;
		}
		
		// get entity position
		Entity entityHit = movObjPos.entityHit;

		Vec3d posVec = entityHit.getPositionVector();
		float height = entityHit.height;		
		double lx = posVec.x;
		double ly = posVec.y + height +Y_SPAWN_OFFSET;
		double lz = posVec.z;
		
		BlockPos blockpos = new BlockPos(lx, ly, lz);
		setTemporaryBlock(world, blockpos, Blocks.ANVIL, DURATION);		
	}

}
