package bassebombecraft.entity.ai.task;

import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;

/**
 * AI goal for companion, e.g. charmed mob or guardian.
 * 
 * The goal will follow the designated entity.
 */
public class FollowEntity extends Goal {

	static final int UPDATE_DELAY = 10;

	final CreatureEntity entity;
	int timeToRecalcPath;
	LivingEntity leaderEntity;
	double followSpeed;
	float minDistance;
	float maxDistance;
	float oldWaterCost;
	float minDistanceSqr; // minimum distance to player (squared)

	/**
	 * FollowEntity AI goal.
	 * 
	 * @param entity        entity to which the task is applied.
	 * @param leader        entity to be followed.
	 * @param followSpeedIn following speed.
	 * @param minDistIn     minimum distance.
	 * @param maxDistIn     maximum distance.
	 */
	public FollowEntity(CreatureEntity entity, LivingEntity leader, double followSpeedIn, float minDistIn,
			float maxDistIn) {
		this.entity = entity;
		this.leaderEntity = leader;
		this.followSpeed = followSpeedIn;
		this.minDistance = minDistIn;
		this.maxDistance = maxDistIn;
		minDistanceSqr = minDistance * minDistance;

		// "movement" AI
		setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
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
		if (!leaderEntity.isAlive()) {
			leaderEntity = null;
			return false;
		}

		// exit if player spectator
		if (isTypePlayerEntity(leaderEntity)) {
			if (((PlayerEntity) leaderEntity).isSpectator())
				return false;
		}

		return isMinimumDistanceReached();
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean shouldContinueExecuting() {

		// exit if leader is undefined
		if (leaderEntity == null)
			return false;

		// if player isn't alive
		if (!leaderEntity.isAlive())
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
	public void tick() {

		// look at
		entity.getLookController().setLookPositionWithEntity(leaderEntity, 10.0F,
				(float) entity.getVerticalFaceSpeed());

		// update counter
		timeToRecalcPath--;

		// exit if threshold isn't reached
		if (timeToRecalcPath > 0)
			return;

		// reset counter
		timeToRecalcPath = UPDATE_DELAY;

		// calculate path
		PathNavigator navigator = entity.getNavigator();
		navigator.tryMoveToEntityLiving(leaderEntity, followSpeed);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		PathNavigator navigator = entity.getNavigator();
		navigator.clearPath();
	}

	/**
	 * Returns true if minimum distance is reached.
	 *
	 * @return true if minimum distance is reached.
	 */
	boolean isMinimumDistanceReached() {
		double distSqr = entity.getDistanceSq(leaderEntity);

		// exit if minimum distance reached
		boolean result = (distSqr >= minDistanceSqr);
		return result;
	}

}
