package bassebombecraft.event.potion;

import static bassebombecraft.ModConstants.AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_AGGRO_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_PRIMING_POTION_NAME;
import static bassebombecraft.ModConstants.POTIONS_CONFIGPATH;
import static bassebombecraft.ModConstants.SUPERIOR_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.WEAK_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.potion.PotionUtils.getInstance;
import static bassebombecraft.potion.PotionUtils.registerPotionRecipe;
import static net.minecraft.item.Items.CHORUS_FRUIT;
import static net.minecraft.item.Items.EMERALD;
import static net.minecraft.item.Items.ENDER_PEARL;
import static net.minecraft.item.Items.FIRE_CHARGE;
import static net.minecraft.item.Items.NETHER_STAR;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.MobAggroEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.MobProjectileEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
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
	public static final Effect MOB_AGGRO_EFFECT = new MobAggroEffect();

	/**
	 * Primed mob effect, used by {@linkplain PrimeMobIdolInventoryItem}.
	 */
	public static final Effect MOB_PRIMING_EFFECT = new MobPrimingEffect();

	/**
	 * Bear blaster effect, used by {@linkplain ShootBearBlaster}.
	 */
	public static final Effect BEAR_BLASTER_EFFECT = new MobProjectileEffect(
			ModConfiguration.bearBlasterProjectileEffectForce.get(),
			ModConfiguration.bearBlasterProjectileEffectExplosion.get());

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
	public static final Effect BACON_BAZOOKA_EFFECT = new MobProjectileEffect(
			ModConfiguration.baconBazookaProjectileEffectForce.get(),
			ModConfiguration.baconBazookaProjectileEffectExplosion.get());

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
		String configPath = POTIONS_CONFIGPATH + WEAK_AMPLIFICATION_POTION_NAME;
		Potion weakAmplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);
		registry.register(weakAmplificationPotion);
		registerPotionRecipe(Potions.AWKWARD, ENDER_PEARL, weakAmplificationPotion);

		// create and register amplification potion
		name = AMPLIFICATION_POTION_NAME.toLowerCase();
		configPath = POTIONS_CONFIGPATH + AMPLIFICATION_POTION_NAME;
		Potion amplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);
		registry.register(amplificationPotion);
		registerPotionRecipe(weakAmplificationPotion, CHORUS_FRUIT, amplificationPotion);

		// create and register superior amplification potion
		name = SUPERIOR_AMPLIFICATION_POTION_NAME.toLowerCase();
		configPath = POTIONS_CONFIGPATH + SUPERIOR_AMPLIFICATION_POTION_NAME;
		Potion superiorAmplificationPotion = getInstance(name, configPath, AMPLIFIER_EFFECT);
		registry.register(superiorAmplificationPotion);
		registerPotionRecipe(amplificationPotion, NETHER_STAR, superiorAmplificationPotion);

		// create and register mob aggro potion
		name = MOB_AGGRO_POTION_NAME.toLowerCase();
		configPath = POTIONS_CONFIGPATH + MOB_AGGRO_POTION_NAME;
		Potion mobAggroPotion = getInstance(name, configPath, MOB_AGGRO_EFFECT);
		registry.register(mobAggroPotion);
		registerPotionRecipe(Potions.AWKWARD, EMERALD, mobAggroPotion);

		// create and register mob priming potion
		name = MOB_PRIMING_POTION_NAME.toLowerCase();
		configPath = POTIONS_CONFIGPATH + MOB_PRIMING_POTION_NAME;
		Potion primedMobPotion = getInstance(name, configPath, MOB_PRIMING_EFFECT);
		registry.register(primedMobPotion);
		registerPotionRecipe(Potions.AWKWARD, FIRE_CHARGE, mobAggroPotion);
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
		registry.register(MOB_AGGRO_EFFECT);
		registry.register(MOB_PRIMING_EFFECT);
		// registry.register(BEAR_BLASTER_EFFECT);
		// registry.register(PRIMED_CREEPER_CANNON_EFFECT);
		// registry.register(CREEPER_CANNON_EFFECT);
		// registry.register(BACON_BAZOOKA_EFFECT);
	}

}
