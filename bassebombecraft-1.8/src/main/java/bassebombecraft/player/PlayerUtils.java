package bassebombecraft.player;

import static net.minecraft.client.Minecraft.getMinecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

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
		Vec3d eyePosition = new Vec3d(player.posX,
				player.posY + (player.getEntityWorld().isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight()
						: player.getEyeHeight()),
				player.posZ);

		return eyePosition;
	}

	/**
	 * Return player direction as an integer between 0 to 3: 0 when looking south, 1
	 * when looking West, 2 looking North and 3 looking East.
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
	 * Returns true if entities has identical unique ID's. Returns false if either
	 * entity is null.
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

	/**
	 * return true if entity is a {@linkplain EntityPlayer}.
	 * 
	 * @param entity
	 *            entity to test.
	 * 
	 * @return true if entity is a {@linkplain EntityPlayer}.
	 */
	public static boolean isEntityPlayer(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof EntityPlayer;
	}

	/**
	 * Returns true if item is held in player off hand.
	 * 
	 * @param player
	 *            player to test.
	 * @param item
	 *            item to test whether it is held in off hand.
	 * 
	 * @return true if item is held in player off hand.
	 */
	public static boolean isItemHeldInOffHand(EntityPlayer player, ItemStack item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		ItemStack heldItem = player.getHeldItemOffhand();

		if (heldItem == null)
			return false;

		return heldItem.equals(item);
	}

	/**
	 * Get player UID at client side.
	 * 
	 * @return player UID.
	 */
	public static String getClientSidePlayerUId() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	/**
	 * Calculate player position
	 * 
	 * @param player
	 *            player
	 * @param partialTicks
	 *            partial ticks.
	 * 
	 * @return player position.
	 */
	public static Vec3d CalculatePlayerPosition(EntityPlayer player, float partialTicks) {
		double doubleX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
		double doubleY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
		double doubleZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
		return new Vec3d(doubleX, doubleY, doubleZ);
	}

	/**
	 * Returns true if player is defined in Minecraft.
	 * 
	 * @return true if player is defined in Minecraft.
	 */
	public static boolean isPlayerDefined() {
		return (getMinecraft().player != null);
	}

	/**
	 * Returns player from Minecraft.
	 * 
	 * @return player from Minecraft.
	 */
	public static EntityPlayer getPlayer() {
		if(!isPlayerDefined()) return null;
		return Minecraft.getMinecraft().player;
	}
	
}
