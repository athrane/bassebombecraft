package bassebombecraft.player;

import static bassebombecraft.BassebombeCraft.getMincraft;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * Utility for calculating information about the player.
 */
public class PlayerUtils {

	/**
	 * Calculate player feet position (as a Y coordinate).
	 * 
	 * @param player player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static double calculatePlayerFeetPositition(PlayerEntity player) {
		double playerFeetPosY = player.posY - player.getYOffset();
		return playerFeetPosY;
	}

	/**
	 * Calculate player feet position (as a Y coordinate).
	 * 
	 * @param player player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static int calculatePlayerFeetPosititionAsInt(PlayerEntity player) {
		return (int) calculatePlayerFeetPositition(player);
	}

	/**
	 * Returns true if y coordinate is below the player y position.
	 * 
	 * @param y      coordinate to compare.
	 * @param player player object.
	 * 
	 * @return true if y coordinate is below the player y position.
	 */
	public static boolean isBelowPlayerYPosition(int y, PlayerEntity player) {
		int playerFeetPosition = PlayerUtils.calculatePlayerFeetPosititionAsInt(player);
		return (y < playerFeetPosition);
	}

	/**
	 * Return player direction as an integer between 0 to 3: 0 when looking south, 1
	 * when looking West, 2 looking North and 3 looking East.
	 * 
	 * @param player player object.
	 * 
	 * @return player direction as an integer between 0 to 3.
	 */
	public static PlayerDirection getPlayerDirection(PlayerEntity player) {
		int direction = MathHelper.floor((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		return PlayerDirection.getById(direction);
	}

	/**
	 * Send chat message to player.
	 * 
	 * @param player to message to.
	 * @param string message to send to player.
	 */
	public static void sendChatMessageToPlayer(PlayerEntity player, String string) {
		ITextComponent message = new StringTextComponent(string);
		player.sendMessage(message);
	}

	/**
	 * Returns true if entities has identical unique ID's. Returns false if either
	 * entity is null.
	 * 
	 * @param e1 entity one to test.
	 * @param e2 entity two to test.
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
	 * return true if entity is a {@linkplain PlayerEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain PlayerEntity}.
	 */
	public static boolean isTypePlayerEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof PlayerEntity;
		return false;
	}

	/**
	 * return true if entity is a {@linkplain PlayerEntity} and it is alive.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain PlayerEntity} and it is alive.
	 */
	public static boolean isPlayerEntityAlive(Entity entity) {
		if(!isTypePlayerEntity(entity)) return false;
		PlayerEntity targetAsPlayer = (PlayerEntity) entity;
		return targetAsPlayer.isAlive();
	}	
	
	/**
	 * Returns true if item is held in player off hand.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held in off hand.
	 * 
	 * @return true if item is held in player off hand.
	 */
	public static boolean isItemHeldInOffHand(PlayerEntity player, ItemStack item) {
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
	 * Returns true if item is held in either player hands.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held by player.
	 * 
	 * @return true if item is held by player in either hand.
	 */
	public static boolean isItemHeldInEitherHands(PlayerEntity player, Item item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		ItemStack itemStackOffhand = player.getHeldItemOffhand();		
		if (itemStackOffhand == null)
			return false;

		Item itemOffhand = itemStackOffhand.getItem();
		if (itemOffhand == null)
			return false;
		
		// exit if item is held in off hand
		if(item.equals(itemOffhand)) return true;
		
		ItemStack itemStack = player.getHeldItemOffhand();		
		if (itemStack == null)
			return false;

		Item itemMainhand = itemStack.getItem();
		if (itemMainhand == null)
			return false;
		
		// exit if item is held in main hand
		return (item.equals(itemMainhand));		
	}
	
	/**
	 * Returns true if item is held in hotbar.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held in off hand.
	 * 
	 * @return true if item is held in player off hand.
	 */
	public static boolean isItemInHotbar(PlayerEntity player, Item item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		// iterator over the items in the hotbar
		for (int i = 0; i < 10; i++) {
			ItemStack itemStackHotbar = player.inventory.getStackInSlot(i);
			Item itemHotBar = itemStackHotbar.getItem();
			if (itemHotBar.equals(item))
				return true;
		}

		return false;
	}

	/**
	 * Get player UID at client side.
	 * 
	 * @return player UID.
	 */
	public static String getClientSidePlayerUId() {
		return Minecraft.getInstance().getSession().getUsername();
	}

	/**
	 * Calculate player position
	 * 
	 * @param player       player
	 * @param partialTicks partial ticks.
	 * 
	 * @return player position.
	 */
	public static Vec3d CalculatePlayerPosition(PlayerEntity player, float partialTicks) {
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
		return (getMincraft().player != null);
	}

	/**
	 * Returns player from Minecraft.
	 * 
	 * @return player from Minecraft.
	 */
	public static PlayerEntity getPlayer() {
		if (!isPlayerDefined())
			return null;
		return getMincraft().player;
	}

}
