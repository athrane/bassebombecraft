package bassebombecraft.config;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AMPLIFICATION_POTION_NAME;
import static bassebombecraft.ModConstants.BACONBAZOOKA_EFFECT_NAME;
import static bassebombecraft.ModConstants.BEARBLASTER_EFFECT_NAME;
import static bassebombecraft.ModConstants.CHARMED_MOB_NAME;
import static bassebombecraft.ModConstants.CREEPERCANNON_EFFECT_NAME;
import static bassebombecraft.ModConstants.INTERNAL_TOML_CONFIG_FILE_NAME;
import static bassebombecraft.ModConstants.ITEM_BASICITEM_DEFAULT_COOLDOWN;
import static bassebombecraft.ModConstants.ITEM_DEFAULT_TOOLTIP;
import static bassebombecraft.ModConstants.MOB_AGGRO_POTION_NAME;
import static bassebombecraft.ModConstants.MOB_PRIMING_POTION_NAME;
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

import bassebombecraft.client.event.charm.ClientCharmedMobsRepository;
import bassebombecraft.entity.commander.command.AttackNearestMobCommand;
import bassebombecraft.entity.commander.command.AttackNearestPlayerCommand;
import bassebombecraft.entity.commander.command.DanceCommand;
import bassebombecraft.event.charm.CharmedMobEventHandler;
import bassebombecraft.event.charm.ServerCharmedMobsRepository;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.ShootSmallFireballRing;
import bassebombecraft.item.action.build.CopyPasteBlocks;
import bassebombecraft.item.action.inventory.AddAggroMobEffect;
import bassebombecraft.item.action.inventory.AddAggroPlayerEffect;
import bassebombecraft.item.action.inventory.AddBlindingEffect;
import bassebombecraft.item.action.inventory.AddFlameEffect;
import bassebombecraft.item.action.inventory.AddHealingEffect;
import bassebombecraft.item.action.inventory.AddLevitationEffect;
import bassebombecraft.item.action.inventory.AddMobsLevitationEffect;
import bassebombecraft.item.action.inventory.AddMobsPrimingEffect;
import bassebombecraft.item.action.inventory.AddReflectEffect;
import bassebombecraft.item.action.inventory.AddSaturationEffect;
import bassebombecraft.item.action.inventory.Naturalize;
import bassebombecraft.item.action.inventory.Pinkynize;
import bassebombecraft.item.action.inventory.Rainbownize;
import bassebombecraft.item.action.inventory.SpawnAngryParrots;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.block.LavaSpiralMist;
import bassebombecraft.item.action.mist.block.NaturalizeSpiralMist;
import bassebombecraft.item.action.mist.block.RainbowSpiralMist;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.action.mist.entity.HealingMist;
import bassebombecraft.item.action.mist.entity.VacuumMist;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.basic.TerminatorEyeItem;
import bassebombecraft.item.baton.MobCommandersBaton;
import bassebombecraft.item.book.BaconBazookaBook;
import bassebombecraft.item.book.BearBlasterBook;
import bassebombecraft.item.book.BeastmasterBook;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.item.book.BuildStairsBook;
import bassebombecraft.item.book.BuildTowerBook;
import bassebombecraft.item.book.CopyPasteBlocksBook;
import bassebombecraft.item.book.CreeperCannonBook;
import bassebombecraft.item.book.DecoyBook;
import bassebombecraft.item.book.DigMobHoleBook;
import bassebombecraft.item.book.HealingMistBook;
import bassebombecraft.item.book.LargeFireballBook;
import bassebombecraft.item.book.LavaSpiralMistBook;
import bassebombecraft.item.book.LingeringFlameBook;
import bassebombecraft.item.book.LingeringFuryBook;
import bassebombecraft.item.book.NaturalizeBook;
import bassebombecraft.item.book.PrimedCreeperCannonBook;
import bassebombecraft.item.book.RainbownizeBook;
import bassebombecraft.item.book.ReceiveAggroBook;
import bassebombecraft.item.book.SetSpawnPointBook;
import bassebombecraft.item.book.SmallFireballBook;
import bassebombecraft.item.book.SmallFireballRingBook;
import bassebombecraft.item.book.SpawnCreeperArmyBook;
import bassebombecraft.item.book.SpawnFlamingChickenBook;
import bassebombecraft.item.book.SpawnGuardianBook;
import bassebombecraft.item.book.SpawnKittenArmyBook;
import bassebombecraft.item.book.SpawnSkeletonArmyBook;
import bassebombecraft.item.book.TeleportBook;
import bassebombecraft.item.book.ToxicMistBook;
import bassebombecraft.item.book.VacuumMistBook;
import bassebombecraft.item.book.WitherSkullBook;
import bassebombecraft.item.inventory.AngelIdolInventoryItem;
import bassebombecraft.item.inventory.AngryParrotsIdolInventoryItem;
import bassebombecraft.item.inventory.BlindnessIdolInventoryItem;
import bassebombecraft.item.inventory.CharmBeastIdolInventoryItem;
import bassebombecraft.item.inventory.ChickenizeIdolInventoryItem;
import bassebombecraft.item.inventory.DecreaseSizeIdolInventoryItem;
import bassebombecraft.item.inventory.EggProjectileIdolInventoryItem;
import bassebombecraft.item.inventory.FlameBlastIdolInventoryItem;
import bassebombecraft.item.inventory.FlowerIdolInventoryItem;
import bassebombecraft.item.inventory.IncreaseSizeIdolInventoryItem;
import bassebombecraft.item.inventory.KillerBeesIdolInventoryItem;
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
import bassebombecraft.item.inventory.ReflectIdolInventoryItem;
import bassebombecraft.item.inventory.RemoveBlockSpiralIdolInventoryItem;
import bassebombecraft.item.inventory.RespawnIdolInventoryItem;
import bassebombecraft.item.inventory.SaturationIdolInventoryItem;
import bassebombecraft.item.inventory.WarPigsIdolInventoryItem;
import bassebombecraft.operator.entity.Respawn;
import bassebombecraft.operator.entity.SpawnDecoy;
import bassebombecraft.operator.entity.SpawnKillerBee;
import bassebombecraft.operator.entity.SpawnWarPig;
import bassebombecraft.potion.effect.AggroMobEffect;
import bassebombecraft.potion.effect.AggroPlayerEffect;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.ReceiveAggroEffect;
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

	// Repositories..

	/**
	 * Charmed mob properties used by repositories
	 * {@linkplain ServerCharmedMobsRepository} and
	 * {@linkplain ClientCharmedMobsRepository}.
	 */
	public static ForgeConfigSpec.IntValue charmDuration;

	// Event handlers..

	/**
	 * Particles spawned by charmed mob, spawned by
	 * {@linkplain CharmedMobEventHandler}.
	 */
	public static ParticlesConfig charmedMobParticles;

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

	// Potion effects..

	// AmplifierEffect
	public static ForgeConfigSpec.IntValue amplifierEffectUpdateFrequency;

	// MobPrimingEffect
	public static ForgeConfigSpec.IntValue mobPrimingEffectCountdown;

	// AggroMobEffect
	public static ForgeConfigSpec.IntValue aggroMobEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue aggroMobEffectUpdateFrequency;

	// ReceiveAggroEffect
	public static ForgeConfigSpec.IntValue receiveAggroEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue receiveAggroEffectUpdateFrequency;

	// AggroPlayerEffect
	public static ForgeConfigSpec.IntValue aggroPlayerEffectAreaOfEffect;
	public static ForgeConfigSpec.IntValue aggroPlayerEffectUpdateFrequency;

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
	public static ItemConfig smallFireballBook;
	public static ItemConfig largeFireballBook;

	// SmallFireballRingBook
	public static ForgeConfigSpec.ConfigValue<String> smallFireballRingBookTooltip;
	public static ForgeConfigSpec.IntValue smallFireballRingBookCooldown;

	// LingeringFlameBook
	public static ItemConfig lingeringFlameBook;
	public static ItemConfig lingeringFuryBook;
	public static ItemConfig toxicMistBook;
	public static ItemConfig witherSkullBook;
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
	public static ItemConfig teleportBook;

	// SetSpawnPointBook
	public static ForgeConfigSpec.ConfigValue<String> setSpawnPointBookTooltip;
	public static ForgeConfigSpec.IntValue setSpawnPointBookCooldown;

	// BeastmasterBook
	public static ItemConfig beastmasterBook;
	public static ItemConfig decoyBook;
	public static ItemConfig receiveAggroBook;

	// DigMobHoleBook
	public static ForgeConfigSpec.ConfigValue<String> digMobHoleBookTooltip;
	public static ForgeConfigSpec.IntValue digMobHoleBookCooldown;

	public static ItemConfig lavaSpiralMistBook;
	public static ItemConfig rainbownizeBook;
	public static ItemConfig naturalizeBook;

	public static ItemConfig vacuumMistBook;
	public static ItemConfig healingMistBook;

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
	public static ItemConfig spawnFlamingChickenBook;

	// BuildTowerBook
	public static ForgeConfigSpec.ConfigValue<String> buildTowerBookTooltip;
	public static ForgeConfigSpec.IntValue buildTowerBookCooldown;

	// BuildStairsBook
	public static ForgeConfigSpec.ConfigValue<String> buildStairsBookTooltip;
	public static ForgeConfigSpec.IntValue buildStairsBookCooldown;

	// CopyPasteBlocksBook
	public static ForgeConfigSpec.ConfigValue<String> copyPasteBlocksBookTooltip;
	public static ForgeConfigSpec.IntValue copyPasteBlocksBookCooldown;

	public static ItemConfig buildMineBook;

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
	public static InventoryItemConfig angryParrotsIdolInventoryItem;
	public static InventoryItemConfig reflectIdolInventoryItem;
	public static InventoryItemConfig killerBeesIdolInventoryItem;
	public static InventoryItemConfig warPigsIdolInventoryItem;
	public static InventoryItemConfig decreaseSizeIdolInventoryItem;
	public static InventoryItemConfig increaseSizeIdolInventoryItem;
	public static InventoryItemConfig respawnIdolInventoryItem;
	public static InventoryItemConfig removeBlockSpiralIdolInventoryItem;

	/**
	 * Properties for {@linkplain RemoveBlockSpiralIdolInventoryItem}.
	 */
	public static ForgeConfigSpec.IntValue removeBlockSpiralIdolInventoryItemSpiralSize;
	public static ParticlesConfig removeBlockSpiralIdolInventoryItemParticleInfo;

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

	// GenericEntityMist action
	public static ForgeConfigSpec.IntValue genericEntityMistSpiralSize;

	// LavaSpiralMist action
	public static ForgeConfigSpec.IntValue lavaSpiralMistDuration;
	public static ParticlesConfig lavaSpiralMistParticleInfo;

	// RainbowSpiralMist action
	public static ForgeConfigSpec.IntValue rainbowSpiralMistDuration;
	public static ParticlesConfig rainbowSpiralMistParticleInfo;

	// NaturalizeSpiralMist action
	public static ForgeConfigSpec.IntValue naturalizeSpiralMistDuration;
	public static ParticlesConfig naturalizeSpiralMistParticleInfo;

	// VacuumMist action
	public static ForgeConfigSpec.IntValue vacuumMistDuration;
	public static ForgeConfigSpec.IntValue vacuumMistForce;
	public static ParticlesConfig vacuumMistParticleInfo;

	// HealingMist action
	public static ForgeConfigSpec.IntValue healingMistDuration;
	public static ParticlesConfig healingMistParticleInfo;

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

	// AddReflectEffect action
	public static ForgeConfigSpec.IntValue addReflectEffectDuration;
	public static ForgeConfigSpec.IntValue addReflectEffectAmplifier;

	// Naturalize action
	public static ForgeConfigSpec.IntValue naturalizeSpiralSize;

	// Rainbownize action
	public static ForgeConfigSpec.IntValue rainbownizeSpiralSize;

	// AddSaturationEffect
	public static ForgeConfigSpec.IntValue addSaturationEffectDuration;

	// Commander commands..
	public static ForgeConfigSpec.DoubleValue danceCommandChance;
	public static ForgeConfigSpec.IntValue attackNearestMobCommandTargetDistance;
	public static ForgeConfigSpec.IntValue attackNearestPlayerCommandTargetDistance;

	// AddAggroMobEffect action
	public static ForgeConfigSpec.IntValue addAggroMobEffectDuration;
	public static ForgeConfigSpec.IntValue addAggroMobEffectAmplifier;

	// AddAggroPlayerEffect action
	public static ForgeConfigSpec.IntValue addAggroPlayerEffectDuration;
	public static ForgeConfigSpec.IntValue addAggroPlayerEffectAmplifier;

	// Spawn angry parrots action
	public static ForgeConfigSpec.IntValue spawnAngryParrotsDamage;
	public static ForgeConfigSpec.DoubleValue spawnAngryParrotsMovementSpeed;

	// Operators..

	// Spawn killer bee operator
	public static ForgeConfigSpec.IntValue spawnKillerBeeDamage;
	public static ForgeConfigSpec.DoubleValue spawnKillerBeeMovementSpeed;

	// Spawn war pig operator
	public static ForgeConfigSpec.IntValue spawnWarPigDamage;
	public static ForgeConfigSpec.DoubleValue spawnWarPigMovementSpeed;

	// Spawn decoy operator
	public static ForgeConfigSpec.IntValue spawnDecoyMaxHealth;
	public static ForgeConfigSpec.DoubleValue spawnDecoyKnockBackResistance;

	// Decrease size effect operator
	public static ForgeConfigSpec.IntValue decreaseSizeEffectDuration;
	public static ForgeConfigSpec.IntValue decreaseSizeEffectAmplifier;

	// Increase size effect operator
	public static ForgeConfigSpec.IntValue increaseSizeEffectDuration;
	public static ForgeConfigSpec.IntValue increaseSizeEffectAmplifier;

	// Receive aggro effect operator
	public static ForgeConfigSpec.IntValue receiveAggroEffectDuration;
	public static ForgeConfigSpec.IntValue receiveAggroEffectAmplifier;

	// Respawn operator
	public static ForgeConfigSpec.IntValue respawnMinEntities;
	public static ForgeConfigSpec.IntValue respawnMaxEntities;
	public static ForgeConfigSpec.IntValue respawnSpawnArea;

	static {

		// build general section
		COMMON_BUILDER.comment("General settings").push("General");
		setupGeneralConfig();
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

		COMMON_BUILDER.comment("Operator settings").push("Operators");
		setupOperatorConfig();
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
	 * Define general settings for repositories and event handlers etc.
	 */
	static void setupGeneralConfig() {

		/**
		 * Charmed mob properties used by repositories
		 * {@linkplain ServerCharmedMobsRepository} and
		 * {@linkplain ClientCharmedMobsRepository}.
		 */
		String name = CHARMED_MOB_NAME;
		;
		COMMON_BUILDER.comment(name + " settings").push(name);
		charmDuration = COMMON_BUILDER.comment("Charm duration (in game ticks).").defineInRange("charmDuration", 1000,
				0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		/**
		 * Particles spawned by charmed mob, spawned by
		 * {@linkplain CharmedMobEventHandler}.
		 */
		name = CHARMED_MOB_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		charmedMobParticles = getInstance(COMMON_BUILDER, "heart", 1, 10, 0.1, 1.0, 1.0, 1.0);
		COMMON_BUILDER.pop();
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

		// Attack nearest player command
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

		// aggro mob effect
		name = AggroMobEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		aggroMobEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.").defineInRange("areaOfEffect",
				10, 0, Integer.MAX_VALUE);
		aggroMobEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
				.defineInRange("updateFrequency", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// receive aggro effect
		name = ReceiveAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		receiveAggroEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.")
				.defineInRange("areaOfEffect", 10, 0, Integer.MAX_VALUE);
		receiveAggroEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
				.defineInRange("updateFrequency", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// aggro player effect
		name = AggroPlayerEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		aggroPlayerEffectAreaOfEffect = COMMON_BUILDER.comment("Area of effect in blocks.")
				.defineInRange("areaOfEffect", 10, 0, Integer.MAX_VALUE);
		aggroPlayerEffectUpdateFrequency = COMMON_BUILDER.comment("Update frequency of the effect in game ticks.")
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
		genericBlockSpiralFillMistSpiralSize = COMMON_BUILDER
				.comment("Spiral szie in blocks for ALL block spiral effects.")
				.defineInRange("spiralSize", 9, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// GenericEntityMist
		name = GenericEntityMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		genericEntityMistSpiralSize = COMMON_BUILDER.comment("Spiral szie in blocks for ALL entity spiral effects.")
				.defineInRange("spiralSize", 9, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// LavaSpiralMist
		name = LavaSpiralMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		lavaSpiralMistDuration = COMMON_BUILDER.comment("Duration in game ticks.").defineInRange("duration", 100, 0,
				Integer.MAX_VALUE);
		lavaSpiralMistParticleInfo = getInstance(COMMON_BUILDER, "flame", 10, 10, 0.1, 0.0, 0.0, 0.0);
		COMMON_BUILDER.pop();

		// RainbowSpiralMist
		name = RainbowSpiralMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		rainbowSpiralMistDuration = COMMON_BUILDER.comment("Duration in game ticks.").defineInRange("duration",
				9 * 9 * 3, 0, Integer.MAX_VALUE);
		rainbowSpiralMistParticleInfo = getInstance(COMMON_BUILDER, "heart", 1, 10, 0.2, 0.75, 0.75, 0.75);
		COMMON_BUILDER.pop();

		// NaturalizeSpiralMist
		name = NaturalizeSpiralMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		naturalizeSpiralMistDuration = COMMON_BUILDER.comment("Duration in game ticks.").defineInRange("duration",
				9 * 9 * 3, 0, Integer.MAX_VALUE);
		naturalizeSpiralMistParticleInfo = getInstance(COMMON_BUILDER, "effect", 1, 10, 0.2, 0.75, 0.75, 0.75);
		COMMON_BUILDER.pop();

		// VacuumMist
		name = VacuumMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		vacuumMistDuration = COMMON_BUILDER.comment("Duration in game ticks.").defineInRange("duration", 500, 0,
				Integer.MAX_VALUE);
		vacuumMistForce = COMMON_BUILDER.comment("Effect pull force in blocks.").defineInRange("force", 5, 0,
				Integer.MAX_VALUE);
		vacuumMistParticleInfo = getInstance(COMMON_BUILDER, "effect", 10, 20, 0.3, 0.75, 0.75, 0.75);
		COMMON_BUILDER.pop();

		// HealingMist
		name = HealingMist.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		healingMistDuration = COMMON_BUILDER.comment("Duration in game ticks.").defineInRange("duration", 200, 0,
				Integer.MAX_VALUE);
		healingMistParticleInfo = getInstance(COMMON_BUILDER, "effect", 5, 20, 0.3, 0.75, 0.0, 0.0);
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
				.comment("Potency of the effect (as a potion effect), i.e. the resulting healing potency.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addHealingEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddReflectEffect
		name = AddReflectEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addReflectEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting reflected damage.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addReflectEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Naturalize
		name = Naturalize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		naturalizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Rainbownize
		name = Rainbownize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		rainbownizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Pinkynize
		name = Pinkynize.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		pinkynizeSpiralSize = COMMON_BUILDER.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 10, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddSaturationEffect
		name = AddSaturationEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addSaturationEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddMobsAggroEffect
		name = AddAggroMobEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addAggroMobEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting mob aggro.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addAggroMobEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 400, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// AddPlayerAggroEffect
		name = AddAggroPlayerEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		addAggroPlayerEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), i.e. the resulting mob aggro toward the player.")
				.defineInRange("amplifier", 1, 0, Integer.MAX_VALUE);
		addAggroPlayerEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 400, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnAngryParrots
		name = SpawnAngryParrots.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnAngryParrotsDamage = COMMON_BUILDER.comment("Parrot damage.").defineInRange("damage", 2, 0,
				Integer.MAX_VALUE);
		spawnAngryParrotsMovementSpeed = COMMON_BUILDER.comment("Parrot movement speed.").defineInRange("movementSpeed",
				1.0D, 0, 5.0D);
		COMMON_BUILDER.pop();
	}

	/**
	 * Define configuration for operators.
	 */
	static void setupOperatorConfig() {

		// SpawnKillerBee
		String name = SpawnKillerBee.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnKillerBeeDamage = COMMON_BUILDER.comment("Bee damage.").defineInRange("damage", 2, 0, Integer.MAX_VALUE);
		spawnKillerBeeMovementSpeed = COMMON_BUILDER.comment("Bee movement speed.").defineInRange("movementSpeed", 1.0D,
				0, 5.0D);
		COMMON_BUILDER.pop();

		// SpawnWarPig
		name = SpawnWarPig.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnWarPigDamage = COMMON_BUILDER.comment("Pig damage.").defineInRange("damage", 2, 0, Integer.MAX_VALUE);
		spawnWarPigMovementSpeed = COMMON_BUILDER.comment("Pig movement speed.").defineInRange("movementSpeed", 0.75D,
				0, 5.0D);
		COMMON_BUILDER.pop();

		// Decrease size effect for the DecreaseSizeIdolInventoryItem class
		name = DecreaseSizeIdolInventoryItem.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		decreaseSizeEffectAmplifier = COMMON_BUILDER.comment(
				"Potency of the effect (as a potion effect), i.e. the resulting size decrease in procentage, i.e. 50% is half size. ")
				.defineInRange("amplifier", 50, 1, 100);
		decreaseSizeEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Increase size effect for the for the IncreaseSizeIdolInventoryItem class
		name = IncreaseSizeIdolInventoryItem.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		increaseSizeEffectAmplifier = COMMON_BUILDER.comment(
				"Potency of the effect (as a potion effect), i.e. the resulting size increase in procentage, i.e. 200% is double size. ")
				.defineInRange("amplifier", 200, 1, 500);
		increaseSizeEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 200, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// SpawnDecoy
		name = SpawnDecoy.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		spawnDecoyMaxHealth = COMMON_BUILDER.comment("Decoy max health.").defineInRange("maxHealth", 200, 0,
				Integer.MAX_VALUE);
		spawnDecoyKnockBackResistance = COMMON_BUILDER.comment("Decoy knockback resistance in %.")
				.defineInRange("knockbackResistance ", 1.0D, 0, 1.0D);
		COMMON_BUILDER.pop();

		// Receive aggro effect for the DecoyBook class
		name = ReceiveAggroEffect.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		receiveAggroEffectAmplifier = COMMON_BUILDER
				.comment("Potency of the effect (as a potion effect), has no fuction for this effect. ")
				.defineInRange("amplifier", 1, 1, 1);
		receiveAggroEffectDuration = COMMON_BUILDER.comment("Duration of effect (as a potion effect) in game ticks.")
				.defineInRange("duration", 500, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// Respawn
		name = Respawn.NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		respawnMinEntities = COMMON_BUILDER.comment("Min. number of entities spawned.").defineInRange("minEntities", 5,
				0, 10);
		respawnMaxEntities = COMMON_BUILDER.comment("Max. number of entities spawned.").defineInRange("maxEntities", 2,
				0, 5);
		respawnSpawnArea = COMMON_BUILDER.comment("Size of spawn areas around the dead entity.")
				.defineInRange("SpawnArea ", 5, 0, 10);
		COMMON_BUILDER.pop();

		
		// RemoveBlockSpiralIdolInventoryItem
		name = RemoveBlockSpiralIdolInventoryItem.ITEM_NAME;		
		COMMON_BUILDER.comment(name + " settings").push(name);
		removeBlockSpiralIdolInventoryItemSpiralSize = COMMON_BUILDER
				.comment("Spiral size, measured in rotations around the centre.")
				.defineInRange("spiralSize", 5, 0, Integer.MAX_VALUE);
		removeBlockSpiralIdolInventoryItemParticleInfo = getInstance(COMMON_BUILDER, "instant_effect", 5, 10, 0.3, 1.0, 1.0, 1.0);
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
		smallFireballBook = getInstance(COMMON_BUILDER, name, "Right-click to shoot a fireball that is hurled at foes.",
				25);

		// LargeFireballBook
		name = LargeFireballBook.ITEM_NAME;
		largeFireballBook = getInstance(COMMON_BUILDER, name,
				"Right-click to shoot a large fireball that is hurled at foes.", 25);

		// SmallFireballRingBook
		name = SmallFireballRingBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		smallFireballRingBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to shot a ring of small fireballs outwards.");
		smallFireballRingBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 50, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// LingeringFlameBook
		name = LingeringFlameBook.ITEM_NAME;
		lingeringFlameBook = getInstance(COMMON_BUILDER, name,
				"Right-click to create a static flame that explodes when a mob comes near.", 25);

		// LingeringFuryBook
		name = LingeringFuryBook.ITEM_NAME;
		lingeringFuryBook = getInstance(COMMON_BUILDER, name,
				"Right-click to create a static flame that explodes violently when a mob comes near.", 50);

		// ToxicMistBook
		name = ToxicMistBook.ITEM_NAME;
		toxicMistBook = getInstance(COMMON_BUILDER, name, "Right-click to create a cloud of poison mist.", 50);

		// WitherSkullBook
		name = WitherSkullBook.ITEM_NAME;
		witherSkullBook = getInstance(COMMON_BUILDER, name, "Creates a Wither Skull that is hurled at foes.", 25);

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
		teleportBook = getInstance(COMMON_BUILDER, name,
				"Right-click to shoot projectile and teleport to the position where the projectile hits.", 25);
		COMMON_BUILDER.pop();

		// SetSpawnPointBook
		name = SetSpawnPointBook.ITEM_NAME;
		COMMON_BUILDER.comment(name + " settings").push(name);
		setSpawnPointBookTooltip = COMMON_BUILDER.comment("Tooltip for item.").define("tooltip",
				"Right-click to set the player spawn point.");
		setSpawnPointBookCooldown = COMMON_BUILDER.comment("Game ticks between item activation.")
				.defineInRange("cooldown", 25, 0, Integer.MAX_VALUE);
		COMMON_BUILDER.pop();

		// BeastmasterBook
		name = BeastmasterBook.ITEM_NAME;
		beastmasterBook = getInstance(COMMON_BUILDER, name,
				"Right-click shot a beast charm at mobs. If the charm hits, the mob will temporarily tamed to fight by the side of the beastmaster who conjured the charm. When the duration of the charm ends, the charmed mob will regain its former mental abilities. A charmed mob can be commanded by Krenko's Command Baton",
				25);

		// DecoyBook
		name = DecoyBook.ITEM_NAME;
		decoyBook = getInstance(COMMON_BUILDER, name,
				"Right-click to spawn a decoy. All mobs in the vicinity will aggro the poor thing.", 100);

		// ReceiveAggroBook
		name = ReceiveAggroBook.ITEM_NAME;
		receiveAggroBook = getInstance(COMMON_BUILDER, name,
				"Right-click to shoot projectile. If the projectile hits a target mob then all mobs in the vicinity will aggro the hit target mob.",
				100);

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
		lavaSpiralMistBook = getInstance(COMMON_BUILDER, name,
				"Creates an expanding spiral of temporary lava blocks centered on where the caster is placed.", 100);

		// VacuumMistBook
		name = VacuumMistBook.ITEM_NAME;
		vacuumMistBook = getInstance(COMMON_BUILDER, name, "Creates a cloud of vacuum which pull mobs into it.", 100);

		// HealingMistBook
		name = HealingMistBook.ITEM_NAME;
		healingMistBook = getInstance(COMMON_BUILDER, name,
				"It creates a cloud of healing that heal nearby friends and foes.", 100);

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
		spawnGuardianBook = getInstance(COMMON_BUILDER, name,
				"Right-click to spawns a friendly golem. The golem will follow and protect its creator, i.e. the player or whoever spawned him. The golem will use the magic from BasseBombeCraft for its protection duties. The guardian can be commanded by Krenko's Command Baton",
				25);

		// SpawnFlamingChickenBook
		name = SpawnFlamingChickenBook.ITEM_NAME;
		spawnFlamingChickenBook = getInstance(COMMON_BUILDER, name,
				"Right-click to spawns a failed phoenix. The phoenix will panic due to it being on fire.", 25);

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

		// BuildMineBook
		name = BuildMineBook.ITEM_NAME;
		buildMineBook = getInstance(COMMON_BUILDER, name,
				"Click on a ground block to excavate an entrance entrance to a lower level mine. A ground block is a block at the same level as the block that the payer is standing on. Click on a block in front of the player to excavate a mine corridor, room or hall.",
				25);

		// RainbownizeBook
		name = RainbownizeBook.ITEM_NAME;
		rainbownizeBook = getInstance(COMMON_BUILDER, name,
				"Right-click to create an expanding spiral of rainbow-colored wool blocks centered on where the caster is placed.",
				25);

		// NaturalizeBook
		name = NaturalizeBook.ITEM_NAME;
		naturalizeBook = getInstance(COMMON_BUILDER, name,
				"Right-click to create an expanding spiral of flowers around the caster.", 25);
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
				"Equip in either hand to activate. The idol will prime any nearby mob for an explosion.", 5, 5,
				splParticles);

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
				"Equip in either hand to activate. The idol will cause a widespread and rapid decrease in the biodiversity within the game comparable to a mass extinction event. Mass extinction events happen every 26 to 30 million years, so expect a long cooldown.",
				1000000000, 200, splParticles);

		// AngryParrotsIdolInventoryItem
		name = AngryParrotsIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.25, 0.0, 0.0, 0.9);
		angryParrotsIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will spawn angry sea parrots who will attack the player's target or some random mobs.",
				25, 5, splParticles);

		// ReflectIdolInventoryItem
		name = ReflectIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.3, 0.75, 0.0, 0.0);
		reflectIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will reflect mob damage back to the assailant.", 50,
				splParticles);

		// KillerBeesIdolInventoryItem
		name = KillerBeesIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "dripping_honey", 5, 20, 0.001, 0.0, 0.0, 0.0);
		killerBeesIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will spawn killer bees who will attack the player's target or some random mobs.",
				25, 5, splParticles);

		// WarPigsIdolInventoryItem
		name = WarPigsIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.3, 0.0, 0.0, 0.0);
		warPigsIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will spawn psycho war pigs who will attack the player's target or some random mobs.",
				25, 5, splParticles);

		// DecreaseSizeIdolInventoryItem
		name = DecreaseSizeIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "enchant", 5, 20, 0.3, 0.75, 0.5, 0.5);
		decreaseSizeIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will decrease the size of nearby mobs. The magic doesn't work on players.",
				25, 5, splParticles);

		// IncreaseSizeIdolInventoryItem
		name = IncreaseSizeIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "enchant", 5, 20, 0.3, 0.75, 0.5, 0.5);
		increaseSizeIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will increase the size of nearby mobs. The magic doesn't work on players.",
				25, 5, splParticles);

		// RespawnIdolInventoryItem
		name = RespawnIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "crit", 5, 20, 0.5, 0.9, 0.9, 0.9);
		respawnIdolInventoryItem = getInstance(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will curse nearby mobs with a spectral curse. When a cursed mob dies any number of spectres will respawn.",
				25, 5, splParticles);

		// RemoveBlockSpiralIdolInventoryItem
		name = RemoveBlockSpiralIdolInventoryItem.ITEM_NAME;
		splParticles = () -> getInstance(COMMON_BUILDER, "enchant", 5, 20, 1.0, 1.0, 0.4, 0.7);
		removeBlockSpiralIdolInventoryItem = getInstanceWithNoRange(COMMON_BUILDER, name,
				"Equip in either hand to activate. The idol will remove blocks in an expandinf spiral.", 5,
				splParticles);
		
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
