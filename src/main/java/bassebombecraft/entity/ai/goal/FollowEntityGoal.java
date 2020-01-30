package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_PATH_RECALC_UPDATE_FREQUENCY;
import static bassebombecraft.ModConstants.AI_TARGET_WATCH_DIST;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;
import static net.minecraft.pathfinding.PathNodeType.WATER;

import java.util.EnumSet;

import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;

/**
 * AI goal for entity, e.g. spawned mob, charmed mob or guardian.
 * 
 * The goal will follow the designated leader.
 */
public class FollowEntityGoal extends Goal {

	/**
	 * Goal owner.
	 */
	final MobEntity entity;

	/**
	 * Entity to follow.
	 */
	LivingEntity leaderEntity;

	double followSpeed;
	float minDistance;
	float maxDistance;
	float minDistanceSqr; // minimum distance to player (squared)

	/**
	 * FollowEntity AI goal.
	 * 
	 * @param entity        entity to which the task is applied.
	 * @param leader        entity to be followed.
	 * @param followSpeed following speed.
	 * @param minDist     minimum distance.
	 * @param maxDist     maximum distance.
	 */
	public FollowEntityGoal(MobEntity entity, LivingEntity leader, double followSpeed, float minDist, float maxDist) {
		this.entity = entity;
		this.leaderEntity = leader;
		this.followSpeed = followSpeed;
		this.minDistance = minDist;
		this.maxDistance = maxDist;
		minDistanceSqr = minDistance * minDistance;

		// "movement" AI
		setMutexFlags(EnumSet.of(MOVE, LOOK));
	}

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

	@Override
	public void startExecuting() {
		entity.setPathPriority(WATER, 0.0F);
	}

	@Override
	public void tick() {

		// look at
		LookController lookController = entity.getLookController();
		lookController.setLookPositionWithEntity(leaderEntity, AI_TARGET_WATCH_DIST,
				(float) entity.getVerticalFaceSpeed());

		// exit if frequency isn't active
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();
		if (!repository.isActive(AI_PATH_RECALC_UPDATE_FREQUENCY))
			return;

		// calculate path
		PathNavigator navigator = entity.getNavigator();
		navigator.tryMoveToEntityLiving(leaderEntity, followSpeed);
	}

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
