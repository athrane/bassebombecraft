package bassebombecraft.player;

import java.util.Optional;
import java.util.UUID;

import static bassebombecraft.item.ItemUtils.*;
import bassebombecraft.item.book.GenericCompositeItemsBook;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

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
	public static double calculatePlayerFeetPositition(Player player) {
		double playerFeetPosY = player.getY() - player.getMyRidingOffset();
		return playerFeetPosY;
	}

	/**
	 * Calculate player feet position (as a Y coordinate).
	 * 
	 * @param player player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static int calculatePlayerFeetPosititionAsInt(Player player) {
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
	public static boolean isBelowPlayerYPosition(int y, Player player) {
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
	public static PlayerDirection getPlayerDirection(Player player) {
		int direction = Mth.floor((double) ((player.getYRot() * 4F) / 360F) + 0.5D) & 3;
		return PlayerDirection.getById(direction);
	}

	/**
	 * Send chat message to player.
	 * 
	 * @param player to message to.
	 * @param string message to send to player.
	 */
	public static void sendChatMessageToPlayer(Player player, String string) {
		Component message = new TextComponent(string);
		UUID uuid = player.getUUID();
		player.sendMessage(message, uuid);
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
		return (e1.getUUID() == e2.getUUID());
	}

	/**
	 * Returns true if entities has identical unique ID's. Returns false if either
	 * entity is null.
	 * 
	 * @param e1 entity one to test.
	 * @param e2 entity two to test.
	 * @return true if entities has identical unique ID's.
	 */
	public static boolean hasIdenticalUniqueID(LivingEntity e1, LivingEntity e2) {
		if (e1 == null)
			return false;
		if (e2 == null)
			return false;
		return (e1.getUUID() == e2.getUUID());
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
		if (oe.isPresent()) {
			return oe.get() instanceof Player;
		}
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
		if (!isTypePlayerEntity(entity))
			return false;
		Player targetAsPlayer = (Player) entity;
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
	public static boolean isItemHeldInOffHand(Player player, ItemStack item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		ItemStack heldItem = player.getOffhandItem();

		if (heldItem == null)
			return false;

		return heldItem.equals(item);
	}

	/**
	 * Returns true if item is held in player off hand.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held in off hand.
	 * 
	 * @return true if item is held in player off hand.
	 */
	public static boolean isItemHeldInOffHand(Player player, Item item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		// get item stack
		ItemStack heldItemStack = player.getOffhandItem();
		if (heldItemStack == null)
			return false;

		// get item
		Item heldItem = heldItemStack.getItem();
		if (heldItem == null)
			return false;

		return item.equals(heldItem);
	}

	/**
	 * Returns true if item is held in player main hand.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held in main hand.
	 * 
	 * @return true if item is held in player main hand.
	 */
	public static boolean isItemHeldInMainHand(Player player, Item item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		// get item stack
		ItemStack heldItemStack = player.getMainHandItem();
		if (heldItemStack == null)
			return false;

		// get item
		Item heldItem = heldItemStack.getItem();
		if (heldItem == null)
			return false;

		return item.equals(heldItem);
	}

	/**
	 * Returns true if item held in player main hand is a sub class of
	 * {@linkplain GenericCompositeItemsBook}.
	 * 
	 * @param player player to test.
	 * 
	 * @return true if item held in player main hand is a sub class of
	 *         {@linkplain GenericCompositeItemsBook}.
	 */
	public static boolean isItemHeldInMainHandOfTypeGenericCompositeItemsBook(Player player) {
		if (player == null)
			return false;

		// get item stack
		ItemStack heldItemStack = player.getMainHandItem();
		if (heldItemStack == null)
			return false;

		// get item
		Item heldItem = heldItemStack.getItem();
		if (heldItem == null)
			return false;

		return isTypeGenericCompositeItemsBook(heldItem);
	}

	/**
	 * Returns true if item is held in either player hands.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held by player.
	 * 
	 * @return true if item is held by player in either hand.
	 */
	public static boolean isItemHeldInEitherHands(Player player, Item item) {
		if (isItemHeldInMainHand(player, item))
			return true;
		if (isItemHeldInOffHand(player, item))
			return true;
		return false;
	}

	/**
	 * Returns true if item is held in hotbar.
	 * 
	 * @param player player to test.
	 * @param item   item to test whether it is held in off hand.
	 * 
	 * @return true if item is held in player off hand.
	 */
	public static boolean isItemInHotbar(Player player, Item item) {
		if (player == null)
			return false;
		if (item == null)
			return false;

		// iterator over the items in the hotbar
		for (int i = 0; i < 10; i++) {
		ItemStack itemStackHotbar = player.getInventory().getItem(i);
				return true;
		}

		return false;
	}

	/**
	 * Calculate player position
	 * 
	 * @param player       player
	 * @param partialTicks partial ticks.
	 * 
	 * @return player position.
	 */
	public static Vec3 CalculatePlayerPosition(Player player, float partialTicks) {
		double doubleX = player.xOld + (player.getX() - player.xOld) * partialTicks;
		double doubleY = player.yOld + (player.getY() - player.yOld) * partialTicks;
		double doubleZ = player.zOld + (player.getZ() - player.zOld) * partialTicks;
		return new Vec3(doubleX, doubleY, doubleZ);
	}

}
