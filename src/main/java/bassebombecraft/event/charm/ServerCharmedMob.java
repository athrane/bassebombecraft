package bassebombecraft.event.charm;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.captureGoals;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.Set;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

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
	final Set<WrappedGoal> goals;

	/**
	 * Captured AI targeting goals.
	 */
	final Set<WrappedGoal> targetGoals;

	/**
	 * Charmed mob.
	 */
	final Mob entity;

	/**
	 * Constructor.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 */
	ServerCharmedMob(Mob entity, int duration) {
		this.entity = entity;
		goals = captureGoals(entity.goalSelector);
		targetGoals = captureGoals(entity.targetSelector);
		id = Integer.toString(entity.getId());
		name = entity.getName().getString() + "/" + id;		
	}

	public Set<WrappedGoal> getGoals() throws UnsupportedOperationException {
		return goals;
	}

	public Set<WrappedGoal> getTargetGoals() throws UnsupportedOperationException {
		return targetGoals;
	}

	public Mob getEntity() {
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
	public static ServerCharmedMob getInstance(Mob entity, int duration) {
		return new ServerCharmedMob(entity, duration);
	}

}
