package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_FOLLOW_CLOEST_PLAYER_UPDATE_FREQUENCY;
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
 */
public class FollowClosestPlayer extends Goal {

	/**
	 * Watch distance.
	 */
	static final float WATCH_DIST = 10.0F;

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
	public FollowClosestPlayer(CreatureEntity entity, float minDistance, double movementSpeed) {
		this.entity = entity;
		this.minDistanceSqr = minDistance * minDistance;
		this.movementSpeed = movementSpeed;

		// "movement" AI
		setMutexFlags(EnumSet.of(MOVE, LOOK));
	}

	@Override
	public boolean shouldExecute() {

		// get closest player
		closestPlayer = this.entity.getEntityWorld().getClosestPlayer(entity, WATCH_DIST);

		// exit if no player could be founds
		if (closestPlayer == null)
			return false;

		// exit if player isn't alive
		if (!closestPlayer.isAlive())
			return false;

		return isMinimumDistanceReached();
	}

	@Override
	public void tick() {

		// exit if frequency isn't active
		FrequencyRepository repository = getBassebombeCraft().getFrequencyRepository();
		if (!repository.isActive(AI_FOLLOW_CLOEST_PLAYER_UPDATE_FREQUENCY))
			return;

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
