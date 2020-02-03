package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.ai.AiUtils.setMutexFlagsforAttackGoal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.DamageSource;

/**
 * Attack goal which attacks target if it is alive and within range.
 * 
 * Damage type is mob damage.
 */
public class AttackInRangeGoal extends Goal {

	/**
	 * Null/No target value to use when clearing the target.
	 */
	final static LivingEntity NO_TARGET = null;

	/**
	 * Goal owner.
	 */
	final MobEntity entity;

	/**
	 * Target.
	 */
	LivingEntity target;

	/**
	 * Minimum distance (squared) for attack.
	 */
	float minDistanceSqr;

	/**
	 * Damage delivered on attack
	 */
	float damage;

	/**
	 * AttackInRangeGoal constructor.
	 * 
	 * @param entity      entity.
	 * @param target      target entity.
	 * @param minDistance minimum distance for attack.
	 * @param damage      damage delivered on attack.
	 */
	public AttackInRangeGoal(MobEntity entity, LivingEntity target, float minDistance, float damage) {
		this.entity = entity;
		this.target = target;
		this.minDistanceSqr = minDistance * minDistance;
		this.damage = damage;
		setMutexFlagsforAttackGoal(this);
	}

	@Override
	public boolean shouldExecute() {

		// exit if target is undefined
		if (target == null) {
			return false;
		}

		// exit if target isn't alive
		if (!target.isAlive())
			return false;

		// attack if entity intersects with target bounding box
		if (target.getBoundingBox().intersects(entity.getBoundingBox()))
			return true;

		// attack if minimum range has been reached
		return isMinimumDistanceReached();
	}

	@Override
	public void tick() {

		// attack
		target.attackEntityFrom(DamageSource.causeMobDamage(entity), damage);
	}

	@Override
	public void resetTask() {

		// don't reset if target is undefined
		if (target == null)
			return;

		// don't reset if target is alive
		if (target.isAlive())
			return;

		// reset
		entity.setAttackTarget(NO_TARGET);
		target = NO_TARGET;
	}

	/**
	 * Returns true if minimum distance is reached.
	 *
	 * @return true if minimum distance is reached.
	 */
	boolean isMinimumDistanceReached() {
		double distSqr = entity.getDistanceSq(target);
		return (distSqr < minDistanceSqr);
	}

}
