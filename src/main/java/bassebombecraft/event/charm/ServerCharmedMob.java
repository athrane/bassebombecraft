package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.captureGoals;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.Set;
import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

/**
 * SERVER side implementation of {@linkplain CharmedMob}. Used by
 * {@linkplain ServerCharmedMobsRepository} to store charmed mobs.
 * 
 * The server side implementation of {@linkplain CharmedMob} captures the AI
 * goals of the charmed entity for later restore when the charm expired.
 */
public class ServerCharmedMob implements CharmedMob {

	/**
	 * ID used for registration and lookup of duration.
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
	 * Constructor.
	 * 
	 * @param entity           charmed mob.
	 * @param duration         duration of charm in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	ServerCharmedMob(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		this.entity = entity;
		goals = captureGoals(entity.goalSelector);
		targetGoals = captureGoals(entity.targetSelector);
		id = Integer.toString(entity.getEntityId());

		// register charmed mob with server duration repository
		DurationRepository repository = getProxy().getServerDurationRepository();
		repository.add(id, duration, cRemovalCallback);
	}

	public Set<PrioritizedGoal> getGoals() throws UnsupportedOperationException {
		return goals;
	}

	public Set<PrioritizedGoal> getTargetGoals() throws UnsupportedOperationException {
		return targetGoals;
	}

	public MobEntity getEntity() {
		return entity;
	}

	public int getDuration() {
		return getCharmDuration(id, getProxy().getServerDurationRepository());
	}

	/**
	 * Factory method.
	 * 
	 * @param entity           charmed mob.
	 * @param duration         duration of charm in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	public static ServerCharmedMob getInstance(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		return new ServerCharmedMob(entity, duration, cRemovalCallback);
	}

}
