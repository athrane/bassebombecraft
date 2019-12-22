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
import static bassebombecraft.config.InventoryItemConfig.getInstance;
import static bassebombecraft.config.InventoryItemConfig.getInstanceWithNoRange;
import static bassebombecraft.config.ItemConfig.getInstance;
import static bassebombecraft.config.ParticlesConfig.getInstance;
import static net.minecraftforge.fml.loading.FMLPaths.CONFIGDIR;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

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
import bassebombecraft.item.action.build.CopyPasteBlocks;
import bassebombecraft.item.action.inventory.AddBlindingEffect;
import bassebombecraft.item.action.inventory.AddFlameEffect;
import bassebombecraft.item.action.inventory.AddHealingEffect;
import bassebombecraft.item.action.inventory.AddLevitationEffect;
import bassebombecraft.item.action.inventory.AddMobsAggroEffect;
import bassebombecraft.item.action.inventory.AddMobsLevitationEffect;
import bassebombecraft.item.action.inventory.AddMobsPrimingEffect;
import bassebombecraft.item.action.inventory.AddPlayerAggroEffect;
import bassebombecraft.item.action.inventory.AddSaturationEffect;
import bassebombecraft.item.action.inventory.Naturalize;
import bassebombecraft.item.action.inventory.Pinkynize;
import bassebombecraft.item.action.inventory.Rainbownize;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.LavaSpiralMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
import bassebombecraft.item.book.BuildStairsBook;
import bassebombecraft.item.book.BuildTowerBook;
import bassebombecraft.item.book.CopyPasteBlocksBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.DigMobHoleBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.SpawnCreeperArmyBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnKittenArmyBook;
import bassebombecraft.item.book.SpawnSkeletonArmyBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.VacuumMistBook;
import bassebombecraft.item.inventory.AngelIdolInventoryItem;
import bassebombecraft.item.inventory.BlindnessIdolInventoryItem;
import bassebombecraft.item.inventory.CharmBeastIdolInventoryItem;
import bassebombecraft.item.inventory.ChickenizeIdolInventoryItem;
import bassebombecraft.item.inventory.EggProjectileIdolInventoryItem;
import bassebombecraft.item.inventory.FlameBlastIdolInventoryItem;
import bassebombecraft.item.inventory.FlowerIdolInventoryItem;
import bassebombecraft.item.inventory.LevitationIdolInventoryItem;
import bassebombecraft.item.inventory.LightningBoltIdolInventoryItem;
import bassebombecraft.item.inventory.LlamaSpitIdolInventoryItem;
import bassebombecraft.item.inventory.MassExtinctionEventIdolInventoryItem;
import bassebombecraft.item.inventory.MeteorIdolInventoryItem;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.MobsLevitationIdolInventoryItem;
import bassebombecraft.item.inventory.PinkynizeIdolInventoryItem;
import bassebombecraft.item.inventory.PlayerAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.item.inventory.RainIdolInventoryItem;
import bassebombecraft.item.inventory.RainbownizeIdolInventoryItem;
import bassebombecraft.item.inventory.ReaperIdolInventoryItem;
import bassebombecraft.item.inventory.SaturationIdolInventoryItem;
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
	public static ItemConfig hudItem;

	// TerminatorEyeItem
	public static ItemConfig terminatorEyeItem;

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
	public static ItemConfig baconBazookaBook;

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

	// LavaSpiralMistBook
	public static ItemConfig lavaSpiralMistBook;

	// VacuumMistBook
	public static ItemConfig vacuumMistBook;
	
	// SpawnCreeperArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnCreeperArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnCreeperArmyBookCooldown;

	// SpawnSkeletonArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnSkeletonArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnSkeletonArmyBookCooldown;

	// SpawnKittenArmyBook
	public static ForgeConfigSpec.ConfigValue<String> spawnKittenArmyBookTooltip;
	public static ForgeConfigSpec.IntValue spawnKittenArmyBookCooldown;

	// SpawnGuardianBook	
	public static ItemConfig spawnGuardianBook;
		
	// BuildTowerBook
	public static ForgeConfigSpec.ConfigValue<String> buildTowerBookTooltip;
	public static ForgeConfigSpec.IntValue buildTowerBookCooldown;

	// BuildStairsBook
	public static ForgeConfigSpec.ConfigValue<String> buildStairsBookTooltip;
	public static ForgeConfigSpec.IntValue buildStairsBookCooldown;

	// CopyPasteBlocksBook
	public static ForgeConfigSpec.ConfigValue<String> copyPasteBlocksBookTooltip;
	public static ForgeConfigSpec.IntValue copyPasteBlocksBookCooldown;

	// Inventory items..
	public static InventoryItemConfig charmBeastIdolInventoryItem;
	public static InventoryItemConfig levitationIdolInventoryItem;
	public static InventoryItemConfig mobsLevitationIdolInventoryItem;
	public static InventoryItemConfig rainIdolInventoryItem;
	public static InventoryItemConfig pinkynizeIdolInventoryItem;
	public static InventoryItemConfig primeMobIdolInventoryItem;
	public static InventoryItemConfig flameBlastIdolInventoryItem;
	public static InventoryItemConfig llamaSpitIdolInventoryItem;
	public static InventoryItemConfig eggProjectileIdolInventoryItem;
	public static InventoryItemConfig meteorIdolInventoryItem;
	public static InventoryItemConfig blindnessIdolInventoryItem;
	public static InventoryItemConfig angelIdolInventoryItem;
	public static InventoryItemConfig chickenizeIdolInventoryItem;
	public static InventoryItemConfig lightningBoltIdolInventoryItem;
	public static InventoryItemConfig flowerIdolInventoryItem;
	public static InventoryItemConfig rainbownizeIdolInventoryItem;
	public static InventoryItemConfig saturationIdolInventoryItem;
	public static InventoryItemConfig mobsAggroIdolInventoryItem;
	public static InventoryItemConfig playerAggroIdolInventoryItem;
	public static InventoryItemConfig reaperIdolInventoryItem;
	public static InventoryItemConfig massExtinctionEventIdolInventoryItem;

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

	// GenericBlockSpiralFillMist action
	public static ForgeConfigSpec.IntValue genericBlockSpiralFillMistSpiralSize;	
	
	// LavaSpiralMist action
	public static ForgeConfigSpec.IntValue lavaSpiralMistDuration;
	public static ParticlesConfig lavaSpiralMistParticleInfo;

	// VacuumMist action
	public static ForgeConfigSpec.IntValue vacuumMistDuration;
	public static ForgeConfigSpec.IntValue vacuumMistForce;	
	public static ParticlesConfig vacuumMistParticleInfo;
	
	// SpawnStairs projectile action
	public static ForgeConfigSpec.IntValue spawnStairsDuration;

	// CopyPasteBlocks action
	public static ForgeConfigSpec.BooleanValue copyPasteBlocksCaptureOnCopy;
	public static ParticlesConfig copyPasteBlocksParticleInfo;

	// AddLevitationEffect action
	public static ForgeConfigSpec.IntValue addLevitationEffectDuration;
	public static ForgeConfigSpec.IntValue addLevitationEffectAmplifier;

	// AddMobsLevitationEffect action
	public static ForgeConfigSpec.IntValue addMobsLevitationEffectDuration;
	public static ForgeConfigSpec.IntValue addMobsLevitationEffectAmplifier;

	// Pinkynize action
	public static ForgeConfigSpec.IntValue pinkynizeSpiralSize;

	// AddMobsPrimingEffect action
	public static ForgeConfigSpec.IntValue addMobsPrimingEffectDuration;

	// AddFlameEffect
	public static ForgeConfigSpec.IntValue addFlameEffectDuration;

	// AddBlindingEffect action
	public static ForgeConfigSpec.IntValue addBlindingEffectDuration;
	public static ForgeConfigSpec.IntValue addBlindingEffectAmplifier;

	// AddHealingEffect action
	public static ForgeConfigSpec.IntValue addHealingEffectDuration;
	public static ForgeConfigSpec.IntValue addHealingEffectAmplifier;

	// Naturalize action
	public static ForgeConfigSpec.IntValue naturalizeSpiralSize;

	// Naturalize action
	public static ForgeConfigSpec.IntValue rainbownizeSpiralSize;

	// AddSaturationEffect
	public static ForgeConfigSpec.IntValue addSaturationEffectDuration;

	// Commander commands..
	public static ForgeConfigSpec.DoubleValue danceCommandChance;
	public static ForgeConfigSpec.IntValue attackNearestMobCommandTargetDistance;
	public static ForgeConfigSpec.IntValue attackNearestPlayerCommandTargetDistance;

	// AddMobsAggroEffect action
	public static ForgeConfigSpec.IntValue addMobsAggroEffectDuration;
	public static ForgeConfigSpec.IntValue addMobsAggroEffectAmplifier;

	// AddPlayerAggroEffect action
	public static ForgeConfigSpec.IntValue addPlayerAggroEffectDuration;
	public static ForgeConfigSpec.IntValue addPlayerAggroEffectAmplifier;

	static {

		// build general section
		COMMON_BUILDER.comment("General settings").push("General");
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Basic item settings").push("BasicItems");
		setupBasicItemsGeneralConfig();
		setupBasicItemsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Commander commands ").push("CommanderCommands");
		setupCommandsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion effect settings").push("PotionEffecs");
		setupPotionEffectsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Potion settings").push("Potions");
		setupPotionsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Action settings").push("Actions");
		setupActionsConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Book settings").push("Books");
		setupBooksConfig();
		COMMON_BUILDER.pop();

		COMMON_BUILDER.comment("Inventory item settings").push("InventoryItems");
		setupInventoryItemsConfig();
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
		String name = TerminatorEyeItem.NAME;
		terminatorEyeItem = getInstance(COMMON_BUILDER, name,
				"An eye ripped from a T-500. Occasionally it tries to focus to acquire a target. Do not eat.", 10);

		// HUD item
		name = HudItem.NAME;
		hudItem = getInstance(COMMON_BUILDER, name, "BasseBombeCraft tactical HUD. Add to hotbar to activate.", 10);
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
		mobRespawningEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.")
				.defineInRange("areaOfEffect", 10, 0, Integer.MAX_VALUE);
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

		// GenericBlockSpiralFillMist
		name = GenericBlockSpiralFillMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		genericBlockSpiralFillMistSpiralSize = COMMON_BUILDER.comment("Spiral szie in blocks for ALL spiral effects.")
				.defineInRange("spiralSize", 20, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// LavaSpiralMist
		name = LavaSpiralMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		lavaSpiralMistDuration = COMMON_BUILDER.comment("Duration in game ticks.")
				.defineInRange("duration", 100, 0, Integer.MAX_VALUE);
		lavaSpiralMistParticleInfo = getInstance(COMMON_BUILDER, "flame", 10, 10, 0.1, 0.0, 0.0, 0.0);
		COMMON_BUILDER.pop();

		// VacuumMist
		name = VacuumMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		vacuumMistDuration = COMMON_BUILDER.comment("Duration in game ticks.")
				.defineInRange("duration", 500, 0, Integer.MAX_VALUE);
		vacuumMistForce = COMMON_BUILDER.comment("Effect pull force in blocks.")
				.defineInRange("force", 5, 0, Integer.MAX_VALUE);		
		vacuumMistParticleInfo = getInstance(COMMON_BUILDER, "effect", 10, 20, 0.3, 0.75, 0.75, 0.75);
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

		// CopyPasteBlocks
		name = CopyPasteBlocks.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		copyPasteBlocksCaptureOnCopy = COMMON_BUILDER
				.comment("Defines whether copied structure should be saved on disk as a template.")
				.define("captureOnCopy", true);
		copyPasteBlocksParticleInfo = getInstance(COMMON_BUILDER, "instant_effect", 5, -1, 0.3, 1.0, 1.0, 1.0);
		COMMON_BUILDER.pop();

		// AddLevitationEffect
		name = AddLevitationEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addLevitationEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting levitation.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addLevitationEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 40, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddMobsLevitationEffect
		name = AddMobsLevitationEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addMobsLevitationEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting levitation.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addMobsLevitationEffectDuration = COMMON_BUILDER
				.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Pinkynize
		name = Pinkynize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		pinkynizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 5, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddMobsPrimingEffect
		name = AddMobsPrimingEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addMobsPrimingEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddFlameEffect
		name = AddFlameEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addFlameEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddBlindingEffect
		name = AddBlindingEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addBlindingEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting blindness.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addBlindingEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddHealingEffect
		name = AddHealingEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addHealingEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting blindness.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addHealingEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Naturalize
		name = Naturalize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		naturalizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 20, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Rainbownize
		name = Rainbownize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		rainbownizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 20, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddSaturationEffect
		name = AddSaturationEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addSaturationEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddMobsAggroEffect
		name = AddMobsAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addMobsAggroEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting mob aggro.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addMobsAggroEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 400, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddPlayerAggroEffect
		name = AddPlayerAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addPlayerAggroEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting mob aggro toward the player.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addPlayerAggroEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 400, 0, Integer.MAX_VALUE);
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
		baconBazookaBook = getInstance(COMMON_BUILDER, name, "Right-click to shoot a pig projectile.", 25);

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

		// LavaSpiralMistBook
		name = LavaSpiralMistBook.ITEM_NAME;
		lavaSpiralMistBook = getInstance(COMMON_BUILDER, name, "Creates an expanding spiral of temporary lava blocks centered on where the caster is placed.", 10);
		
		// VacuumMistBook
		name = VacuumMistBook.ITEM_NAME;
		vacuumMistBook = getInstance(COMMON_BUILDER, name, "Creates a cloud of vacuum which pull mobs into it.", 100);
				
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

		// SpawnGuardianBook
		name = SpawnGuardianBook.ITEM_NAME;
		spawnGuardianBook = getInstance(COMMON_BUILDER, name, "Right-click to spawns a friendly golem. The golem will follow and protect its creator, i.e. the player or whoever spawned him. The golem will use the magic from BasseBombeCraft for its protection duties. The guardian can be commanded by Krenko's Command Baton", 25);
				
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

		// CopyPasteBlocksBook
		name = CopyPasteBlocksBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		copyPasteBlocksBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click on two ground blocks to set markers for the area to be copied . After the two markers are defined, right-click on a ground block to paste the area.");
		copyPasteBlocksBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

	}

	/**
	 * Define configuration for inventory items.
	 */
	static void setupInventoryItemsConfig() {

		// MobCharmBeastIdolInventoryItem
		String name = CharmBeastIdolInventoryItem.ITEM_NAME;
		Supplier<ParticlesConfig> splParticles = () -> getInstance(COMMON_BUILDER, "enchant", 5, 20, 1, 0.0, 0.0, 1.0);
		charmBeastIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will charm nearby mobs. The charmed creatures can be commanded by Krenko's Command Baton.",
				100, 5, splParticles);

		// LevitationIdolInventoryItem
		name = LevitationIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "cloud", 5, 20, 0.3, 0.0, 0.0, 1.0);
		levitationIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will levitate the player.", 4, splParticles);

		// MobsLevitationIdolInventoryItem
		name = MobsLevitationIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "cloud", 5, 20, 0.3, 0.0, 0.0, 1.0);
		mobsLevitationIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will levitate nearby creatures.", 5, 5, splParticles);

		// RainIdolInventoryItem
		name = RainIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "rain", 5, 20, 0.75, 0.0, 0.75, 0.0);
		rainIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will make it rain.", 200, splParticles);

		// PinkynizeIdolInventoryItem
		name = PinkynizeIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "enchant", 5, 20, 1.0, 1.0, 0.4, 0.7);
		pinkynizeIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol create a pink spiral of wool outwards from the player.", 5,
				splParticles);

		// PrimeMobIdolInventoryItem
		name = PrimeMobIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "large_smoke", 7, 20, 0.2, 1.0, 1.0, 1.0);
		primeMobIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol create a pink spiral of wool outwards from the player.", 5,
				5, splParticles);

		// FlameBlastIdolInventoryItem
		name = FlameBlastIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "flame", 5, 20, 0.3, 0.75, 0.25, 0.25);
		flameBlastIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will engulf nearby creatures in flames.", 100, 5,
				splParticles);

		// LlamaSpitIdolInventoryItem
		name = LlamaSpitIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "spit", 5, 20, 0.1, 0.75, 0.75, 0.75);
		llamaSpitIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The llama spirit embedded in the idol will spit on nearby creatures.",
				25, 5, splParticles);

		// EggProjectileIdolInventoryItem
		name = EggProjectileIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.2, 0.75, 0.75, 0.75);
		eggProjectileIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will target nearby creatures and attack with eggs.", 20, 5,
				splParticles);

		// MeteorIdolInventoryItem
		name = MeteorIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "flame", 5, 20, 0.2, 0.75, 0.75, 0.75);
		meteorIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will rain meteors from the sky onto nearby creatures.", 50,
				5, splParticles);

		// BlindnessIdolInventoryItem
		name = BlindnessIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "ambient_entity_effect", 5, 20, 0.7, 0.75, 0.25, 0.25);
		blindnessIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will blind nearby creatures.", 50, 5, splParticles);

		// AngelIdolInventoryItem
		name = AngelIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.3, 0.75, 0.0, 0.0);
		angelIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol heals the player.", 50, splParticles);

		// ChickenizeIdolInventoryItem
		name = ChickenizeIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.75, 0.0, 0.75, 0.0);
		chickenizeIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol transforms nearby creatures to chickens.", 50, 5,
				splParticles);

		// LightningBoltIdolInventoryItem
		name = LightningBoltIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.3, 0.75, 0.75, 0.75);
		lightningBoltIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol shoot lightning bolts on nearby creatures.", 50, 5,
				splParticles);

		// FlowerIdolInventoryItem
		name = FlowerIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.075, 0.0, 0.0, 0.75);
		flowerIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol create a spiral of flowers outwards from the player.", 5,
				splParticles);

		// RainbownizeIdolInventoryItem
		name = RainbownizeIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "effect", 5, 20, 0.075, 0.0, 0.0, 0.75);
		rainbownizeIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol create a rainbow coloured spiral outwards from the player.",
				5, splParticles);

		// SaturationIdolInventoryItem
		name = SaturationIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "happy_villager", 5, 20, 0.3, 0.8, 0.0, 0.0);
		saturationIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will fill up your belly.", 100, splParticles);

		// MobsAggroIdolInventoryItem
		name = MobsAggroIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "damage_indicator", 5, 20, 0.3, 0.9, 0.9, 0.9);
		mobsAggroIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will aggro all creatures in the vicinity toward other mobs. This includes team members and charmed mobs.",
				100, 5, splParticles);

		// PlayerAggroIdolInventoryItem
		name = PlayerAggroIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "damage_indicator", 5, 20, 0.3, 0.9, 0.9, 0.9);
		playerAggroIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will aggro all creatures in the vicinity towards the player. This includes team members and charmed mobs.",
				100, 5, splParticles);

		// ReaperIdolInventoryItem
		name = ReaperIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.075, 0.0, 0.0, 0.0);
		reaperIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will kill the user and destroy itself.", 200, splParticles);

		// MassExtinctionEventIdolInventoryItem
		name = MassExtinctionEventIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.075, 0.25, 0.75, 0.25);
		massExtinctionEventIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will cause a widespread and rapid decrease in the biodiversity within the game comparable to a mass extinction event. Mass extinction events happens every 26 to 30 million years, so expect a long cooldown.",
				1000000000, 200, splParticles);

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
