package bassebombecraft.potion;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

import bassebombecraft.config.ConfigUtils;
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
	 * Do common initialisation of potion.
	 * 
	 * Name is stored with lower case in the potion registry.
	 * 
	 * @param potion potion which is initialised.
	 * @param name   potion name.
	 */
	public static void doCommonPotionInitialization(Potion potion, String name) {
		potion.setRegistryName(name.toLowerCase());
	}

	/**
	 * Do common initialisation of potion effect.
	 * 
	 * Name is stored with lower case in the effect registry.
	 * 
	 * @param effect effect which is initialised.
	 * @param name   effect name.
	 */
	public static void doCommonEffectInitialization(Effect effect, String name) {
		effect.setRegistryName(MODID, name.toLowerCase());
	}

	/**
	 * Potion factory method.
	 * 
	 * @param name   name of potion, used for registration.
	 * @param path   configuration path in TOML configuration.
	 * @param effect potion effect.
	 * 
	 * @return potion ready for registration.
	 */
	public static Potion getInstance(String name, String path, Effect effect) {

		// get configuration
		int duration = ConfigUtils.getInt(path + ".duration");
		int amplifier = ConfigUtils.getInt(path + ".amplifier");

		// create effect instance
		EffectInstance effectInstance = new EffectInstance(effect, duration, amplifier);

		// create potion
		String registryName = name.toLowerCase();
		Potion potion = new Potion(name, effectInstance);
		potion.setRegistryName(MODID, registryName);
		return potion;
	}

	/**
	 * REgister potion with the Forge {@linkplain BrewingRecipeRegistry}.
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

}
