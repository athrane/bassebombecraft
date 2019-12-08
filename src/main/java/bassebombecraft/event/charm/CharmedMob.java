package bassebombecraft.event.charm;

import static bassebombecraft.entity.ai.AiUtils.captureGoals;

import java.util.Set;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

/**
 * Charmed mob.
 */
public class CharmedMob {

	/**
	 * Captured AI goals.
	 */
	final Set<PrioritizedGoal> goals;

	/**
	 * Captured AI targeting goals.
	 */
	final Set<PrioritizedGoal> targetGoals;

	/**
	 * Charmed mob.
	 */
	final MobEntity entity;

	/**
	 * Charm duration in ticks
	 */
	int duration;

	/**
	 * CharmedMob constructor.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 * 
	 */
	CharmedMob(MobEntity entity, int duration) {
		this.entity = entity;
		goals = captureGoals(entity.goalSelector);
		targetGoals = captureGoals(entity.targetSelector);
		this.duration = duration;
	}

	public Set<PrioritizedGoal> getGoals() {
		return goals;
	}

	public Set<PrioritizedGoal> getTargetGoals() {
		return targetGoals;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public void update() {
		if (duration == 0)
			return;
		duration = duration - 1;
	}

	public boolean isCharmExpired() {
		return (duration == 0);
	}

	public int getDuration( ) {
		return duration;
	}
	
	/**
	 * CharmedMob factory method.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 * 
	 */
	public static CharmedMob getInstance(MobEntity entity, int duration) {
		return new CharmedMob(entity, duration);
	}

}
