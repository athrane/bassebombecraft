package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.POTIONS;

import java.util.function.Supplier;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered potion effects.
 */
public class RegisteredEffects {

	/**
	 * Deferred registry for registration of effects.
	 */
	public static final DeferredRegister<Effect> EFFECT_REGISTRY = create(POTIONS, MODID);

	/**
	 * Registered effects.
	 */
	public static final RegistryObject<Effect> AGGRO_MOB_EFFECT = register(AggroMobEffect.NAME, AggroMobEffect::new);
	public static final RegistryObject<Effect> RECEIVE_AGGRO_EFFECT = register(ReceiveAggroEffect.NAME,ReceiveAggroEffect::new);
	public static final RegistryObject<Effect> AGGRO_PLAYER_EFFECT = register(AggroPlayerEffect.NAME, AggroPlayerEffect::new);
	public static final RegistryObject<Effect> MOB_PRIMING_EFFECT = register(MobPrimingEffect.NAME, MobPrimingEffect::new);
	public static final RegistryObject<Effect> BACON_BAZOOKA_EFFECT = register(BaconBazookaEffect.NAME, BaconBazookaEffect::new);
	public static final RegistryObject<Effect> BEAR_BLASTER_EFFECT = register(BearBlasterEffect.NAME, BearBlasterEffect::new);
	public static final RegistryObject<Effect> CREEPER_CANNON_EFFECT = register(CreeperCannonEffect.NAME, CreeperCannonEffect::new);
	public static final RegistryObject<Effect> PRIMED_CREEPER_CANNON_EFFECT = register(PrimedCreeperCannonEffect.NAME, PrimedCreeperCannonEffect::new);
	public static final RegistryObject<Effect> AMPLIFIER_EFFECT = register(AmplifierEffect.NAME, AmplifierEffect::new);
	public static final RegistryObject<Effect> REFLECT_EFFECT = register(ReflectEffect.NAME, ReflectEffect::new);
	public static final RegistryObject<Effect> INCREASE_SIZE_EFFECT = register(IncreaseSizeEffect.NAME, IncreaseSizeEffect::new);
	public static final RegistryObject<Effect> DECREASE_SIZE_EFFECT = register(DecreaseSizeEffect.NAME, DecreaseSizeEffect::new);
	public static final RegistryObject<Effect> WILDFIRE_EFFECT = register(WildfireEffect.NAME, WildfireEffect::new);

	/**
	 * Register object.
	 * 
	 * @param key registry object name.
	 * @param splEffect supplier used to create effect instance.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<Effect> register(String key, Supplier<Effect> splEffect) {
		return EFFECT_REGISTRY.register(key.toLowerCase(), splEffect);
	}
		
}

