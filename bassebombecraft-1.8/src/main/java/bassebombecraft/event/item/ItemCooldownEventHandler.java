package bassebombecraft.event.item;

import static bassebombecraft.player.PlayerUtils.isEntityPlayer;

import bassebombecraft.item.ItemUtils;
import bassebombecraft.item.inventory.GenericInventoryItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Event handler for handling reset of cool down when equipping idols.
 */
@Mod.EventBusSubscriber
public class ItemCooldownEventHandler {

	
	/**
	 * Handle the {@linkplain LivingEquipmentChangeEvent} to implement 
	 * reset of idol cooldown when equipped.
	 * 
	 * @param event the event to handle.
	 */
	@SubscribeEvent
	public static void handleEvent(LivingEquipmentChangeEvent event) {
		
		// get item
		ItemStack itemStack = event.getTo();
		Item item = itemStack.getItem();

		// exit if item isn't an idol
		if(!ItemUtils.isInventoryItem(item)) return;
		
		// type cast as idol
		GenericInventoryItem inventoryItem = (GenericInventoryItem) item;
		
		// exit if entity isn't player
		EntityLivingBase entity = event.getEntityLiving();
		if (!isEntityPlayer(entity))
			return;

		// type cast as player
		EntityPlayer player = (EntityPlayer) entity;
		
		// reset cooldown
		// exit if cooldown is effect
		if (player.getCooldownTracker().hasCooldown(item))
			return;

		// add cooldown
		CooldownTracker tracker = player.getCooldownTracker();
		tracker.setCooldown(item, inventoryItem.getCoolDown());
		
	}	
	
}