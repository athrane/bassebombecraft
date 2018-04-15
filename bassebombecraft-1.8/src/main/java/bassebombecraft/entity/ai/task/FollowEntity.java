package bassebombecraft.entity.ai.task;

import static bassebombecraft.player.PlayerUtils.isEntityPlayer;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;

/**
 * AI task for companion, e.g. charmed mob or guardian.
 * 
 * The task will follow the designated entity.
 */
public class FollowEntity extends EntityAIBase {

	static final int UPDATE_DELAY = 10;

	final EntityLiving entity;
	int timeToRecalcPath;
	EntityLivingBase leaderEntity;
	double followSpeed;
	PathNavigate petPathfinder;
	float minDistance;
	float maxDistance;
	float oldWaterCost;
	float minDistanceSqr; // minimum distance to player (squared)

	/**
	 * FollowEntity AI task.
	 * 
	 * @param entity
	 *            entity to which the task is applied.
	 * @param leader
	 *            entity to be followed.
	 * @param followSpeedIn
	 *            following speed.
	 * @param minDistIn
	 *            minimum distance.
	 * @param maxDistIn
	 *            maximum distance.
	 */
	public FollowEntity(EntityLiving entity, EntityLivingBase leader, double followSpeedIn, float minDistIn,
			float maxDistIn) {
		this.entity = entity;
		this.leaderEntity = leader;
		this.followSpeed = followSpeedIn;
		this.petPathfinder = entity.getNavigator();
		this.minDistance = minDistIn;
		this.maxDistance = maxDistIn;
		minDistanceSqr = minDistance * minDistance;
		
		//  "movement" AI
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {

		// exit if leader is undefined
		if (leaderEntity == null)
			return false;

		// if leader isn't alive clear leader from task
		if (!leaderEntity.isEntityAlive()) {
			leaderEntity = null;
			return false;
		}

		// exit if player spectator
		if (isEntityPlayer(leaderEntity)) {
			if (((EntityPlayer) leaderEntity).isSpectator())
				return false;
		}

		return isMinimumDistanceReached();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {

		// exit if leader is undefined
		if (leaderEntity == null)
			return false;

		// if player isn't alive
		if (!leaderEntity.isEntityAlive())
			return false;

		return isMinimumDistanceReached();
	}

	@Override
	public void startExecuting() {
		timeToRecalcPath = 0;
		oldWaterCost = entity.getPathPriority(PathNodeType.WATER);
		entity.setPathPriority(PathNodeType.WATER, 0.0F);
	}

	@Override
	public void updateTask() {

		// look at
		entity.getLookHelper().setLookPositionWithEntity(leaderEntity, 10.0F, (float) entity.getVerticalFaceSpeed());

		// update counter
		timeToRecalcPath--;

		// exit if threshold isn't reached
		if (timeToRecalcPath > 0)
			return;

		// reset counter
		timeToRecalcPath = UPDATE_DELAY;

		// calculate path
		petPathfinder.tryMoveToEntityLiving(leaderEntity, followSpeed);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		petPathfinder.clearPath();
		entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
	}

	/**
	 * Returns true if minimum distance is reached.
	 *
	 * @return true if minimum distance is reached.
	 */	
	boolean isMinimumDistanceReached() {	
		double distSqr = entity.getDistanceSqToEntity(leaderEntity);

		// exit if minimum distance reached
		boolean result = (distSqr >= minDistanceSqr);
		return result;
	}

	
}
