package bassebombecraft.event.potion;

import static bassebombecraft.potion.PotionUtils.registerPotionRecipe;
import static bassebombecraft.potion.RegisteredPotions.AMPLIFICATION_POTION;
import static bassebombecraft.potion.RegisteredPotions.MOB_AGGRO_POTION;
import static bassebombecraft.potion.RegisteredPotions.MOB_PRIMING_POTION;
import static bassebombecraft.potion.RegisteredPotions.SUPERIOR_AMPLIFICATION_POTION;
import static bassebombecraft.potion.RegisteredPotions.WEAK_AMPLIFICATION_POTION;
import static net.minecraft.world.item.Items.CHORUS_FRUIT;
import static net.minecraft.world.item.Items.EMERALD;
import static net.minecraft.world.item.Items.ENDER_PEARL;
import static net.minecraft.world.item.Items.FIRE_CHARGE;
import static net.minecraft.world.item.Items.NETHER_STAR;
import static net.minecraft.world.item.alchemy.Potions.AWKWARD;
import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

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
