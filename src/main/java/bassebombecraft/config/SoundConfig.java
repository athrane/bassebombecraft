package bassebombecraft.config;

import static bassebombecraft.ModConstants.MODID;
import static net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fmllegacy.RegistryObject;;

/**
 * Class for defining sound information in configuration files.
 */
public class SoundConfig {

	public ForgeConfigSpec.ConfigValue<String> sound;

	/**
	 * Constructor.
	 * 
	 * Adds a subsection named ".Sound".
	 * 
	 * @param builder configuration spec builder.
	 * @param sound   configured sound.
	 */
	public SoundConfig(Builder builder, RegistryObject<SoundEvent> sound) {
		builder.comment("Sound settings").push("Sound");
		String soundAsString = sound.getId().getPath();
		this.sound = builder.comment("Sound. Legal sounds are defined in bassebombecraft.sound.RegisteredSounds.")
				.define("sound", soundAsString);
		builder.pop();
	}

	/**
	 * Get function to resolve sound.
	 * 
	 * @return function to resolve sound.
	 */
	public Supplier<SoundEvent> getSplSound() {
		return () -> SOUND_EVENTS.getValue(new ResourceLocation(MODID, sound.get()));
	}
	
	/**
	 * Factory method.
	 * 
	 * @param builder configuration spec builder.
	 * @param sound   configured sound.
	 * 
	 * @return sound configuration
	 */
	public static SoundConfig getInstance(Builder builder, RegistryObject<SoundEvent> sound) {
		return new SoundConfig(builder, sound);
	}

}
