package bassebombecraft.entity.ai.goal;

import static bassebombecraft.entity.EntityUtils.selfDestruct;
import static bassebombecraft.entity.ai.AiUtils.setMutexFlagsforTargetingGoal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

/**
 * AI target acquisition goal which self-destructs the entity if / when the
 * target has died.
 */
public class SelfdestructWhenTargetDiesGoal extends Goal {

	/**
	 * Null/No target value to use when clearing the target.
	 */
	static final LivingEntity NO_TARGET = null;

	/**
	 * Goal owner.
	 */
	final MobEntity entity;

	/**
	 * Target.
	 */
	LivingEntity target;

	/**
	 * SelfdestructWhenTargetDiesGoal constructor.
	 * 
	 * @param entity entity.
	 * @param target target entity.
	 */
	public SelfdestructWhenTargetDiesGoal(MobEntity entity, LivingEntity target) {
		this.entity = entity;
		this.target = target;
		setMutexFlagsforTargetingGoal(this);
	}

	@Override
	public boolean shouldExecute() {

		// if target is undefined then self-destruct
		if (target == null)
			return true;

		// if target isn't alive then self-destruct
		if (target.isAlive())
			return false;

		return true;
	}

	@Override
	public void tick() {
		entity.setAttackTarget(NO_TARGET);
		target = NO_TARGET;
		selfDestruct(entity);
	}

	@Override
	public void resetTask() {
		// NO-OP
	}

}
