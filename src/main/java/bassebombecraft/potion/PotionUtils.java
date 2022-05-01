package bassebombecraft.potion;

import static bassebombecraft.ModConstants.POTIONS_CONFIGPATH;
import static bassebombecraft.potion.effect.RegisteredEffects.AMPLIFIER_EFFECT;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static net.minecraft.world.item.Items.POTION;
import static net.minecraftforge.common.brewing.BrewingRecipeRegistry.addRecipe;

import java.util.Collection;
import java.util.Optional;

import bassebombecraft.config.ConfigUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fmllegacy.RegistryObject;

/**
 * Utility for potions.
 */
public class PotionUtils {

	/**
	 * Potion factory method.
	 * 
	 * @param name   name of potion, used for registration.
	 * @param effect potion effect.
	 * 
	 * @return potion ready for registration.
	 */
	public static Potion getInstance(String name, MobEffect effect) {

		// calculate path in TOML configuration.
		String configPath = POTIONS_CONFIGPATH + name;

		// get configuration
		int duration = ConfigUtils.getInt(configPath + ".duration");
		int amplifier = ConfigUtils.getInt(configPath + ".amplifier");

		// create effect instance and potion
		MobEffectInstance effectInstance = new MobEffectInstance(effect, duration, amplifier);
		return new Potion(effectInstance);
	}

	/**
	 * Register potion with the Forge {@linkplain BrewingRecipeRegistry}.
	 * 
	 * @param basePotion base potion to create potion from.
	 * @param reagent    reagent in potion.
	 * @param splPotion  function to resolve potion.
	 */
	public static void registerPotionRecipe(Potion basePotion, Item reagent, RegistryObject<Potion> splPotion) {
		Potion targetPotion = splPotion.get();
		Ingredient baseItem = Ingredient.of(setPotion(new ItemStack(POTION), basePotion));
		Ingredient reagantItem = Ingredient.of(new ItemStack(reagent));
		ItemStack out = setPotion(new ItemStack(Items.POTION), targetPotion);
		addRecipe(new BrewingRecipe(baseItem, reagantItem, out));
	}

	/**
	 * Register potion with the Forge {@linkplain BrewingRecipeRegistry}.
	 * 
	 * @param splBasePotion function to resolve base potion to create potion from.
	 * @param reagent       reagent in potion.
	 * @param splPotion     function to resolve potion.
	 */
	public static void registerPotionRecipe(RegistryObject<Potion> splBasePotion, Item reagent,
			RegistryObject<Potion> splPotion) {
		Potion basePotion = splBasePotion.get();
		registerPotionRecipe(basePotion, reagent, splPotion);
	}

	/**
	 * Returns effect, if potion effect is active on living entity.
	 * 
	 * @param entity the entity to test.
	 * @param effect the effect to test for.
	 * @return {@linkplain Optional} containing the effect if it is active.
	 *         Otherwise the optional is empty.
	 */
	public static Optional<EffectInstance> getEffectIfActive(LivingEntity entity, Effect effect) {
		if (entity == null)
			return empty();
		return ofNullable(entity.getEffect(effect));
	}

	/**
	 * Returns true if any of amplifier effect is active on living entity.
	 * 
	 * @param entity the entity to test.
	 * @param effect the effect to test for.
	 * 
	 * @return true if effect is active on living entity.
	 */
	public static boolean isAmplifierEffectActive(LivingEntity entity) {
		Optional<EffectInstance> optEffect = getEffectIfActive(entity, AMPLIFIER_EFFECT.get());
		return optEffect.isPresent();
	}

	/**
	 * Resolves first effect.
	 * 
	 * @param entity to resolve effects from.
	 * 
	 * @return {@linkplain Optional} first effect. Otherwise the optional is empty.
	 */
	public static Optional<EffectInstance> resolveFirstEffect(LivingEntity entity) {
		Collection<EffectInstance> effects = entity.getActiveEffects();
		return effects.stream().findFirst();
	}

}
