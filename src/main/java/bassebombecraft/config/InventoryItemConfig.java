package bassebombecraft.config;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.ModConstants.NOT_AN_AOE_EFFECT;
import static bassebombecraft.sound.RegisteredSounds.DEFAULT_SOUND;
import static net.minecraftforge.registries.ForgeRegistries.SOUND_EVENTS;

import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

/**
 * Class for defining inventory item information in configuration files.
 */
public class InventoryItemConfig {

	public ForgeConfigSpec.ConfigValue<String> tooltip;
	public ForgeConfigSpec.IntValue cooldown;
	public ForgeConfigSpec.IntValue range;
	public ParticlesConfig particles;
	public SoundConfig sound;

	/**
	 * InventoryItemConfig constructor.
	 * 
	 * Doesn't add any subsections.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param tooltip      item tooltip.
	 * @param cooldown     item cooldown.
	 * @param range        activation range.
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 * @param splSound     {@linkplain SoundConfig} supplier.
	 */
	public InventoryItemConfig(Builder builder, String name, String tooltip, int cooldown, int range,
			Supplier<ParticlesConfig> splParticles, Supplier<SoundConfig> splSound) {
		builder.comment(name + " settings").push(name);
		this.tooltip = builder.comment("Tooltip for item.").define("tooltip", tooltip);
		this.cooldown = builder.comment("Game ticks between item activation.").defineInRange("cooldown", cooldown, 0,
				Integer.MAX_VALUE);
		this.range = builder.comment("Item activation range in blocks. 1 = Not an AOE effect. ").defineInRange("range",
				range, 0, Integer.MAX_VALUE);
		this.particles = splParticles.get();
		this.sound = splSound.get();
		builder.pop();
	}

	/**
	 * Get function to resolve sound.
	 * 
	 * @return function to resolve sound.
	 */
	public Supplier<SoundEvent> getSplSound() {
		return sound.getSplSound();
	}
	
	/**
	 * Factory method.
	 * 
	 * Item is defined to use default sound.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param tooltip      item tooltip.
	 * @param cooldown     item cooldown.
	 * @param range        activation range.
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 */
	public static InventoryItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown, int range,
			Supplier<ParticlesConfig> splParticles) {
		return new InventoryItemConfig(builder, name, tooltip, cooldown, range, splParticles,
				() -> new SoundConfig(builder, DEFAULT_SOUND));
	}

	/**
	 * Factory method.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param tooltip      item tooltip.
	 * @param cooldown     item cooldown.
	 * @param range        activation range.
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 * @param splSound     {@linkplain SoundConfig} supplier.
	 */
	public static InventoryItemConfig getInstance(Builder builder, String name, String tooltip, int cooldown, int range,
			Supplier<ParticlesConfig> splParticles, Supplier<SoundConfig> splSound) {
		return new InventoryItemConfig(builder, name, tooltip, cooldown, range, splParticles, splSound);
	}

	/**
	 * Factory method.
	 * 
	 * No range argument is defined. Will be set to 1. Which is interpreted as not
	 * an AOE effect.
	 * 
	 * Item is defined to use default sound.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param tooltip      item tooltip.
	 * @param cooldown     item cooldown.
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 */
	public static InventoryItemConfig getInstanceWithNoRange(Builder builder, String name, String tooltip, int cooldown,
			Supplier<ParticlesConfig> splParticles) {
		return new InventoryItemConfig(builder, name, tooltip, cooldown, NOT_AN_AOE_EFFECT, splParticles,
				() -> new SoundConfig(builder, DEFAULT_SOUND));
	}

	/**
	 * Factory method.
	 * 
	 * No range argument is defined. Will be set to 1. Which is interpreted as not
	 * an AOE effect.
	 * 
	 * @param builder      configuration spec builder.
	 * @param name         item name.
	 * @param tooltip      item tooltip.
	 * @param cooldown     item cooldown.
	 * @param splParticles {@linkplain ParticlesConfig} supplier.
	 * @param splSound     {@linkplain SoundConfig} supplier.
	 */
	public static InventoryItemConfig getInstanceWithNoRange(Builder builder, String name, String tooltip, int cooldown,
			Supplier<ParticlesConfig> splParticles, Supplier<SoundConfig> splSound) {
		return new InventoryItemConfig(builder, name, tooltip, cooldown, NOT_AN_AOE_EFFECT, splParticles, splSound);
	}
	
}
