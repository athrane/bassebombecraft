package bassebombecraft.player.pvp;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface for PVP participation.
 */
public interface PvpParticipation {

	/**
	 * Get player.
	 * 
	 * @return player.
	 */
	EntityPlayer getPlayer();

	/**
	 * Update duration of PVP participation.
	 */
	void updateDuration();
		
	/**
	 * Extend participation.
	 */
	void extendParticipation();

	/**
	 * Return true if participation is expired.
	 * 
	 * @return true if participation is expired.
	 */
	boolean isExpired();

}
