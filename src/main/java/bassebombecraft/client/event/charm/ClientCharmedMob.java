package bassebombecraft.client.event.charm;

import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.entity.ai.AiUtils.getCharmDuration;

import java.util.Set;

import bassebombecraft.event.charm.CharmedMob;
import bassebombecraft.event.charm.ServerCharmedMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;

/**
 * CLIENT side implementation of {@linkplain CharmedMob}. Used by
 * {@linkplain ClientCharmedMobsRepository} to store charmed mobs.
 * 
 * The client side implementation does NOT capture the AI goals of the charmed
 * entity. Restore of AI goals is handled by the server side implementation
 * {@linkplain ServerCharmedMob}.
 */
public class ClientCharmedMob implements CharmedMob {

	/**
	 * Charmed mob.
	 */
	final Mob entity;

	/**
	 * ID used for registration and lookup of duration.
	 */
	String id;

	/**
	 * Display name used for rendering.
	 */
	String name;

	/**
	 * Constructor.
	 * 
	 * @param entity   charmed mob.
	 * @param duration duration of charm in measured in ticks.
	 */
	ClientCharmedMob(Mob entity, int duration) {
		this.entity = entity;
		id = Integer.toString(entity.getId());
		name = entity.getName().getString() + "/" + id;
	}

	public Set<WrappedGoal> getGoals() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported at client side.");
	}

	public Set<WrappedGoal> getTargetGoals() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported at client side.");
	}

	public Mob getEntity() {
		return entity;
	}

	public int getDuration() {
		return getCharmDuration(id, getProxy().getClientDurationRepository());
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
	public static ClientCharmedMob getInstance(Mob entity, int duration) {
		return new ClientCharmedMob(entity, duration);
	}

}
