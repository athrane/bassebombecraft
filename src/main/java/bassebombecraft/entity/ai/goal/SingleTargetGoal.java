package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.EntityUtils.selfDestruct;
import static bassebombecraft.entity.ai.AiUtils.setMutexFlagsforTargetingGoal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

/**
 * AI target acquisition goal which targets the same static target.
 * 
 * If the target died the the targeting entity self-destruct.
 */
public class SingleTargetGoal extends Goal {

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
	 * SingleTargetGoal constructor.
	 * 
	 * @param entity entity.
	 * @param target target entity.
	 */
	public SingleTargetGoal(MobEntity entity, LivingEntity target) {
		this.entity = entity;
		this.target = target;
		setMutexFlagsforTargetingGoal(this);
	}

	@Override
	public boolean shouldExecute() {

		// exit if target is undefined
		if (target == null)
			return false;

		// execute if target is alive
		if (target.isAlive())
			return true;

		return false;
	}

	@Override
	public void tick() {

		// update target
		entity.setAttackTarget(target);
	}

	@Override
	public void resetTask() {

		// self-destruct if target is undefined
		if (target == null) {
			entity.setAttackTarget(NO_TARGET);
			selfDestruct(entity);			
			return;
		}

		// don't reset if target is alive
		if (target.isAlive())
			return;

		// reset
		entity.setAttackTarget(NO_TARGET);
		target = NO_TARGET;
		selfDestruct(entity);
	}

}
