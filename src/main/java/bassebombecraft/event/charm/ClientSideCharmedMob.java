package bassebombecraft.event.charm;

import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;
import static bassebombecraft.entity.ai.AiUtils.registerCharmedMob;

import java.util.Set;
import java.util.function.Consumer;

import bassebombecraft.event.duration.DurationRepository;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

/**
 * CLIENT side implementation of {@linkplain CharmedMob}. Used by
 * {@linkplain ClientSideCharmedMobsRepository} to store charmed mobs.
 * 
 * The client side implementation does NOT capture the AI goals of the charmed
 * entity. Restore of AI goals is handled by the server side implementation
 * {@linkplain ServerSideCharmedMob}.
 */
public class ClientSideCharmedMob implements CharmedMob {

	/**
	 * Charmed mob.
	 */
	final MobEntity entity;

	/**
	 * ID used for registration and lookup of duration.
	 */
	String id;

	/**
	 * Constructor.
	 * 
	 * @param entity           charmed mob.
	 * @param duration         duration of charm in measured in ticks.
	 * @param cRemovalCallback removal callback function invoked by
	 *                         {@linkplain DurationRepository} when mob charm
	 *                         expires.
	 */
	ClientSideCharmedMob(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		this.entity = entity;
		id = Integer.toString(entity.getEntityId());
		registerCharmedMob(id, duration, cRemovalCallback);
	}

	public Set<PrioritizedGoal> getGoals() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported at client side.");
	}

	public Set<PrioritizedGoal> getTargetGoals() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported at client side.");
	}

	public MobEntity getEntity() {
		return entity;
	}

	public int getDuration() {
		return getCharmDuration(id);
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
	public static ClientSideCharmedMob getInstance(MobEntity entity, int duration, Consumer<String> cRemovalCallback) {
		return new ClientSideCharmedMob(entity, duration, cRemovalCallback);
	}

}
