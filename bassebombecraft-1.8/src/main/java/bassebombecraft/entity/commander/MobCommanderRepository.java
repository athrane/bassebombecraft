package bassebombecraft.entity.commander;

import net.minecraft.entity.LivingEntity;

/**
 * Interface for repository for mob commander.
 */
public interface MobCommanderRepository {

	enum Commands {
		NULL, STOP, COMMANDERS_TARGET, NEAREST_MOB, NEAREST_PLAYER
	}

	/**
	 * Returns true if player is registered as mob commander.
	 * 
	 * @param entity entity to test registration for.
	 * 
	 * @return true if entity is registered as mob commander.
	 */
	public boolean isRegistered(LivingEntity entity);

	/**
	 * Register as mob commander.
	 * 
	 * @param entity entity to register as mob commander.
	 */
	public void register(LivingEntity entity);

	/**
	 * Unregister as mob commander.
	 * 
	 * @param entity entity to unregister as mob commander.
	 */
	public void remove(LivingEntity entity);

	/**
	 * Clear registry.
	 */
	public void clear();

	/**
	 * Get next mob command.
	 * 
	 * @param entity commander to get command from.
	 * 
	 * @return mob command to be executed by commanded mobs.
	 */
	public MobCommand getCommand(LivingEntity entity);

	/**
	 * Cycle mob command
	 * 
	 * @param entity commander to cycle command for.
	 */
	public void cycle(LivingEntity entity);

}
