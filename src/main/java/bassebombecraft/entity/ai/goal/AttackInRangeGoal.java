package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.EntityUtils.isMinimumDistanceReached;
import static bassebombecraft.entity.ai.AiUtils.setMutexFlagsforAttackGoal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.damagesource.DamageSource;

/**
 * Attack goal which attacks target if it is alive and within range.
 * 
 * Damage type is mob damage.
 */
public class AttackInRangeGoal extends Goal {

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
	public AttackInRangeGoal(Mob entity, LivingEntity target, float minDistance, float damage) {
		this.entity = entity;
		this.target = target;
		this.minDistanceSqr = minDistance * minDistance;
		this.damage = damage;
		setMutexFlagsforAttackGoal(this);
	}

	@Override
	public boolean canUse() {

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
		return isMinimumDistanceReached(entity, target, minDistanceSqr);
	}

	@Override
	public void tick() {

		// attack
		target.hurt(DamageSource.mobAttack(entity), damage);
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
		entity.setTarget(NO_TARGET);
		target = NO_TARGET;
	}

}
