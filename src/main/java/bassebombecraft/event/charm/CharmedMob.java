package bassebombecraft.event.charm;

import java.util.Set;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

/**
 * Charmed mob.
 */
public interface CharmedMob {

	/**
	 * Value for expired charm.
	 */
	static final int IS_EXPIRED = 0;
	
	/**
	 * Return set of captured AI goals.
	 * 
	 * @return captured AI goals.
	 * 
	 * @throws UnsupportedOperationException if operation isn't supported.
	 */
	public Set<PrioritizedGoal> getGoals() throws UnsupportedOperationException;

	/**
	 * Return set of captured AI target goals.
	 * 
	 * @return captured AI target goals.
	 * 
	 * @throws UnsupportedOperationException if operation isn't supported.
	 */
	public Set<PrioritizedGoal> getTargetGoals() throws UnsupportedOperationException;

	/**
	 * Return charmed mob.
	 * 
	 * @return charmed mob
	 */
	public MobEntity getEntity();

	/**
	 * Return remaining duration of charm (in game ticks).
	 * 
	 * @return remaining duration of charm (in game ticks).
	 */
	public int getDuration();

	/**
	 * Return ID used to identify instance in repositories.
	 * 
	 * @return ID used to identify instance in repositories
	 */
	public String getId();
	
}
