package bassebombecraft.entity.commander;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for repository for mob commander.
 */
public interface MobCommanderRepository {

	enum Commands {
		NULL,	
		WAIT,
		COMMANDERS_TARGET
	}
	
	/**
	 * Returns true if player is registered as mob commander.
	 * 
	 * @param player
	 *            player to test participation for.
	 * 
	 * @return true if player is registered as mob commander.
	 */
	public boolean isRegistered(EntityPlayer player);

	/**
	 * Register as mob commander.
	 * 
	 * @param player
	 *            Player to register as mob commander.
	 */
	public void register(EntityPlayer player);

	/**
	 * Unregister as mob commander.
	 * 
	 * @param player
	 *            Player to unregister as mob commander.
	 */
	public void remove(EntityPlayer player);

	/**
	 * Clear registry.
	 */
	public void clear();

	/**
	 * Get next mob command.
	 */
	public MobCommand getCommand(EntityPlayer player);

	/**
	 * Cycle mob command
	 * 
	 * @param player
	 *            Player to cycle command for.
	 */
	public void cycle(EntityPlayer player);

}
