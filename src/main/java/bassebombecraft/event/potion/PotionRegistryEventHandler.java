package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.*;
import static bassebombecraft.potion.PotionUtils.getInstance;

import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.MobProjectileEffect;
import bassebombecraft.potion.effect.MobsAggroEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Event handler for registration of potions.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionRegistryEventHandler {

	/**
	 * Mobs aggro effect, used by {@linkplain MobsAggroIdolInventoryItem}.
	 */
	public static final Effect MOBS_AGGRO_EFFECT = new MobsAggroEffect();

	/**
	 * Primed mob effect, used by {@linkplain PrimeMobIdolInventoryItem}.
	 */
	public static final Effect PRIMED_MOB_EFFECT = new MobPrimingEffect();

	/**
	 * Bear blaster effect, used by {@linkplain ShootBearBlaster}.
	 */
	public static final Effect BEAR_BLASTER_EFFECT = new MobProjectileEffect("BearBlasterProjectileEffect");

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect PRIMED_CREEPER_CANNON_EFFECT = new MobProjectileEffect(
			"PrimedCreeperCannonProjectilePotion");

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect CREEPER_CANNON_EFFECT = new MobProjectileEffect("CreeperCannonProjectileEffect");

	/**
	 * Bacon Bazooka effect, used by {@linkplain ShootBaconBazooka}.
	 */
	public static final Effect BACON_BAZOOKA_EFFECT = new MobProjectileEffect("BaconBazookaProjectileEffect");

	/**
	 * Potion amplifier effect.
	 */
	public static final Effect AMPLIFIER_EFFECT = new AmplifierEffect();

	/**
	 * Handle {@linkplain RegistryEvent.Register<Potion>} event to register potions
	 * with forge.
	 * 
	 * @param event to handle.
	 */
	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		IForgeRegistry<Potion> registry = event.getRegistry();

		// create and register weak amplification potion
		String name = WEAK_AMPLIFICATION_POTION_NAME.toLowerCase();
		String configPath = "Potions." + WEAK_AMPLIFICATION_POTION_NAME;
		Potion weakAmplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);
		registry.register(weakAmplificationPotion);

		// add recipe
		Ingredient base = Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
		Ingredient reagant = Ingredient.fromStacks(new ItemStack(Items.ENDER_PEARL));
		ItemStack out = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), weakAmplificationPotion);
		BrewingRecipeRegistry.addRecipe(new BrewingRecipe(base, reagant, out));		
				
		// create and register amplification potion
		name = AMPLIFICATION_POTION_NAME.toLowerCase();
		configPath = "Potions." + AMPLIFICATION_POTION_NAME;
		Potion amplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);		
		registry.register(amplificationPotion );

		// add recipe
		base = Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), weakAmplificationPotion));
		reagant = Ingredient.fromStacks(new ItemStack(Items.CHORUS_FRUIT));
		out = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), amplificationPotion);
		BrewingRecipeRegistry.addRecipe(new BrewingRecipe(base, reagant, out));		
		
		// create and register superior amplification potion
		name = SUPERIOR_AMPLIFICATION_POTION_NAME.toLowerCase();
		configPath = "Potions." + SUPERIOR_AMPLIFICATION_POTION_NAME;
		Potion superiorAmplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);				
		registry.register(superiorAmplificationPotion);

		// add recipe
		base = Ingredient.fromStacks(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), amplificationPotion));
		reagant = Ingredient.fromStacks(new ItemStack(Items.NETHER_STAR));
		out = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), superiorAmplificationPotion);
		BrewingRecipeRegistry.addRecipe(new BrewingRecipe(base, reagant, out));				
	}

	/**
	 * Handle {@linkplain RegistryEvent.Register<Potion>} event to register potions
	 * with forge.
	 * 
	 * @param event to handle.
	 */
	@SubscribeEvent
	public static void registerEffects(RegistryEvent.Register<Effect> event) {
		IForgeRegistry<Effect> registry = event.getRegistry();
		registry.register(AMPLIFIER_EFFECT);
		registry.register(MOBS_AGGRO_EFFECT);
		registry.register(PRIMED_MOB_EFFECT);
		// registry.register(BEAR_BLASTER_EFFECT);
		// registry.register(PRIMED_CREEPER_CANNON_EFFECT);
		// registry.register(CREEPER_CANNON_EFFECT);
		// registry.register(BACON_BAZOOKA_EFFECT);
	}

}
