package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.AI_PATH_RECALC_UPDATE_FREQUENCY;
import static bassebombecraft.ModConstants.AI_TARGET_WATCH_DIST;
import static bassebombecraft.entity.EntityUtils.isMinimumDistanceReached;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;

import java.util.EnumSet;

import bassebombecraft.event.frequency.FrequencyRepository;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;

/**
 * AI goal for companion, e.g. charmed mob or guardian.
 * 
 * The goal will follow the closest player.
 * 
 * Deprecated since it isn't used.
 */
@Deprecated
public class FollowClosestPlayerGoal extends Goal {

	/**
	 * Goal owner.
	 */
	final CreatureEntity entity;

	/**
	 * Closed playe to follow.
	 */
	PlayerEntity closestPlayer;

	/**
	 * Minimum distance to player (squared).
	 */
	float minDistanceSqr;

	/**
	 * Movement speed.
	 */
	double movementSpeed;

	/**
	 * FollowClosestPlayer AI goal.
	 * 
	 * @param entity        entity to which the task is applied.
	 * @param minDistance   minimum distance to keep to the nearest player.
	 * @param movementSpeed movement speed.
	 */
	public FollowClosestPlayerGoal(CreatureEntity entity, float minDistance, double movementSpeed) {
		this.entity = entity;
		this.minDistanceSqr = minDistance * minDistance;
		this.movementSpeed = movementSpeed;

		// "movement" AI
		setMutexFlags(EnumSet.of(MOVE, LOOK));
	}

	@Override
	public boolean shouldExecute() {

		// get closest player
		closestPlayer = this.entity.getEntityWorld().getClosestPlayer(entity, AI_TARGET_WATCH_DIST);

		// exit if no player could be founds
		if (closestPlayer == null)
			return false;

		// exit if player isn't alive
		if (!closestPlayer.isAlive())
			return false;

		// execute if minimum distance hasn't been reached yet
		boolean isMinDistReached = isMinimumDistanceReached(entity, closestPlayer, minDistanceSqr);
		return (!isMinDistReached);
	}

	@Override
	public void tick() {
		try {

			// exit if frequency isn't active
			FrequencyRepository repository = getProxy().getFrequencyRepository();
			if (!repository.isActive(AI_PATH_RECALC_UPDATE_FREQUENCY))
				return;

			// move towards
			PathNavigator navigator = entity.getNavigator();
			navigator.tryMoveToEntityLiving(closestPlayer, movementSpeed);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void resetTask() {
		closestPlayer = null;
		PathNavigator navigator = entity.getNavigator();
		navigator.clearPath();
	}

}
