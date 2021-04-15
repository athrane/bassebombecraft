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
	 * Deferred registry for registration of sounds.
	 */
	public static final DeferredRegister<Effect> POTION_REGISTRY = create(POTIONS, MODID);

	/**
	 * Registered Effects.
	 */
	public static final RegistryObject<Effect> AGGRO_MOB_EFFECT = register(AggroMobEffect::new);
	public static final RegistryObject<Effect> RECEIVE_AGGRO_EFFECT = register(ReceiveAggroEffect::new);
	public static final RegistryObject<Effect> AGGRO_PLAYER_EFFECT = register(AggroPlayerEffect::new);
	public static final RegistryObject<Effect> MOB_PRIMING_EFFECT = register(MobPrimingEffect::new);
	public static final RegistryObject<Effect> BACON_BAZOOKA_EFFECT = register(BaconBazookaEffect::new);
	public static final RegistryObject<Effect> BEAR_BLASTER_EFFECT = register(BearBlasterEffect::new);
	public static final RegistryObject<Effect> CREEPER_CANNON_EFFECT = register(CreeperCannonEffect::new);
	public static final RegistryObject<Effect> PRIMED_CREEPER_CANNON_EFFECT = register(PrimedCreeperCannonEffect::new);
	public static final RegistryObject<Effect> AMPLIFIER_EFFECT = register(AmplifierEffect::new);
	public static final RegistryObject<Effect> REFLECT_EFFECT = register(ReflectEffect::new);
	public static final RegistryObject<Effect> INCREASE_SIZE_EFFECT = register(IncreaseSizeEffect::new);
	public static final RegistryObject<Effect> DECREASE_SIZE_EFFECT = register(DecreaseSizeEffect::new);

	/**
	 * Register item.
	 * 
	 * @param splItem supplier used to create item instance..
	 * 
	 * @return registry object.
	 */
	static RegistryObject<Effect> register(Supplier<Effect> splEffect) {
		String key = splEffect.get().getClass().getSimpleName().toLowerCase();
		return POTION_REGISTRY.register(key, splEffect);
	}
}
