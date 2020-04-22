package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.captureGoals;

import java.util.Set;
import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
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
	 * Consumer to support callback when {@linkplain DurationRepository} expires a
	 * {@linkplain CharmedMob} added by {@linkplain CharmedMobsRepository}.
	 * 
	 * When invoked by the {@linkplain DurationRepository} the expired element will
	 * be removed from the {@linkplain CharmedMobsRepository} as well.
	 */
	Consumer<String> cRemovalCallback;
	
	/**
	 * CharmedMob constructor.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob is
	 *                         uncharmed.
	 */
	CharmedMob(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		this.entity = entity;
		goals = captureGoals(entity.goalSelector);
		targetGoals = captureGoals(entity.targetSelector);
		
		try {
			// register charmed mob
			DurationRepository repository = getProxy().getDurationRepository();
			id = Integer.toString(entity.getEntityId());
			repository.add(id, duration, cRemovalCallback);

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

	public MobEntity getEntity() {
		return entity;
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
	 * @param entity           charmed mob.
	 * @param duration         duration of charm in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob is
	 *                         uncharmed.
	 */
	public static CharmedMob getInstance(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		return new CharmedMob(entity, duration, cRemovalCallback);
	}

}
