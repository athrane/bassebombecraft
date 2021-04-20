package bassebombecraft.config;

import static bassebombecraft.sound.RegisteredSounds.DEFAULT_SOUND;

import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

/**
 * Class for defining item information in configuration files.
 */
public class ItemConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;
	public SoundConfig sound;

	/**
	 * Constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     item name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param splSound {@linkplain SoundConfig} supplier.
	 */
	public ItemConfig(Builder builder, String name, String tooltip, int cooldown, Supplier<SoundConfig> splSound) {
		builder.comment(name + " settings").push(name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		this.sound = splSound.get();
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
	 * Factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     book name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * 
	 * @return item configuration
	 */
	public static ItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown) {
		return new ItemConfig(builder, name, tooltip, cooldown, () -> new SoundConfig(builder, DEFAULT_SOUND));
	}

	/**
	 * Factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     book name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param splSound {@linkplain SoundConfig} supplier.
	 * 
	 * @return item configuration
	 */
	public static ItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown,
			Supplier<SoundConfig> splSound) {
		return new ItemConfig(builder, name, tooltip, cooldown, splSound);
	}

}
