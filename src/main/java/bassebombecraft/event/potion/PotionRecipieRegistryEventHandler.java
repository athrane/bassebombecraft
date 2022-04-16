package bassebombecraft.event.potion;

import static bassebombecraft.potion.PotionUtils.registerPotionRecipe;
import static bassebombecraft.potion.RegisteredPotions.*;
import static net.minecraft.world.item.Items.*;
import static net.minecraft.potion.Potions.AWKWARD;
import staticnet.minecraft.world.item.alchemy.Potionson.Mod.EventBusSubscriber.Bus.MOD;

import bassebombecraft.potion.RegisteredPotions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Event handler for registration of potion recipes.
 */
@EventBusSubscriber(bus = MOD)
public class PotionRecipieRegistryEventHandler {

	/**
	 * Handle {@linkplain FMLCommonSetupEvent} event to register potion recipes from
	 * the potions defined in {@linkplain RegisteredPotions}.
	 * 
	 * @param event setup event.
	 */
	@SubscribeEvent
	public static void handleRegisterEntityType(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> registerPotionRecipe(AWKWARD, ENDER_PEARL, WEAK_AMPLIFICATION_POTION));
		event.enqueueWork(() -> registerPotionRecipe(WEAK_AMPLIFICATION_POTION, CHORUS_FRUIT, AMPLIFICATION_POTION));
		event.enqueueWork(() -> registerPotionRecipe(AMPLIFICATION_POTION, NETHER_STAR, SUPERIOR_AMPLIFICATION_POTION));
		event.enqueueWork(() -> registerPotionRecipe(AMPLIFICATION_POTION, NETHER_STAR, SUPERIOR_AMPLIFICATION_POTION));
		event.enqueueWork(() -> registerPotionRecipe(AWKWARD, EMERALD, MOB_AGGRO_POTION));
		event.enqueueWork(() -> registerPotionRecipe(AWKWARD, FIRE_CHARGE, MOB_PRIMING_POTION));
	}

}
