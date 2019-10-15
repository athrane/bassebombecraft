package bassebombecraft.potion;

import static bassebombecraft.ModConstants.*;
import bassebombecraft.config.ConfigUtils;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;

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
		int amplifier = ConfigUtils.getInt(path  + ".amplifier");

		// create effect instance
		EffectInstance effectInstance = new EffectInstance(effect, duration, amplifier);

		// create potion
		String registryName = name.toLowerCase();
		Potion potion = new Potion(name, effectInstance);
		potion.setRegistryName(MODID, registryName);
		return potion;
	}

}
