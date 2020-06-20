package bassebombecraft.client.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

/**
 * Utility for calculating information about the player.
 * 
 * Class should only be loaded by by the configuration: physical client /
 * logical client.
 * 
 * The reason is it contains imports of the Minecraft client
 * {@linkplain Minecraft} which will break class loading on a server.
 */

public class ClientPlayerUtils {

	/**
	 * Returns true if player is defined in Minecraft client.
	 * 
	 * @return true if player is defined in Minecraft client.
	 */
	public static boolean isClientSidePlayerDefined() {
		Minecraft mcClient = Minecraft.getInstance();
		if (mcClient == null)
			return false;
		return (mcClient.player != null);
	}

	/**
	 * Returns player from Minecraft client.
	 * 
	 * @return player from Minecraft client.
	 */
	public static PlayerEntity getClientSidePlayer() {
		if (!isClientSidePlayerDefined())
			return null;
		Minecraft mcClient = Minecraft.getInstance();
		return mcClient.player;
	}

	/**
	 * Get player UID at client side.
	 * 
	 * @return player UID.
	 */
	public static String getClientSidePlayerUId() {
		return Minecraft.getInstance().getSession().getUsername();
	}

}
