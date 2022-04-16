package bassebombecraft.entity.ai.goal;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.AI_PATH_RECALC_UPDATE_FREQUENCY;
import static bassebombecraft.ModConstants.AI_TARGET_WATCH_DIST;
import static bassebombecraft.entity.EntityUtils.isMinimumDistanceReached;
import static net.minecraft.entity.ai.goal.Goal.Flag.LOOK;
import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;

importimport bassebombecraft.event.frequency.FrequencyRepository;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;

 javanet.minecraft.world.entity.ai.goal.Goal.Flagft.event.frequency.FrequencyRepository;
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
	final PathfinderMob entity;

	/**
	 * Closed playe to follow.
	 */
	Player closestPlayer;

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
	public FollowClosestPlayerGoal(PathfinderMob entity, float minDistance, double movementSpeed) {
		this.entity = entity;
		this.minDistanceSqr = minDistance * minDistance;
		this.movementSpeed = movementSpeed;

		// "movement" AI
		setFlags(EnumSet.of(MOVE, LOOK));
	}

	@Override
	public boolean canUse() {

		// get closest player
		closestPlayer = this.entity.getCommandSenderWorld().getNearestPlayer(entity, AI_TARGET_WATCH_DIST);

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
			FrequencyRepository repository = getProxy().getServerFrequencyRepository();
			if (!repository.isActive(AI_PATH_RECALC_UPDATE_FREQUENCY))
				return;

			// move towards
			PathNavigation navigator = entity.getNavigation();
			navigator.moveTo(closestPlayer, movementSpeed);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
	}

	@Override
	public void stop() {
		closestPlayer = null;
		PathNavigation navigator = entity.getNavigation();
		navigator.stop();
	}

}
