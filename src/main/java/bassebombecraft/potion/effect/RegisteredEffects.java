package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.MOB_EFFECTS;

import java.util.function.Supplier;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered potion effects.
 */
public class RegisteredEffects {

	/**
	 * Deferred registry for registration of potion effects.
	 */
	public static final DeferredRegister<MobEffect> EFFECT_REGISTRY = create(MOB_EFFECTS, MODID);

	/**
	 * Registered effects.
	 */
	public static final RegistryObject<MobEffect> AGGRO_MOB_EFFECT = register(AggroMobEffect.NAME, AggroMobEffect::new);
	public static final RegistryObject<MobEffect> RECEIVE_AGGRO_EFFECT = register(ReceiveAggroEffect.NAME,
			ReceiveAggroEffect::new);
	public static final RegistryObject<MobEffect> AGGRO_PLAYER_EFFECT = register(AggroPlayerEffect.NAME,
			AggroPlayerEffect::new);
	public static final RegistryObject<MobEffect> MOB_PRIMING_EFFECT = register(MobPrimingEffect.NAME,
			MobPrimingEffect::new);
	public static final RegistryObject<MobEffect> BACON_BAZOOKA_EFFECT = register(BaconBazookaEffect.NAME,
			BaconBazookaEffect::new);
	public static final RegistryObject<MobEffect> BEAR_BLASTER_EFFECT = register(BearBlasterEffect.NAME,
			BearBlasterEffect::new);
	public static final RegistryObject<MobEffect> CREEPER_CANNON_EFFECT = register(CreeperCannonEffect.NAME,
			CreeperCannonEffect::new);
	public static final RegistryObject<MobEffect> PRIMED_CREEPER_CANNON_EFFECT = register(
			PrimedCreeperCannonEffect.NAME, PrimedCreeperCannonEffect::new);
	public static final RegistryObject<MobEffect> AMPLIFIER_EFFECT = register(AmplifierEffect.NAME,
			AmplifierEffect::new);
	public static final RegistryObject<MobEffect> REFLECT_EFFECT = register(ReflectEffect.NAME, ReflectEffect::new);
	public static final RegistryObject<MobEffect> INCREASE_SIZE_EFFECT = register(IncreaseSizeEffect.NAME,
			IncreaseSizeEffect::new);
	public static final RegistryObject<MobEffect> DECREASE_SIZE_EFFECT = register(DecreaseSizeEffect.NAME,
			DecreaseSizeEffect::new);
	public static final RegistryObject<MobEffect> WILDFIRE_EFFECT = register(WildfireEffect.NAME, WildfireEffect::new);
	public static final RegistryObject<MobEffect> CONTAGION_EFFECT = register(ContagionEffect.NAME,
			ContagionEffect::new);

	/**
	 * Register object.
	 * 
	 * @param key       registry object name.
	 * @param splEffect supplier used to create effect instance.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<MobEffect> register(String key, Supplier<MobEffect> splEffect) {
		return EFFECT_REGISTRY.register(key.toLowerCase(), splEffect);
	}

}
