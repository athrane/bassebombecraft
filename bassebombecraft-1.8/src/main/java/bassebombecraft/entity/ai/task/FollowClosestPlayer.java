package bassebombecraft.entity.ai.task;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * AI task for companion, e.g. charmed mob or guardian.
 * 
 * The task will follow the closest player.
 */
public class FollowClosestPlayer extends EntityAIBase {

	static final int UPDATE_DELAY = 10;
	static final float WATCH_DIST = 8.0F;

	final EntityLiving entity;
	EntityPlayer closestPlayer;
	int updateDelayCounter = 0;
	float minDistanceSqr; // minimum distance to player (squared)
	double movementSpeed;

	/**
	 * FollowClosestPlayer AI task.
	 * 
	 * @param entity
	 *            entity to which the task is applied.
	 * @param minDistance
	 *            minimum distance to keep to the nearest player.
	 * @param movementSpeed
	 *            movement speed.
	 */
	public FollowClosestPlayer(EntityLiving entity, float minDistance, double movementSpeed) {
		this.entity = entity;
		this.minDistanceSqr = minDistance * minDistance;
		this.movementSpeed = movementSpeed;
	}

	@Override
	public boolean shouldExecute() {
		closestPlayer = this.entity.worldObj.getClosestPlayerToEntity(entity, WATCH_DIST);
		return (closestPlayer != null);
	}

	@Override
	public void startExecuting() {
		updateDelayCounter = 0;
	}

	@Override
	public void updateTask() {

		// add pause between attacks
		updateDelayCounter = updateDelayCounter + 1;

		// reset count if threshold is reached
		if (updateDelayCounter < UPDATE_DELAY)
			return;
		updateDelayCounter = 0;

		entity.getNavigator().tryMoveToEntityLiving(closestPlayer, movementSpeed);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {

		// if player isn't alive then determine if a new player can be found
		if (!closestPlayer.isEntityAlive())
			return shouldExecute();

		// determine if entity has reached th minimum distance
		double distSqr = entity.getDistanceSqToEntity(closestPlayer);

		// exit if minimum distance reached
		boolean result = (distSqr >= minDistanceSqr);
		return result;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		closestPlayer = null;
		entity.getNavigator().clearPathEntity();
	}
}
