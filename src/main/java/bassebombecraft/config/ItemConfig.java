package bassebombecraft.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining item information in configuration files.
 */
public class ItemConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;

	/**
	 * ItemConfig constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder configuration spec builder.
	 * @param name item name.
	 * @param tooltip book tooltip.
	 * @param cooldown book cooldown.
	 */
	public ItemConfig(Builder builder, String name, String tooltip, int cooldown) {
		builder.comment(name + " settings").push(name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		builder.pop();
	}

	/**
	 * ItemConfig factory method.
	 * 
	 * @param builder configuration spec builder.
	 * @param name book name.
	 * @param tooltip book tooltip.
	 * @param cooldown book cooldown.
	 */
	public static ItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown) {
		return new ItemConfig(builder, name, tooltip, cooldown);
	}

}
