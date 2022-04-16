package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.EntityUtils.isMinimumDistanceReached;
import static bassebombecraft.entity.ai.AiUtils.setMutexFlagsforMovementGoal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * Charge goal which moves entity towards target until target is within range.
 */
public class ChargeTowardsGoal extends Goal {

	/**
	 * Speed modifier for movement controller. Should be 1, actual speed is read
	 * form entity attributes.
	 */
	static final double SPEED_MODIFIER = 1.0D;

	/**
	 * Null/No target value to use when clearing the target.
	 */
	static final LivingEntity NO_TARGET = null;

	/**
	 * Goal owner.
	 */
	final Mob entity;

	/**
	 * Target.
	 */
	LivingEntity target;

	/**
	 * Minimum distance (squared) to stop charging.
	 */
	float minDistanceSqr;

	/**
	 * ChargeTowardsGoal constructor.
	 * 
	 * @param entity      entity.
	 * @param target      target entity.
	 * @param minDistance minimum distance to target to stop charging.
	 */
	public ChargeTowardsGoal(Mob entity, LivingEntity target, int minDistance) {
		this.entity = entity;
		this.target = target;
		this.minDistanceSqr = minDistance * minDistance;
		setMutexFlagsforMovementGoal(this);
	}

	@Override
	public boolean canUse() {

		// exit if target is undefined
		if (target == null)
			return false;

		// exit if target isn't alive
		if (!target.isAlive())
			return false;

		// exit if entity intersects with target bounding box
		if (target.getBoundingBox().intersects(entity.getBoundingBox()))
			return false;

		// charge if minimum range hasn't been reached yet
		boolean isMinDistReached = isMinimumDistanceReached(entity, target, minDistanceSqr);
		return (!isMinDistReached);
	}

	@Override
	public void tick() {

		// move towards target
		Vec3 Vector3d = target.getEyePosition(1.0F);
		entity.getMoveControl().setWantedPosition(Vector3d.x, Vector3d.y, Vector3d.z, SPEED_MODIFIER);

		// update target
		entity.setTarget(target);
	}

	@Override
	public void stop() {

		// don't reset if target is undefined
		if (target == null)
			return;

		// don't reset if target is alive
		if (target.isAlive())
			return;

		// reset
		target = NO_TARGET;
	}

}
