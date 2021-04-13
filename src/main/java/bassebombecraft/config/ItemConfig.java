package bassebombecraft.config;

import static bassebombecraft.sound.RegisteredSounds.DEFAULT;

import java.util.function.Supplier;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.RegistryObject;;

/**
 * Class for defining item information in configuration files.
 */
public class ItemConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;
	public ForgeConfigSpec.ConfigValue<String> sound;
	RegistryObject<SoundEvent> soundResolved;

	/**
	 * ItemConfig constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     item name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param sound    sound when using item.
	 */
	public ItemConfig(Builder builder, String name, String tooltip, int cooldown, RegistryObject<SoundEvent> sound) {
		builder.comment(name + " settings").push(name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		this.soundResolved = sound;
		String soundAsString = sound.getId().getPath();
		this.sound = builder.comment(
				"Sound when using item. The names of the legal sounds are defined in bassebombecraft.sound.RegisteredSounds.")
				.define("sound", soundAsString);
		builder.pop();
	}

	/**
	 * Get item tooltip.
	 * 
	 * @return item tooltip.
	 */
	public String getToolTip() {
		return tooltip.get();
	}

	/**
	 * Get item cooldown.
	 * 
	 * @return item cooldown.
	 */
	int getCoolDown() {
		return cooldown.get();
	}

	/**
	 * Get item sound.
	 * 
	 * @return item sound.
	 */
	public Supplier<SoundEvent> splGetSound() {
		return () -> soundResolved.get();
	}

	/**
	 * ItemConfig factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     book name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 */
	public static ItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown) {
		return new ItemConfig(builder, name, tooltip, cooldown, DEFAULT);
	}

	/**
	 * ItemConfig factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     book name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param sound    sound when using item.
	 * 
	 */
	public static ItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown,
			RegistryObject<SoundEvent> sound) {
		return new ItemConfig(builder, name, tooltip, cooldown, sound);
	}

}
