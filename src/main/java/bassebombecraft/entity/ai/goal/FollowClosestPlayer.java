package bassebombecraft.entity.ai.goal;

import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNavigator;

/**
 * AI goal for companion, e.g. charmed mob or guardian.
 * 
 * The goal will follow the closest player.
 */
public class FollowClosestPlayer extends Goal {

	static final int UPDATE_DELAY = 10;
	static final float WATCH_DIST = 8.0F;

	/**
	 * Goal owner.
	 */
	final CreatureEntity entity;

	/**
	 * Closed playe to follow.
	 */
	PlayerEntity closestPlayer;

	int updateDelayCounter = 0;
	float minDistanceSqr; // minimum distance to player (squared)
	double movementSpeed;

	/**
	 * FollowClosestPlayer AI goal.
	 * 
	 * @param entity        entity to which the task is applied.
	 * @param minDistance   minimum distance to keep to the nearest player.
	 * @param movementSpeed movement speed.
	 */
	public FollowClosestPlayer(CreatureEntity entity, float minDistance, double movementSpeed) {
		this.entity = entity;
		this.minDistanceSqr = minDistance * minDistance;
		this.movementSpeed = movementSpeed;

		// "movement" AI
		setMutexFlags(EnumSet.of(MOVE, LOOK));
	}

	@Override
	public boolean shouldExecute() {
		closestPlayer = this.entity.getEntityWorld().getClosestPlayer(entity, WATCH_DIST);
		return (closestPlayer != null);
	}

	@Override
	public boolean shouldContinueExecuting() {
		// if player isn't alive then determine if a new player can be found
		if (!closestPlayer.isAlive())
			return shouldExecute();
		return isMinimumDistanceReached();
	}

	@Override
	public void startExecuting() {
		updateDelayCounter = 0;
	}

	@Override
	public void tick() {

		// add pause between attacks
		updateDelayCounter = updateDelayCounter + 1;

		// reset count if threshold is reached
		if (updateDelayCounter < UPDATE_DELAY)
			return;
		updateDelayCounter = 0;

		// move towards
		PathNavigator navigator = entity.getNavigator();
		navigator.tryMoveToEntityLiving(closestPlayer, movementSpeed);
	}

	@Override
	public void resetTask() {
		closestPlayer = null;
		PathNavigator navigator = entity.getNavigator();
		navigator.clearPath();
	}

	/**
	 * Returns true if minimum distance is reached.
	 *
	 * @return true if minimum distance is reached.
	 */
	boolean isMinimumDistanceReached() {
		double distSqr = entity.getDistanceSq(closestPlayer);

		// exit if minimum distance reached
		boolean result = (distSqr >= minDistanceSqr);
		return result;
	}
}
