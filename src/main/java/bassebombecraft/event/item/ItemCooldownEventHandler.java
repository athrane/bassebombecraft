package bassebombecraft.event.item;

import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import bassebombecraft.item.ItemUtils;
import bassebombecraft.item.inventory.GenericInventoryItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Event handler for handling reset of cool down when equipping idols.
 */
@Mod.EventBusSubscriber
public class ItemCooldownEventHandler {

	/**
	 * Handle the {@linkplain LivingEquipmentChangeEvent} to implement reset of idol
	 * cooldown when equipped.
	 * 
	 * @param event the event to handle.
	 */
	@SubscribeEvent
	public static void handleLivingEquipmentChangeEvent(LivingEquipmentChangeEvent event) {

		// get item
		ItemStack itemStack = event.getTo();
		Item item = itemStack.getItem();

		// exit if item isn't an idol
		if (!ItemUtils.isTypeInventoryItem(item))
			return;

		// type cast as idol
		GenericInventoryItem inventoryItem = (GenericInventoryItem) item;

		// exit if entity isn't player
		LivingEntity entity = event.getEntityLiving();
		if (!isTypePlayerEntity(entity))
			return;

		// type cast as player
		Player player = (Player) entity;

		// reset cooldown
		// exit if cooldown is effect
		if (player.getCooldowns().isOnCooldown(item))
			return;

		// add cooldown
		ItemCooldowns tracker = player.getCooldowns();
		tracker.addCooldown(item, inventoryItem.getCoolDown());

	}

}
