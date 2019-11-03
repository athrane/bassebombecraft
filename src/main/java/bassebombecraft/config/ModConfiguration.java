package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.BACONBAZOOKA_EFFECT_NAME;
import static bassebombecraft.ModConstants.BEARBLASTER_EFFECT_NAME;
import static bassebombecraft.ModConstants.CREEPERCANNON_EFFECT_NAME;
import static bassebombecraft.ModConstants.INTERNAL_TOML_CONFIG_FILE_NAME;
import static bassebombecraft.ModConstants.ITEM_BASICITEM_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.ModConstants.MOB_AGGRO_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_PRIMING_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_RESPAWNING_POTION_NAME;
import static bassebombecraft.ModConstants.PRIMEDCREEPERCANNON_EFFECT_NAME;
import static bassebombecraft.ModConstants.SUPERIOR_AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.WEAK_AMPLIFICATION_POTION_NAME;
import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import bassebombecraft.entity.commander.command.AttackNearestMobCommand;
import bassebombecraft.entity.commander.command.AttackNearestPlayerCommand;
import bassebombecraft.entity.commander.command.DanceCommand;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.ShootSmallFireballRing;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
import bassebombecraft.item.book.BuildStairsBook;
import bassebombecraft.item.book.BuildTowerBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.DigMobHoleBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.SpawnCreeperArmyBook;
import bassebombecraft.item.book.SpawnKittenArmyBook;
import bassebombecraft.item.book.SpawnSkeletonArmyBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.MobAggroEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.MobRespawningEffect;
import bassebombecraft.potion.effect.PlayerAggroEffect;
import bassebombecraft.projectile.action.DigMobHole;
import bassebombecraft.projectile.action.SpawnCreeperArmy;
import bassebombecraft.projectile.action.SpawnKittenArmy;
import bassebombecraft.projectile.action.SpawnSkeletonArmy;
import bassebombecraft.projectile.action.SpawnStairs;
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
	 * Basic item category.
	 */
	static final String CATEGORY_BOOK_ITEMS = "Books";

	/**
	 * Potion category.
	 */
	static final String CATEGORY_POTIONS = "Potions";

	/**
	 * Potion effect category.
	 */
	static final String CATEGORY_POTION_EFFECT = "PotionEffects";

	/**
	 * Action effect category.
	 */
	static final String CATEGORY_ACTION = "Actions";

	/**
	 * Commander commands category.
	 */
	static final String CATEGORY_COMMANDS = "CommanderCommands";

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

	// Potions..

	// WeakAmplificationPotion
	public static ForgeConfigSpec.IntValue weakAmplificationPotionAmplifier;
	public static ForgeConfigSpec.IntValue weakAmplificationPotionDuration;

	// AmplificationPotion
	public static ForgeConfigSpec.IntValue amplificationPotionAmplifier;
	public static ForgeConfigSpec.IntValue amplificationPotionDuration;

	// SuperiorAmplificationPotion
	public static ForgeConfigSpec.IntValue superiorAmplificationPotionAmplifier;
	public static ForgeConfigSpec.IntValue superiorAmplificationPotionDuration;

	// MobAggroPotion
	public static ForgeConfigSpec.IntValue mobAggroPotionAmplifier;
	public static ForgeConfigSpec.IntValue mobAggroPotionDuration;

	// PrimedMobPotion
	public static ForgeConfigSpec.IntValue mobPrimingPotionAmplifier;
	public static ForgeConfigSpec.IntValue mobPrimingPotionDuration;

	// MobRespawnerPotion
	public static ForgeConfigSpec.IntValue mobRespawningPotionAmplifier;
	public static ForgeConfigSpec.IntValue mobRespawningPotionDuration;

	// Potion effects..

	// AmplifierEffect
	public static ForgeConfigSpec.IntValue amplifierEffectUpdateFrequency;

	// MobPrimingEffect
	public static ForgeConfigSpec.IntValue mobPrimingEffectCountdown;

	// MobAggroEffect
	public static ForgeConfigSpec.IntValue mobAggroEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue mobAggroEffectUpdateFrequency;

	// PlayerAggroEffect
	public static ForgeConfigSpec.IntValue playerAggroEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue playerAggroEffectDuration;
	public static ForgeConfigSpec.IntValue playerAggroEffectUpdateFrequency;

	// MobRespawningEffect
	public static ForgeConfigSpec.IntValue mobRespawningEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue mobRespawningEffectDuration;
	public static ForgeConfigSpec.IntValue mobRespawningEffectSpawnArea;
	
	// BaconBazookaProjectileEffect
	public static ForgeConfigSpec.IntValue baconBazookaProjectileEffectForce;
	public static ForgeConfigSpec.IntValue baconBazookaProjectileEffectExplosion;

	// BearBlasterProjectileEffect
	public static ForgeConfigSpec.IntValue bearBlasterProjectileEffectForce;
	public static ForgeConfigSpec.IntValue bearBlasterProjectileEffectExplosion;

	// CreeperCannonProjectileEffect
	public static ForgeConfigSpec.IntValue creeperCannonProjectileEffectForce;
	public static ForgeConfigSpec.IntValue creeperCannonProjectileEffectExplosion;

	// PrimedCreeperCannonProjectileEffect
	public static ForgeConfigSpec.IntValue primedCreeperCannonProjectileEffectForce;
	public static ForgeConfigSpec.IntValue primedCreeperCannonProjectileEffectExplosion;

	// Books..

	// MobCommandersBaton
	public static ForgeConfigSpec.ConfigValue<String> mobCommandersBatonTooltip;
	public static ForgeConfigSpec.IntValue mobCommandersBatonCooldown;

	// SmallFireballBook
	public static ForgeConfigSpec.ConfigValue<String> smallFireballBookTooltip;
	public static ForgeConfigSpec.IntValue smallFireballBookCooldown;

	// SmallFireballRingBook
	public static ForgeConfigSpec.ConfigValue<String> smallFireballRingBookTooltip;
	public static ForgeConfigSpec.IntValue smallFireballRingBookCooldown;

	// BaconBazookaBook
	public static ForgeConfigSpec.ConfigValue<String> baconBazookaBookTooltip;
	public static ForgeConfigSpec.IntValue baconBazookaBookCooldown;

	// BearBlasterBook
	public static ForgeConfigSpec.ConfigValue<String> bearBlasterBookTooltip;
	public static ForgeConfigSpec.IntValue bearBlasterBookCooldown;

	// CreeperCannonBook
	public static ForgeConfigSpec.ConfigValue<String> creeperCannonBookTooltip;
	public static ForgeConfigSpec.IntValue creeperCannonBookCooldown;

	// PrimedCreeperCannonBook
	public static ForgeConfigSpec.ConfigValue<String> primedCreeperCannonBookTooltip;
	public static ForgeConfigSpec.IntValue primedCreeperCannonBookCooldown;

	// TeleportBook
	public static ForgeConfigSpec.ConfigValue<String> teleportBookTooltip;
	public static ForgeConfigSpec.IntValue teleportBookCooldown;

	// SetSpawnPointBook
	public static ForgeConfigSpec.ConfigValue<String> setSpawnPointBookTooltip;
	public static ForgeConfigSpec.IntValue setSpawnPointBookCooldown;

	// DigMobHoleBook
	public static ForgeConfigSpec.ConfigValue<String> digMobHoleBookTooltip;
	public static ForgeConfigSpec.IntValue digMobHoleBookCooldown;

	// SpawnCreeperArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnCreeperArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnCreeperArmyBookCooldown;

	// SpawnSkeletonArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnSkeletonArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnSkeletonArmyBookCooldown;

	// SpawnKittenArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnKittenArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnKittenArmyBookCooldown;

	// BuildTowerBook
	public static ForgeConfigSpec.ConfigValue<String> buildTowerBookTooltip;
	public static ForgeConfigSpec.IntValue buildTowerBookCooldown;

	// BuildStairsBook
	public static ForgeConfigSpec.ConfigValue<String> buildStairsBookTooltip;
	public static ForgeConfigSpec.IntValue buildStairsBookCooldown;

	// Actions..

	// ShootFireballRing projectile action
	public static ForgeConfigSpec.IntValue shootSmallFireballRingFireballs;

	// ShootBaconBazooka projectile action
	public static ForgeConfigSpec.IntValue shootBaconBazookaProjectileAge;
	public static ForgeConfigSpec.IntValue shootBaconBazookaDuration;
	public static ForgeConfigSpec.IntValue shootBaconBazookaSpawnDisplacement;

	// ShootBearBlaster projectile action
	public static ForgeConfigSpec.IntValue shootBearBlasterProjectileAge;
	public static ForgeConfigSpec.IntValue shootBearBlasterDuration;
	public static ForgeConfigSpec.IntValue shootBearBlasterSpawnDisplacement;

	// ShootCreeperCannon projectile action
	public static ForgeConfigSpec.IntValue shootCreeperCannonDuration;
	public static ForgeConfigSpec.IntValue shootCreeperCannonSpawnDisplacement;

	// SpawnCreeperArmy projectile action
	public static ForgeConfigSpec.IntValue spawnCreeperArmyEntities;
	public static ForgeConfigSpec.IntValue spawnCreeperArmySpawnArea;

	// SpawnSkeletonArmy projectile action
	public static ForgeConfigSpec.IntValue spawnSkeletonArmyEntities;
	public static ForgeConfigSpec.IntValue spawnSkeletonArmySpawnArea;

	// SpawnKittenArmy projectile action
	public static ForgeConfigSpec.IntValue spawnKittenArmyEntities;
	public static ForgeConfigSpec.IntValue spawnKittenArmySpawnArea;
	public static ForgeConfigSpec.IntValue spawnKittenArmyAge;
	public static ForgeConfigSpec.ConfigValue<List<? extends String>> spawnKittenArmyNames;

	// DigMobHole projectile action
	public static ForgeConfigSpec.IntValue digMobHoleNoHitHoleDepth;
	public static ForgeConfigSpec.IntValue digMobHoleNoHitHoleHeight;
	public static ForgeConfigSpec.IntValue digMobHoleNoHitHoleWidth;
	public static ForgeConfigSpec.IntValue digMobHoleHeightExpansion;

	// SpawnStairs projectile action
	public static ForgeConfigSpec.IntValue spawnStairsDuration;

	// Commander commands..
	public static ForgeConfigSpec.DoubleValue danceCommandChance;
	public static ForgeConfigSpec.IntValue attackNearestMobCommandTargetDistance;
	public static ForgeConfigSpec.IntValue attackNearestPlayerCommandTargetDistance;

	static {

		// build general section
		COMMON_BUILDER.comment("General settings").push(CATEGORY_GENERAL);
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Basic item settings").push(CATEGORY_BASIC_ITEMS);
		setupBasicItemsGeneralConfig();
		setupBasicItemsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Commander commands ").push(CATEGORY_COMMANDS);
		setupCommandsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion effect settings").push(CATEGORY_POTION_EFFECT);
		setupPotionEffectsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion settings").push(CATEGORY_POTIONS);
		setupPotionsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Action settings").push(CATEGORY_ACTION);
		setupActionsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Book settings").push(CATEGORY_BOOK_ITEMS);
		setupBooksConfig();
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

		// terminator eye item
		String terminatorEyeItemName = TerminatorEyeItem.NAME;
		COMMON_BUILDER.comment(terminatorEyeItemName + " settings").push(terminatorEyeItemName);
		terminatorEyeItemTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"An eye ripped from a T-500. Occasionally it tries to focus to acquire a target. Do not eat.");
		terminatorEyeItemCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// HUD item
		String hudItemName = HudItem.NAME;
		COMMON_BUILDER.comment(hudItemName + " settings").push(hudItemName);
		hudItemTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"BasseBombeCraft tactical HUD. Add to hotbar to activate.");
		hudItemCooldown = COMMON_BUILDER.comment("Game ticks between item activation.").defineInRange("cooldown", 10, 0,
				Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for commander commands.
	 */
	static void setupCommandsConfig() {

		// Dance command
		String name = DanceCommand.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		danceCommandChance = COMMON_BUILDER.comment("Chance for mob to jump during dance.").defineInRange("jumpChance",
				0.75, 0, 1);
		COMMON_BUILDER.pop();

		// Attack nearest mob command
		name = AttackNearestMobCommand.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		attackNearestMobCommandTargetDistance = COMMON_BUILDER.comment("Distance within  the mob will aquire a target.")
				.defineInRange("targetDistance", 5, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Attack nearest mob command
		name = AttackNearestPlayerCommand.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		attackNearestPlayerCommandTargetDistance = COMMON_BUILDER
				.comment("Distance within  the mob will aquire a target.")
				.defineInRange("targetDistance", 5, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

	}

	/**
	 * Define configuration for potion effects.
	 */
	static void setupPotionEffectsConfig() {

		// amplifier effect
		String name = AmplifierEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		amplifierEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
				.defineInRange("updateFrequency", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob priming effect
		name = MobPrimingEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobPrimingEffectCountdown = COMMON_BUILDER.comment("Countdown of the effect in game ticks.")
				.defineInRange("countdown", 60, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob aggro effect
		name = MobAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobAggroEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.").defineInRange("areaOfEffect",
				10, 0, Integer.MAX_VALUE);
		mobAggroEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
				.defineInRange("updateFrequency", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob respawning effect
		name = MobRespawningEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobRespawningEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.").defineInRange("areaOfEffect",
				10, 0, Integer.MAX_VALUE);
		mobRespawningEffectDuration = COMMON_BUILDER.comment("Duration of effect (on aggro'ed mobs) in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		mobRespawningEffectSpawnArea = COMMON_BUILDER.comment("Spawn area in blocks.").defineInRange("spawnArea", 5, 0,
				Integer.MAX_VALUE);		
		COMMON_BUILDER.pop();

		// player aggro effect
		name = PlayerAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		playerAggroEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.")
				.defineInRange("areaOfEffect", 10, 0, Integer.MAX_VALUE);
		playerAggroEffectDuration = COMMON_BUILDER.comment("Duration of effect (on aggro'ed mobs) in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		playerAggroEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
				.defineInRange("updateFrequency", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// bacon bazooka projectile effect
		name = BACONBAZOOKA_EFFECT_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		baconBazookaProjectileEffectForce = COMMON_BUILDER.comment("Projectile force.").defineInRange("force", 3, 0,
				Integer.MAX_VALUE);
		baconBazookaProjectileEffectExplosion = COMMON_BUILDER.comment("Projectile impact explosion size.")
				.defineInRange("explosion", 2, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// bear blaster projectile effect
		name = BEARBLASTER_EFFECT_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		bearBlasterProjectileEffectForce = COMMON_BUILDER.comment("Projectile force.").defineInRange("force", 3, 0,
				Integer.MAX_VALUE);
		bearBlasterProjectileEffectExplosion = COMMON_BUILDER.comment("Projectile impact explosion size.")
				.defineInRange("explosion", 2, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// creeper cannon projectile effect
		name = CREEPERCANNON_EFFECT_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		creeperCannonProjectileEffectForce = COMMON_BUILDER.comment("Projectile force.").defineInRange("force", 2, 0,
				Integer.MAX_VALUE);
		creeperCannonProjectileEffectExplosion = COMMON_BUILDER.comment(
				"Projectile impact explosion size. Please notice: default creeper explosion radius is 3, powered creeper explosion radius is 6.")
				.defineInRange("explosion", 3, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// primed creeper cannon projectile effect
		name = PRIMEDCREEPERCANNON_EFFECT_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		primedCreeperCannonProjectileEffectForce = COMMON_BUILDER.comment("Projectile force.").defineInRange("force", 2,
				0, Integer.MAX_VALUE);
		primedCreeperCannonProjectileEffectExplosion = COMMON_BUILDER.comment(
				"Projectile impact explosion size. Please notice: default creeper explosion radius is 3, powered creeper explosion radius is 6.")
				.defineInRange("explosion", 6, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for potions.
	 */
	static void setupPotionsConfig() {

		// weak amplification potion
		String name = WEAK_AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		weakAmplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.").defineInRange("amplifier",
				15, 0, Integer.MAX_VALUE);
		weakAmplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// amplification potion
		name = AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		amplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.").defineInRange("amplifier", 63,
				0, Integer.MAX_VALUE);
		amplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// superior amplification potion
		name = SUPERIOR_AMPLIFICATION_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		superiorAmplificationPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion.")
				.defineInRange("amplifier", 127, 0, Integer.MAX_VALUE);
		superiorAmplificationPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob aggro potion
		name = MOB_AGGRO_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobAggroPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion. Not used by potion.")
				.defineInRange("amplifier", 0, 0, Integer.MAX_VALUE);
		mobAggroPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob priming potion
		name = MOB_PRIMING_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobPrimingPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion, i.e. the resulting explosion.")
				.defineInRange("amplifier", 10, 0, Integer.MAX_VALUE);
		mobPrimingPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// mob respawning potion
		name = MOB_RESPAWNING_POTION_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobRespawningPotionAmplifier = COMMON_BUILDER.comment("Potency of the potion, i.e. the number of spawned mobs.")
				.defineInRange("amplifier", 2, 0, Integer.MAX_VALUE);
		mobRespawningPotionDuration = COMMON_BUILDER.comment("Duration of the potion in game ticks.")
				.defineInRange("duration", 1200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for actions.
	 */
	static void setupActionsConfig() {

		// ShootSmallFireballRing
		String name = ShootSmallFireballRing.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		shootSmallFireballRingFireballs = COMMON_BUILDER.comment("Number of fireballs spawned in 360 degrees circle.")
				.defineInRange("number", 16, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// ShootBaconBazooka
		name = ShootBaconBazooka.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		shootBaconBazookaProjectileAge = COMMON_BUILDER.comment("Projectile entity age.").defineInRange("number", -1, 0,
				Integer.MAX_VALUE);
		shootBaconBazookaDuration = COMMON_BUILDER.comment("Projectile (which is a potion) duration in game ticks.")
				.defineInRange("duration", 20, 0, Integer.MAX_VALUE);
		shootBaconBazookaSpawnDisplacement = COMMON_BUILDER
				.comment("Projectile spawn displacement in blocks in front of the shooter.")
				.defineInRange("spawnDisplacement", 2, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// ShootBearBlaster
		name = ShootBearBlaster.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		shootBearBlasterProjectileAge = COMMON_BUILDER.comment("Projectile entity age.").defineInRange("number", -1, 0,
				Integer.MAX_VALUE);
		shootBearBlasterDuration = COMMON_BUILDER.comment("Projectile (which is a potion) duration in game ticks.")
				.defineInRange("duration", 20, 0, Integer.MAX_VALUE);
		shootBearBlasterSpawnDisplacement = COMMON_BUILDER
				.comment("Projectile spawn displacement in blocks in front of the shooter.")
				.defineInRange("spawnDisplacement", 2, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// ShootCreeperCannon
		name = ShootCreeperCannon.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		shootCreeperCannonDuration = COMMON_BUILDER.comment(
				"Projectile (which is a potion) duration in game ticks. Please notice: the general fuse time for creeper is 30, setting duration < 30, will disable the creeper explosion.")
				.defineInRange("duration", 20, 0, Integer.MAX_VALUE);
		shootCreeperCannonSpawnDisplacement = COMMON_BUILDER
				.comment("Projectile spawn displacement in blocks in front of the shooter.")
				.defineInRange("spawnDisplacement", 2, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// DigMobHole
		name = DigMobHole.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		digMobHoleNoHitHoleDepth = COMMON_BUILDER.comment("No-hit, hole depth (Z) in blocks.")
				.defineInRange("noHitHoleDepth", 2, 0, Integer.MAX_VALUE);
		digMobHoleNoHitHoleHeight = COMMON_BUILDER.comment("No-hit, hole height (Y) in blocks.")
				.defineInRange("noHitHoleHeight", 2, 0, Integer.MAX_VALUE);
		digMobHoleNoHitHoleWidth = COMMON_BUILDER.comment("No-hit, hole width (X) in blocks.")
				.defineInRange("noHitHoleWidth", 2, 0, Integer.MAX_VALUE);
		digMobHoleHeightExpansion = COMMON_BUILDER.comment("Hole expansion in addition to entity bounding box.")
				.defineInRange("heightExpansion", 1, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnCreeperArmy
		name = SpawnCreeperArmy.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnCreeperArmyEntities = COMMON_BUILDER.comment("Number of spawned entities.").defineInRange("number", 5, 0,
				Integer.MAX_VALUE);
		spawnCreeperArmySpawnArea = COMMON_BUILDER.comment("Spawn area in blocks.").defineInRange("spawnArea", 5, 0,
				Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnSkeletonArmy
		name = SpawnSkeletonArmy.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnSkeletonArmyEntities = COMMON_BUILDER.comment("Number of spawned entities.").defineInRange("number", 8, 0,
				Integer.MAX_VALUE);
		spawnSkeletonArmySpawnArea = COMMON_BUILDER.comment("Spawn area in blocks.").defineInRange("spawnArea", 5, 0,
				Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnKittenArmy
		name = SpawnKittenArmy.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnKittenArmyEntities = COMMON_BUILDER.comment("Number of spawned entities.").defineInRange("number", 8, 0,
				Integer.MAX_VALUE);
		spawnKittenArmySpawnArea = COMMON_BUILDER.comment("Spawn area in blocks.").defineInRange("spawnArea", 5, 0,
				Integer.MAX_VALUE);
		spawnKittenArmyAge = COMMON_BUILDER.comment("Kitten age.").defineInRange("age", 5, 0, Integer.MAX_VALUE);
		List<String> asList = Arrays.asList("Gotta Be Kitten Me", "Mittens", "Cloud", "Gotta Be Kitten Me", "Mittens",
				"Claus the Cat", "Caroline the Cat", "Fish", "Olly", "Patrick", "Yubaba", "Jennifer", "Bobby", "Olaf",
				"Violet", "Celestia", "Chelsea", "Chompy", "Beep", "Jacob", "Jonas", "Snow", "Sia", "The Bae", "Hugme",
				"Ghost", "Lady", "Pinky", "Litten", "Boogie", "Chocolate", "Marshmallow", "Caramel", "Kowalski", "Love",
				"Insert Name", "Meow", "TheCatIsALie", "Glados", "Molly", "Betty", "Jim", "ChangAndEng", "KitTheKitten",
				"#KittenClub", "MsPiggy", "SpaceHair", "CometKitty", "UniKitty", "Tiger", "Scott", "Buzz", "Mis",
				"Susanne", "TheTank", "Connie", "Chilli", "Ismael", "Megatron", "Volyova", "Scratches", "Hairball",
				"Mulle", "Billy", "Lilly", "Lizzy", "Kat", "Penny", "Sussie", "Winston", "Nemesis", "Hunter", "Claw",
				"Nusser", "Pusser", "Skade", "Darwin", "Basse", "Oginok", "Not A Number", "Star", "Hero", "Tiny",
				"Pandemonium Warden");
		spawnKittenArmyNames = COMMON_BUILDER.comment("Kitten names.").defineList("names", asList,
				e -> e instanceof String);
		COMMON_BUILDER.pop();

		// SpawnStairs
		name = SpawnStairs.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnStairsDuration = COMMON_BUILDER.comment("Duration of the effect in game ticks.").defineInRange("duration",
				600, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for books.
	 */
	static void setupBooksConfig() {

		// MobCommandersBaton
		String name = MobCommandersBaton.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		mobCommandersBatonTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to issue commands to charmed and commanded mobs.");
		mobCommandersBatonCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SmallFireballBook
		name = SmallFireballBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		smallFireballBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a fireball that is hurled at foes.");
		smallFireballBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SmallFireballRingBook
		name = SmallFireballRingBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		smallFireballRingBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shot a ring of small fireballs outwards.");
		smallFireballRingBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// BaconBazookaBook
		name = BaconBazookaBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		baconBazookaBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a pig projectile.");
		baconBazookaBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// BearBlasterBook
		name = BearBlasterBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		bearBlasterBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a polar bear projectile.");
		bearBlasterBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// CreeperCannonBook
		name = CreeperCannonBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		creeperCannonBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a creeper projectile.");
		creeperCannonBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// PrimedCreeperCannonBook
		name = PrimedCreeperCannonBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		primedCreeperCannonBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a primed creeper projectile.");
		primedCreeperCannonBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// TeleportBook
		name = TeleportBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		teleportBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot projectile and teleport to the position where the projectile hits.");
		teleportBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.").defineInRange("cooldown",
				25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SetSpawnPointBook
		name = SetSpawnPointBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		setSpawnPointBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to set the player spawn point.");
		setSpawnPointBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// DigMobHoleBook
		name = DigMobHoleBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		digMobHoleBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a projectile. If a creature is hit then an inconvenient hole is digged beneath the hit individual.");
		digMobHoleBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.").defineInRange("cooldown",
				25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnCreeperArmyBook
		name = SpawnCreeperArmyBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnCreeperArmyBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to spawn an army of creepers. The creepers can be commanded by Krenko's Command Baton.");
		spawnCreeperArmyBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnSkeletonArmyBook
		name = SpawnSkeletonArmyBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnSkeletonArmyBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click on spawn an army of skeletons. The skeletons can be commanded by Krenko's Command Baton.");
		spawnSkeletonArmyBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnKittenArmyBook
		name = SpawnKittenArmyBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnSkeletonArmyBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click on spawn an army of kittens. The kittens can be commanded by Krenko's Command Baton.");
		spawnSkeletonArmyBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// BuildTowerBook
		name = BuildTowerBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		buildTowerBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click on the ground to build a ruined portal tower.");
		buildTowerBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.").defineInRange("cooldown",
				200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// BuildStairsBook
		name = BuildStairsBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		buildStairsBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shoot a projectile. A magical staircase will be built from the player to where the projectile hit.");
		buildStairsBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
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
