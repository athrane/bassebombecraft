package bassebombecraft.config;

import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;;

/**
 * Class for defining inventory item information in configuration files.
 */
public class InventoryItemConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;
	public ForgeConfigSpec.IntValue range;
	public ParticlesConfig particles;

	/**
	 * InventoryItemConfig constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     item name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param range    activation range.
	 * @param supplier {@linkplain ParticlesConfig} supplier.
	 */
	public InventoryItemConfig(Builder builder, String name, String tooltip, int cooldown, int range,
			Supplier<ParticlesConfig> supplier) {
		builder.comment(name + " settings").push(name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		this.range = builder.comment("Item activation range in blocks.").defineInRange("range", range, 0,
				Integer.MAX_VALUE);
		this.particles = supplier.get();
		builder.pop();
	}

	/**
	 * InventoryItemConfig factory method.
	 * 
	 * @param builder  configuration spec builder.
	 * @param name     book name.
	 * @param tooltip  book tooltip.
	 * @param cooldown book cooldown.
	 * @param range    activation range.
	 * @param supplier {@linkplain ParticlesConfig} supplier.
	 */
	public static InventoryItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown, int range,
			Supplier<ParticlesConfig> supplier) {
		return new InventoryItemConfig(builder, name, tooltip, cooldown, range, supplier);
	}

}
