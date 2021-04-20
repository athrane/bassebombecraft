package bassebombecraft.sound;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.DeferredRegister.create;
import static net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Registry objects for registered sounds.
 * 
 * Sound ID's are registered in
 * src/main/resources/assets/bassebombecraft/sound.json.
 */
public class RegisteredSounds {

	/**
	 * Deferred registry for registration of sounds.
	 */
	public static final DeferredRegister<SoundEvent> SOUNDS_REGISTRY = create(SOUND_EVENTS, MODID);

	/**
	 * Registered sounds.
	 * 
	 * Defined in sounds.json
	 */
	public static final RegistryObject<SoundEvent> DEFAULT_SOUND = register("default_sound");
	public static final RegistryObject<SoundEvent> SHOOT_SKULL_PROJECTILE_SOUND = register("item.shoot_skull_projectile");
	public static final RegistryObject<SoundEvent> TELEPORT_INVOKER_SOUND = register("operator.teleport_invoker");

	/**
	 * Register sound.
	 * 
	 * @param key registry object name.
	 * 
	 * @return registry object.
	 */
	static RegistryObject<SoundEvent> register(String key) {
		ResourceLocation location = new ResourceLocation(MODID, key);
		SoundEvent soundEvent = new SoundEvent(location);
		Supplier<SoundEvent> splSound = () -> soundEvent;
		return SOUNDS_REGISTRY.register(key, splSound);
	}

}
