package bassebombecraft.projectile.action;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.block.BlockUtils.setTemporaryBlock;

import bassebombecraft.event.block.temporary.TemporaryBlockRepository;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
	public void execute(EntityThrowable projectile, World world, MovingObjectPosition movObjPos) {
		
		// NO-OP if no entity was hit
		if (movObjPos.entityHit == null) {
			// NO-OP
			return;
		}
		
		// get entity position
		Entity entityHit = movObjPos.entityHit;

		Vec3 posVec = entityHit.getPositionVector();
		float height = entityHit.height;		
		double lx = posVec.xCoord;
		double ly = posVec.yCoord + height +Y_SPAWN_OFFSET;
		double lz = posVec.zCoord;
		
		BlockPos blockpos = new BlockPos(lx, ly, lz);
		setTemporaryBlock(world, blockpos, Blocks.anvil, DURATION);		
	}

}
