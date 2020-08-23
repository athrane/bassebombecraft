package bassebombecraft.projectile.action;

import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.entity.projectile.ProjectileUtils.*;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain ProjectileAction} which sets the spawn
 * point.
 * 
 * If a block is hit then the spawn point is set there. If an entity is hit the
 * spawn point will be set at the entity position.
 */
public class SetSpawnPoint implements ProjectileAction {

	/**
	 * Set forced spawn point.
	 */
	static final boolean FORCED = true;
	
	/**
	 * Don't check for bed.
	 */
	static final boolean NO_BED = false;

	@Override
	public void execute(ThrowableEntity projectile, World world, RayTraceResult result) {
		
		// exit if thrower isn't a player
		LivingEntity thrower = projectile.getThrower();
		if (!isTypePlayerEntity(thrower))
			return;

		// exit if nothing was hit
		if (isNothingHit(result))
			return;
		
		// declare 
		BlockPos spawnPosition = null;
		
		// spawn at hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result;
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position
			spawnPosition = entity.getPosition();
		}

		// spawn at hit block 
		if(isBlockHit(result)) {
			
			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;			
			
			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get block position 
			spawnPosition = calculatePosition(blockResult);
		}
		
		// type cast
		PlayerEntity player = (PlayerEntity) thrower;

		// set spawn point
		player.setSpawnPoint(spawnPosition, FORCED, NO_BED,world.getDimension().getType());		
	}

}
