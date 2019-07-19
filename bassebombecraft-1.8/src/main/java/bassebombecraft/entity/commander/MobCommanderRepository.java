package bassebombecraft.entity.commander;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface for repository for mob commander.
 */
public interface MobCommanderRepository {

	enum Commands {
		NULL,	
		STOP,
		COMMANDERS_TARGET,
		NEAREST_MOB,
		NEAREST_PLAYER
	}
	
	/**
	 * Returns true if player is registered as mob commander.
	 * 
	 * @param player
	 *            player to test participation for.
	 * 
	 * @return true if player is registered as mob commander.
	 */
	public boolean isRegistered(PlayerEntity player);

	/**
	 * Register as mob commander.
	 * 
	 * @param player
	 *            Player to register as mob commander.
	 */
	public void register(PlayerEntity player);

	/**
	 * Unregister as mob commander.
	 * 
	 * @param player
	 *            Player to unregister as mob commander.
	 */
	public void remove(PlayerEntity player);

	/**
	 * Clear registry.
	 */
	public void clear();

	/**
	 * Get next mob command.
	 */
	public MobCommand getCommand(PlayerEntity player);

	/**
	 * Cycle mob command
	 * 
	 * @param player
	 *            Player to cycle command for.
	 */
	public void cycle(PlayerEntity player);

}
