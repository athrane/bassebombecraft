package bassebombecraft.potion;

import static bassebombecraft.ModConstants.AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_AGGRO_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_PRIMING_POTION_NAME;
import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.SUPERIOR_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.WEAK_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.potion.PotionUtils.getInstance;
import static bassebombecraft.potion.effect.RegisteredEffects.AGGRO_MOB_EFFECT;
import static bassebombecraft.potion.effect.RegisteredEffects.AMPLIFIER_EFFECT;
import static bassebombecraft.potion.effect.RegisteredEffects.MOB_PRIMING_EFFECT;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.POTION_TYPES;

import java.util.function.Supplier;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered potion.
 */
public class RegisteredPotions {

	/**
	 * Deferred registry for registration of potions.
	 */
	public static final DeferredRegister<Potion> POTION_REGISTRY = create(POTION_TYPES, MODID);

	/**
	 * Registered potions.
	 */
	public static final RegistryObject<Potion> WEAK_AMPLIFICATION_POTION = register(WEAK_AMPLIFICATION_POTION_NAME, AMPLIFIER_EFFECT);				
	public static final RegistryObject<Potion> AMPLIFICATION_POTION = register(AMPLIFICATION_POTION_NAME, AMPLIFIER_EFFECT);				
	public static final RegistryObject<Potion> SUPERIOR_AMPLIFICATION_POTION = register(SUPERIOR_AMPLIFICATION_POTION_NAME, AMPLIFIER_EFFECT);				
	public static final RegistryObject<Potion> MOB_AGGRO_POTION = register(MOB_AGGRO_POTION_NAME, AGGRO_MOB_EFFECT);				
	public static final RegistryObject<Potion> MOB_PRIMING_POTION = register(MOB_PRIMING_POTION_NAME, MOB_PRIMING_EFFECT);				

	/**
	 * Register potion.
	 * 
	 * @param key              registry object name.
	 * @param splEffect        function to resolve potion effect.
	 * 
	 * @return registry object.
	 */	
	static RegistryObject<Potion> register(String key, RegistryObject<Effect> splEffect) {
		Supplier<Potion> splPotion = () -> getInstance(key, splEffect.get());
		return POTION_REGISTRY.register(key.toLowerCase(), splPotion);
	}
	
}
