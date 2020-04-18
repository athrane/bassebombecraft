package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.captureGoals;

import java.util.Set;

import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

/**
 * Charmed mob.
 */
public class CharmedMob {

	/**
	 * Value for expired charm.
	 */
	static final int IS_EXPIRED = 0;

	/**
	 * Id used for registration of duration.
	 */
	String id;

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
		
		try {
			// register charmed mob
			DurationRepository repository = getProxy().getDurationRepository();
			id = entity.getName().getUnformattedComponentText();
			repository.add(id, duration);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);
		}
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

	public boolean isCharmExpired() {
		try {
			DurationRepository repository = getProxy().getDurationRepository();
			return repository.isExpired(id);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// return charm is expired
			return true;
		}
	}

	public int getDuration() {
		try {
			DurationRepository repository = getProxy().getDurationRepository();

			// return zero if expired
			if (repository.isExpired(id))
				return IS_EXPIRED;

			return repository.get(id);

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// return zero as expired
			return IS_EXPIRED;
		}
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
