package bassebombecraft.player.pvp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for repository for handling PVP.
 */
public interface PvpRepository {

	/**
	 * Returns true if player is registered for PVP.
	 * 
	 * @param player
	 *            player to test participation for.
	 * 
	 * @return true if player is registered for PVP.
	 */
	public boolean isRegisteredForPvp(EntityPlayer player);

	/**
	 * Participate in PVP.
	 * 
	 * @param player Player to add to PVP or extend participation for.
	 */
	public void participate(EntityPlayer player); 
	
	/**
	 * Unregister PVP participant.
	 * 
	 * @param particpant
	 *            PVP participant.
	 */
	public void remove(PvpParticipation participant);

	/**
	 * Return true if PVP is active, e.g. more than one player is registered.
	 * 
	 * @return true if PVP is active, e.g. more than one player is registered.
	 */
	boolean isPvpActive();

	/**
	 * Clear PVP.
	 */
	public void clear();

	/**
	 * Update player participation.
	 */
	public void updatePlayerParticipation();

}
