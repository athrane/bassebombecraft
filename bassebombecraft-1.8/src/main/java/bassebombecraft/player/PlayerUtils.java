package bassebombecraft.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.MathHelper;

/**
 * Utility for calculating information about the player.
 */
public class PlayerUtils {

	/**
	 * Calculate player feet position (as a Y coordinate).
	 * 
	 * @param player
	 *            player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static double calculatePlayerFeetPositition(EntityPlayer player) {
		double playerFeetPosY = player.posY - player.getYOffset();
		return playerFeetPosY;
	}

	/**
	 * Calculate player feet position (as a Y coordinate).
	 * 
	 * @param player
	 *            player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static int calculatePlayerFeetPosititionAsInt(EntityPlayer player) {
		return (int) calculatePlayerFeetPositition(player);
	}

	/**
	 * Returns true if y coordinate is below the player y position.
	 * 
	 * @param y
	 *            coordinate to compare.
	 * @param player
	 *            player object.
	 * 
	 * @return true if y coordinate is below the player y position.
	 */
	public static boolean isBelowPlayerYPosition(int y, EntityPlayer player) {
		int playerFeetPosition = PlayerUtils.calculatePlayerFeetPosititionAsInt(player);
		return (y < playerFeetPosition);
	}

	/**
	 * Get player eye position.
	 * 
	 * @param player
	 *            player object.
	 * 
	 * @return player eye position.
	 */
	public static Vec3d getPlayerEyePos(EntityPlayer player) {
		Vec3d eyePosition = new Vec3d(player.posX, player.posY + (player.getEntityWorld().isRemote
				? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()), player.posZ);

		return eyePosition;
	}

	/**
	 * Return player direction as an integer between 0 to 3: 0 when looking
	 * south, 1 when looking West, 2 looking North and 3 looking East.
	 * 
	 * @param player
	 *            player object.
	 * 
	 * @return player direction as an integer between 0 to 3.
	 */
	public static PlayerDirection getPlayerDirection(EntityPlayer player) {
		int direction = MathHelper.floor((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		return PlayerDirection.getById(direction);
	}

	/**
	 * Send chat message to player.
	 * 
	 * @param player
	 *            to message to.
	 * @param string
	 *            message to send to player.
	 */
	public static void sendChatMessageToPlayer(EntityPlayer player, String string) {
		ITextComponent message = new TextComponentString(string);
		player.sendMessage(message);
	}

	/**
	 * Returns true if entities has identical unique ID's. Returns false if
	 * either entity is null.
	 * 
	 * @param e1
	 *            entity one to test.
	 * @param e2
	 *            entity two to test.
	 * @return true if entities has identical unique ID's.
	 */
	public static boolean hasIdenticalUniqueID(Entity e1, Entity e2) {
		if (e1 == null)
			return false;
		if (e2 == null)
			return false;
		return (e1.getUniqueID() == e2.getUniqueID());
	}
}
