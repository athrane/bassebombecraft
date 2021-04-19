package bassebombecraft.potion;

import static bassebombecraft.ModConstants.POTIONS_CONFIGPATH;
import static bassebombecraft.potion.effect.RegisteredEffects.AMPLIFIER_EFFECT;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

import java.util.Optional;

import bassebombecraft.config.ConfigUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

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
	public static Potion getInstance(String name, Effect effect) {

		// calculate path in TOML configuration.
		String configPath = POTIONS_CONFIGPATH + name;
		
		// get configuration
		int duration = ConfigUtils.getInt(configPath  + ".duration");
		int amplifier = ConfigUtils.getInt(configPath + ".amplifier");

		// create effect instance and potion
		EffectInstance effectInstance = new EffectInstance(effect, duration, amplifier);
		return new Potion(effectInstance);
	}
	
	/**
	 * Register potion with the Forge {@linkplain BrewingRecipeRegistry}.
	 * 
	 * @param basePotion   base potion to create potion form.
	 * @param reagent      reagent in potion.
	 * @param targetPotion target potion.
	 */
	public static void registerPotionRecipe(Potion basePotion, Item reagent, Potion targetPotion) {
		Ingredient baseItem = Ingredient.fromStacks(addPotionToItemStack(new ItemStack(Items.POTION), basePotion));
		Ingredient reagantItem = Ingredient.fromStacks(new ItemStack(reagent));
		ItemStack out = addPotionToItemStack(new ItemStack(Items.POTION), targetPotion);
		BrewingRecipeRegistry.addRecipe(new BrewingRecipe(baseItem, reagantItem, out));
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
		Optional<EffectInstance> optEffect = ofNullable(entity.getActivePotionEffect(effect));
		return optEffect;
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

}
