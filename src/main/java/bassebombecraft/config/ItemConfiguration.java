package bassebombecraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining item information in configuration files.
 */
public class ItemConfiguration {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;

	/**
	 * ItemConfiguration constructor.
	 * 
	 * @param builder configuration spec builder.
	 * @param name book name.
	 * @param tooltip book tooltip.
	 * @param cooldown book cooldown.
	 */
	public ItemConfiguration(Builder builder, String name, String tooltip, int cooldown) {
		builder.comment(name + " settings").push("Books." + name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		builder.pop();
	}

	/**
	 * ItemConfiguration factory method.
	 * 
	 * @param builder configuration spec builder.
	 * @param name book name.
	 * @param tooltip book tooltip.
	 * @param cooldown book cooldown.
	 */
	public static ItemConfiguration getInstance(Builder builder, String name, String tooltip, int cooldown) {
		return new ItemConfiguration(builder, name, tooltip, cooldown);
	}

}
