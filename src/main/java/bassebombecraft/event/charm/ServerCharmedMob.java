package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.captureGoals;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.Set;

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
	 * Display name used for rendering.
	 */
	String name;
	
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
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 */
	ServerCharmedMob(MobEntity entity, int duration) {
		this.entity = entity;
		goals = captureGoals(entity.goalSelector);
		targetGoals = captureGoals(entity.targetSelector);
		id = Integer.toString(entity.getEntityId());
		name = entity.getName().getString() + "/" + id;		
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

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * Factory method.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 */
	public static ServerCharmedMob getInstance(MobEntity entity, int duration) {
		return new ServerCharmedMob(entity, duration);
	}

}
