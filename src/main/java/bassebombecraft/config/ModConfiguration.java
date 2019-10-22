package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.INTERNAL_TOML_CONFIG_FILE_NAME;
import static bassebombecraft.ModConstants.ITEM_BASICITEM_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.ModConstants.MOB_AGGRO_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_PRIMING_POTION_NAME;
import static bassebombecraft.ModConstants.SUPERIOR_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.WEAK_AMPLIFICATION_POTION_NAME;
import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

import java.nio.file.Path;

import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.potion.effect.MobAggroEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;

/**
 * Configuration class for the mod.
 */
public class ModConfiguration {

	/**
	 * General category.
	 */
	static final String CATEGORY_GENERAL = "General";

	/**
	 * Basic item category.
	 */
	static final String CATEGORY_BASIC_ITEMS = "BasicItems";

	/**
	 * Potion category.
	 */
	static final String CATEGORY_POTIONS = "Potions";

	/**
	 * Potion effect category.
	 */
	static final String CATEGORY_POTION_EFFECT = "PotionEffects";

	/**
	 * Common configuration builder.
	 */
	static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

	/**
	 * Common configuration for the mod.
	 */
	public static ForgeConfigSpec COMMON_CONFIG;

	// Basic item properties
	public static ForgeConfigSpec.IntValue basicItemDefaultCooldown;
	public static ForgeConfigSpec.ConfigValue<String> basicItemDefaultTooltip;

	// HudItem properties
	public static ForgeConfigSpec.ConfigValue<String> hudItemTooltip;
	public static ForgeConfigSpec.IntValue hudItemCooldown;

	// TerminatorEyeItem
	public static ForgeConfigSpec.ConfigValue<String> terminatorEyeItemTooltip;
	public static ForgeConfigSpec.IntValue terminatorEyeItemCooldown;

	// AmplificationPotion
	public static ForgeConfigSpec.IntValue amplificationPotionAmplifier;
	public static ForgeConfigSpec.IntValue amplificationPotionDuration;

	// SuperiorAmplificationPotion
	public static ForgeConfigSpec.IntValue SuperioramplificationPotionAmplifier;
	public static ForgeConfigSpec.IntValue SuperioramplificationPotionDuration;

	// MobAggroPotion
	public static ForgeConfigSpec.IntValue mobAggroPotionAmplifier;
	public static ForgeConfigSpec.IntValue mobAggroPotionDuration;

	// PrimedMobPotion
	public static ForgeConfigSpec.IntValue mobPrimingPotionAmplifier;
	public static ForgeConfigSpec.IntValue mobPrimingPotionDuration;

	// MobPrimingEffect
	public static ForgeConfigSpec.IntValue mobPrimingEffectCountdown;

	// MobPrimingEffect
	public static ForgeConfigSpec.IntValue mobAggroEffectAreaOfEffect;

	static {

		// build general section
		COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();

		// build basic items
		COMMON_BUILDER.comment("Basic item settings").push(CATEGORY_BASIC_ITEMS);
		setupBasicItemsGeneralConfig();
		setupBasicItemsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion effect settings").push(CATEGORY_POTION_EFFECT);
		setupPotionEffectsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion settings").push(CATEGORY_POTIONS);
		setupPotionsConfig();
		COMMON_BUILDER.pop();

		// do build
		COMMON_CONFIG = COMMON_BUILDER.build();
	}

	/**
	 * Define general settings for basic items.
	 */
	static void setupBasicItemsGeneralConfig() {
		basicItemDefaultCooldown = COMMON_BUILDER
				.comment("Game ticks between item activation. Default value for basic items.")
				.defineInRange("cooldown", ITEM_BASICITEM_DEFAULT_COOLDOWN, 0, Integer.MAX_VALUE);
		basicItemDefaultTooltip = COMMON_BUILDER.comment("Default tooltip for basic items.").define("tooltip",
				ITEM_DEFAULT_TOOLTIP);
	}

	/**
	 * Define configuration for basic items.
	 */
	static void setupBasicItemsConfig() {

		String terminatorEyeItemName = TerminatorEyeItem.NAME;
		COMMON_BUILDER.comment(terminatorEyeItemName + " settings").push(terminatorEyeItemName);
		terminatorEyeItemTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"An eye ripped from a T-500. Occasionally it tries to focus to acquire a target. Do not eat.");
		terminatorEyeItemCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		String hudItemName = HudItem.NAME;
		COMMON_BUILDER.comment(hudItemName + " settings").push(hudItemName);
		hudItemTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"BasseBombeCraft tactical HUD. Add to hotbar to activate.");
		hudItemCooldown = COMMON_BUILDER.comment("Game ticks between item activation.").defineInRange("cooldown", 10, 0,
				Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for potion effects.
	 */
	static void setupPotionEffectsConfig() {

		// mob priming effect
		String mobPrimingEffectName = MobPrimingEffect.NAME;
		COMMON_BUILDER.comment(mobPrimingEffectName + " settings").push(mobPrimingEffectName);
		mobPrimingEffectCountdown = COMMON_BUILDER.comment("Countdown of the effect in game ticks.")
				.defineInRange("countdown", 60, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob aggro effect
		String mobAggroEffectName = MobAggroEffect.NAME;
		COMMON_BUILDER.comment(mobAggroEffectName + " settings").push(mobAggroEffectName);
		mobAggroEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.").defineInRange("areaOfEffect",
				10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for potions.
	 */
	static void setupPotionsConfig() {

		// weak amplification potion
		String weakAmplificationPotionName = WEAK_AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(weakAmplificationPotionName + " settings").push(weakAmplificationPotionName);
		amplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.").defineInRange("amplifier", 16,
				0, Integer.MAX_VALUE);
		amplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// amplification potion
		String amplificationPotionName = AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(amplificationPotionName + " settings").push(amplificationPotionName);
		amplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.").defineInRange("amplifier", 64,
				0, Integer.MAX_VALUE);
		amplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// superior amplification potion
		String superiorAmplificationPotionName = SUPERIOR_AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(superiorAmplificationPotionName + " settings").push(superiorAmplificationPotionName);
		amplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.").defineInRange("amplifier", 128,
				0, Integer.MAX_VALUE);
		amplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob aggro potion
		String mobAggroPotionName = MOB_AGGRO_POTION_NAME;
		COMMON_BUILDER.comment(mobAggroPotionName + " settings").push(mobAggroPotionName);
		mobAggroPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion. Not used by potion.")
				.defineInRange("amplifier", 0, 0, Integer.MAX_VALUE);
		mobAggroPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob priming potion
		String mobPrimingPotionName = MOB_PRIMING_POTION_NAME;
		COMMON_BUILDER.comment(mobPrimingPotionName + " settings").push(mobPrimingPotionName);
		mobPrimingPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion, i.e. the resulting explosion.")
				.defineInRange("amplifier", 10, 0, Integer.MAX_VALUE);
		mobPrimingPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Load configuration using forge.
	 */
	public static void loadConfig() {
		ModLoadingContext.get().registerConfig(Type.COMMON, COMMON_CONFIG);
		Path path = CONFIGDIR.get().resolve(INTERNAL_TOML_CONFIG_FILE_NAME);

		// log path
		Logger logger = getBassebombeCraft().getLogger();
		logger.info("Loading configuration file from: " + path);

		// load configuration
		CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
				.writingMode(WritingMode.REPLACE).build();
		configData.load();
		COMMON_CONFIG.setConfig(configData);
	}

}
