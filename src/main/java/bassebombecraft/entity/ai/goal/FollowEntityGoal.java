package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.AI_PATH_RECALC_UPDATE_FREQUENCY;
import static bassebombecraft.ModConstants.AI_TARGET_WATCH_DIST;
import static bassebombecraft.entity.EntityUtils.isMinimumDistanceReached;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.world.entity.ai.goal.Goal.Flag.MOVE;
import static net.minecraft.world.level.pathfinder.BlockPathTypes.WATER;

import java.util.EnumSet;

import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

/**
 * AI goal for entity, e.g. spawned mob, charmed mob or guardian.
 * 
 * The goal will follow the designated leader.
 */
public class FollowEntityGoal extends Goal {

	/**
	 * Goal owner.
	 */
	final Mob entity;

	/**
	 * Entity to follow.
	 */
	LivingEntity leaderEntity;

	/**
	 * Follow speed..
	 */
	double followSpeed;

	/**
	 * Minimum distance to target.
	 */
	float minDistance;

	/**
	 * Maximum distance to target.
	 */
	float maxDistance;

	/**
	 * Minimum distance to target (squared).
	 */
	float minDistanceSqr;

	/**
	 * FollowEntity AI goal.
	 * 
	 * @param entity      entity to which the task is applied.
	 * @param leader      entity to be followed.
	 * @param followSpeed following speed.
	 * @param minDist     minimum distance.
	 * @param maxDist     maximum distance.
	 */
	public FollowEntityGoal(Mob entity, LivingEntity leader, double followSpeed, float minDist, float maxDist) {
		this.entity = entity;
		this.leaderEntity = leader;
		this.followSpeed = followSpeed;
		this.minDistance = minDist;
		this.maxDistance = maxDist;
		minDistanceSqr = minDistance * minDistance;

		// "movement" AI
		setFlags(EnumSet.of(MOVE, LOOK));
	}

	@Override
	public boolean canUse() {

		// exit if leader is undefined
		if (leaderEntity == null)
			return false;

		// if leader isn't alive clear leader from task
		if (!leaderEntity.isAlive()) {
			leaderEntity = null;
			return false;
		}

		// execute if minimum distance hasn't been reached yet
		boolean isMinDistReached = isMinimumDistanceReached(entity, leaderEntity, minDistanceSqr);
		return (!isMinDistReached);

	}

	@Override
	public void start() {
		entity.setPathfindingMalus(WATER, 0.0F);
	}

	@Override
	public void tick() {
		try {

			// look at
			LookControl lookController = entity.getLookControl();
			lookController.setLookAt(leaderEntity, AI_TARGET_WATCH_DIST,
					(float) entity.getMaxHeadXRot());

			// exit if frequency isn't active
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
			if (!repository.isActive(AI_PATH_RECALC_UPDATE_FREQUENCY))
				return;

			// move toward target
			PathNavigation navigator = entity.getNavigation();
			navigator.moveTo(leaderEntity, followSpeed);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void stop() {
		PathNavigation navigator = entity.getNavigation();
		navigator.stop();
	}

}
