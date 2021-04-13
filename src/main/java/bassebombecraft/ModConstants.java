package bassebombecraft;

import static bassebombecraft.config.ModConfiguration.baconBazookaProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.baconBazookaProjectileEffectForce;
import static bassebombecraft.config.ModConfiguration.bearBlasterProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.bearBlasterProjectileEffectForce;
import static bassebombecraft.config.ModConfiguration.creeperCannonProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.creeperCannonProjectileEffectForce;
import static bassebombecraft.config.ModConfiguration.primedCreeperCannonProjectileEffectExplosion;
import static bassebombecraft.config.ModConfiguration.primedCreeperCannonProjectileEffectForce;

import bassebombecraft.entity.ai.goal.CommandersTargetGoal;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.event.block.ProcessBlockDirectivesEventHandler;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.inventory.InventoryItemActionStrategy;
import bassebombecraft.item.action.mist.block.GenericBlockSpiralFillMist;
import bassebombecraft.item.action.mist.entity.GenericEntityMist;
import bassebombecraft.item.composite.CompositeMagicItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.potion.effect.AggroPlayerEffect;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.DecreaseSizeEffect;
import bassebombecraft.potion.effect.IncreaseSizeEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.MobProjectileEffect;
import bassebombecraft.potion.effect.ReflectEffect;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Mod constants.
 */
public class ModConstants {

	/**
	 * Mod name.
	 */
	public static final String NAME = "BasseBombeCraft";

	/**
	 * Mod ID.
	 */
	public static final String MODID = "bassebombecraft";

	/**
	 * Mod version.
	 */
	public static final String VERSION = "1.16.5-2.1";

	/**
	 * In game tab name.
	 */
	public static final String TAB_NAME = "bassebombecraft";

	/**
	 * Forge domain.
	 */
	public static final String MINECRAFT_DOMAIN = "minecraft";

	/**
	 * TOML Configuration file post fix.
	 */
	public static final String CONFIG_FILE_TOML_POSTFIX = ".toml";

	/**
	 * Configuration file name.
	 */
	public static final String INTERNAL_TOML_CONFIG_FILE_NAME = MODID + "-common" + CONFIG_FILE_TOML_POSTFIX;

	/**
	 * Version check URL.
	 */
	public static final String VERSION_URL = "https://raw.githubusercontent.com/athrane/bassebombecraft/master/version.json";

	/**
	 * Analytics URL.
	 */
	public static final String ANALYTICS_URL = "http://www.google-analytics.com/collect";

	/**
	 * GA API version.
	 */
	public static final String GA_API_VERSION = "1";

	/**
	 * GA development Property.
	 */
	// public static final String GA_PROPERTY = "UA-91107600-1";

	/**
	 * GA Property.
	 */
	public static final String GA_PROPERTY = "UA-91418540-1";

	/**
	 * GA data source.
	 */
	public static final String GA_SOURCE = "app";

	/**
	 * GA application ID.
	 */
	public static final String GA_APP_ID = "bassebombecraft";

	/**
	 * GA event hit type.
	 */
	public static final String GA_HITTYPE_EVENT = "event";

	/**
	 * GA exception hit type.
	 */
	public static final String GA_HITTYPE_EXCEPTION = "exception";

	/**
	 * GA session start.
	 */
	public static final String GA_SESSION_START = "start";

	/**
	 * GA session end.
	 */
	public static final String GA_SESSION_END = "end";

	/**
	 * Number of HTTP threads used for analytics.
	 */
	public static final int NUMBER_HTTP_THREADS = 2;

	/**
	 * Null block tile entity.
	 */
	public static final TileEntity NULL_TILE_ENTITY = null;

	/**
	 * X origin coordinate.
	 */
	public static final int ORIGIN_X = 0;

	/**
	 * Y origin coordinate.
	 */
	public static final int ORIGIN_Y = 0;

	/**
	 * Z origin coordinate.
	 */
	public static final int ORIGIN_Z = 0;

	/**
	 * Origin block position.
	 */
	public static final BlockPos ORIGIN_BLOCK_POS = new BlockPos(ORIGIN_X, ORIGIN_Y, ORIGIN_Z);

	/**
	 * X unity coordinate.
	 */
	public static final int UNITY_X = 1;

	/**
	 * Y unity coordinate.
	 */
	public static final int UNITY_Y = 1;

	/**
	 * Z unity coordinate.
	 */
	public static final int UNITY_Z = 1;

	/**
	 * Unity block size.
	 */
	public static final BlockPos UNITY_BLOCK_SIZE = new BlockPos(UNITY_X, UNITY_Y, UNITY_Z);

	/**
	 * Flag which indicates that a block should't be harvested when processed.
	 */
	public static final boolean DONT_HARVEST = false;

	/**
	 * Flag which indicates that a block should be harvested when processed.
	 */
	public static final boolean HARVEST = true;

	/**
	 * Not a bad potion effect.
	 */
	public static final EffectType NOT_BAD_POTION_EFFECT = EffectType.NEUTRAL;

	/**
	 * Potion trigger time, measured in mob death time.
	 */
	public static final int POTION_MOB_DEATH_TIME_TRIGGER = 19;

	/**
	 * Potion default liquid color.
	 */
	public static final int POTION_LIQUID_COLOR = 1;

	/**
	 * Default book item cooldown value.
	 */
	public static final int ITEM_BOOK_DEFAULT_COOLDOWN = 10;

	/**
	 * Default book idol cooldown value.
	 */
	public static final int ITEM_IDOL_DEFAULT_COOLDOWN = 5;

	/**
	 * Default basic item cooldown value.
	 */
	public static final int ITEM_BASICITEM_DEFAULT_COOLDOWN = 10;

	/**
	 * Default item tooltip value.
	 */
	public static final String ITEM_DEFAULT_TOOLTIP = "N/A";

	/**
	 * Mod structure world generator weight.
	 */
	@Deprecated
	public static final int MOD_STRUCUTRE_GENERATOR_WEIGHT = 1;

	/**
	 * Particle spawn frequency in {@linkplain GenericEntityMist}. Measured in world
	 * ticks.
	 */
	@Deprecated
	public static final int PARTICLE_SPAWN_FREQUENCY = 10;

	/**
	 * Effect update frequency in {@linkplain GenericBlockSpiralFillMist}. Measured
	 * in world ticks.
	 */
	public static final int BLOCK_EFFECT_FREQUENCY = 3;

	/**
	 * Effect update frequency in {@linkplain GenericEntityMist}. Measured in world
	 * ticks.
	 */
	public static final int MIST_EFFECT_FREQUENCY = 5;

	/**
	 * Number of processed blocks per game tick in
	 * {@linkplain ProcessBlockDirectivesEventHandler}.
	 */
	public static final int BLOCKS_PER_TICK = 3;

	/**
	 * Aggro duration for AI Commanded team members during self destruct in
	 * {@linkplain CommandersTargetGoal}.
	 */
	public static final int AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO = 1000;

	/**
	 * Fire duration for AI Commanded team members during self destruct in
	 * {@linkplain CommandersTargetGoal}.
	 */
	public static final int AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE = 1000;

	/**
	 * Update frequency for {@linkplain CompanionAttack}. Measured in ticks.
	 */
	public static final int AI_COMPANION_ATTACK_UPDATE_FREQUENCY = 10;

	/**
	 * Update frequency for path recalculation in AI goals. Measured in ticks.
	 */
	public static final int AI_PATH_RECALC_UPDATE_FREQUENCY = 10;

	/**
	 * Minimum range for close quarters attacks in {@linkplain CompanionAttack}.
	 * Measured in blocks.
	 */
	public static final int AI_COMPANION_ATTACK_MINIMUM_RANGE = 5;

	/**
	 * AI watch distance (in blocks) for entity to look at a target in AI goals.
	 */
	public static final int AI_TARGET_WATCH_DIST = 10;

	/**
	 * Rendering: Line width for rendering billboards.
	 */
	public static final int BILLBOARD_LINE_WIDTH = 1;

	/**
	 * Rendering: Triangle height for equilateral triangle with side length 1.
	 */
	public static final float EQUILATERAL_TRIANGLE_HEIGHT = 0.866F;

	/**
	 * HUD item: Team target label.
	 */
	public static final String TARGET_LABEL = "Target";

	/**
	 * HUD item: displacement of text.
	 */
	public static final double HUD_TEXT_DISP = 0.25;

	/**
	 * HUD Item: Number of targets to render.
	 */
	public static final int TEAM_MEMBERS_TO_RENDER = 7;

	/**
	 * Defines should be effect only.
	 */
	public static final boolean LIGHTNING_BOLT_NOT_EFFECT_ONLY = false;

	/**
	 * Basic items config path prefix in TOML configuration file.
	 */
	@Deprecated
	public static final String BASICITEMS_CONFIGPATH = "BasicItems.";

	/**
	 * Potions config path prefix in TOML configuration file.
	 */
	public static final String POTIONS_CONFIGPATH = "Potions.";

	/**
	 * Books config path prefix in TOML configuration file.
	 */
	public static final String BOOKS_CONFIGPATH = "Books.";

	/**
	 * Charmed mob configuration identifier.
	 */
	public static final String CHARMED_MOB_NAME = "CharmedMob";

	/**
	 * Reflect effect configuration identifier.
	 */
	public static final String REFLECT_EFFECT_NAME = "ReflectEffect";

	/**
	 * Processed block directives configuration identifier.
	 */
	public static final String PROCESSED_BLOCK_DIRECTIVES_NAME = "ProcessBlockDirectives";

	/**
	 * Week amplification potion name.
	 */
	public static final String WEAK_AMPLIFICATION_POTION_NAME = "WeakAmplificationPotion";

	/**
	 * Amplification potion name.
	 */
	public static final String AMPLIFICATION_POTION_NAME = "AmplificationPotion";

	/**
	 * Superior amplification potion name.
	 */
	public static final String SUPERIOR_AMPLIFICATION_POTION_NAME = "SuperiorAmplificationPotion";

	/**
	 * Mob aggro potion name.
	 */
	public static final String MOB_AGGRO_POTION_NAME = "MobAggroPotion";

	/**
	 * Mob priming potion name.
	 */
	public static final String MOB_PRIMING_POTION_NAME = "MobPrimingPotion";

	/**
	 * Bacon bazooka projectile effect name.
	 */
	public static final String BACONBAZOOKA_EFFECT_NAME = "BaconBazookaProjectileEffect";

	/**
	 * Bear blaster projectile effect name.
	 */
	public static final String BEARBLASTER_EFFECT_NAME = "BearBlasterProjectileEffect";

	/**
	 * Creeper cannon projectile effect name.
	 */
	public static final String CREEPERCANNON_EFFECT_NAME = "CreeperCannonProjectileEffect";

	/**
	 * Primed creeper cannon projectile effect name.
	 */
	public static final String PRIMEDCREEPERCANNON_EFFECT_NAME = "PrimedCreeperCannonProjectileEffect";

	/**
	 * Aggro player effect, used by {@linkplain MobRespawningEffectEventHandler}.
	 */
	public static final Effect AGGRO_PLAYER_EFFECT = new AggroPlayerEffect();

	/**
	 * Primed mob effect, used by {@linkplain PrimeMobIdolInventoryItem}.
	 */
	public static final Effect MOB_PRIMING_EFFECT = new MobPrimingEffect();

	/**
	 * Bacon Bazooka effect, used by {@linkplain ShootBaconBazooka}.
	 */
	public static final Effect BACON_BAZOOKA_EFFECT = new MobProjectileEffect(baconBazookaProjectileEffectForce.get(),
			baconBazookaProjectileEffectExplosion.get());

	/**
	 * Bear blaster effect, used by {@linkplain ShootBearBlaster}.
	 */
	public static final Effect BEAR_BLASTER_EFFECT = new MobProjectileEffect(bearBlasterProjectileEffectForce.get(),
			bearBlasterProjectileEffectExplosion.get());

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect CREEPER_CANNON_EFFECT = new MobProjectileEffect(creeperCannonProjectileEffectForce.get(),
			creeperCannonProjectileEffectExplosion.get());

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect PRIMED_CREEPER_CANNON_EFFECT = new MobProjectileEffect(
			primedCreeperCannonProjectileEffectForce.get(), primedCreeperCannonProjectileEffectExplosion.get());

	/**
	 * Potion amplifier effect.
	 */
	public static final Effect AMPLIFIER_EFFECT = new AmplifierEffect();

	/**
	 * Reflect effect.
	 */
	public static final Effect REFLECT_EFFECT = new ReflectEffect();

	/**
	 * Increase size effect.
	 */
	public static final Effect INCREASE_SIZE_EFFECT = new IncreaseSizeEffect();

	/**
	 * Decrease size effect.
	 */
	public static final Effect DECREASE_SIZE_EFFECT = new DecreaseSizeEffect();

	/**
	 * Range value for non-AOE effect in {@linkplain InventoryItemActionStrategy}
	 * implementation.
	 */
	public static final int NOT_AN_AOE_EFFECT = 1;

	/**
	 * Entity attribute to define an entity as a decoy.
	 */
	public static final Attribute DECOY = new RangedAttribute("bassebombecraft.decoy", 1.0D, 0.0D, 1.0D)
			.setShouldWatch(true);

	/**
	 * Entity attribute to tag an entity for respawn.
	 */
	public static final Attribute RESPAWN = new RangedAttribute("bassebombecraft.respawn", 1.0D, 0.0D, 1.0D)
			.setShouldWatch(true);

	/**
	 * Entity attribute to tag an entity as respawned.
	 */
	public static final Attribute IS_RESPAWNED = new RangedAttribute("bassebombecraft.isrespawned", 1.0D, 0.0D, 1.0D)
			.setShouldWatch(true);

	/**
	 * Maximum size composite magic. Used in {@linkplain CompositeMagicItem}.
	 */
	public static final int COMPOSITE_MAX_SIZE = 6;

	/**
	 * PF class suffix for composite item.
	 */
	public static final String PF_SUFFIX = "ProjectileFormationItem";

	/**
	 * PFM class suffix for composite item.
	 */
	public static final String PFM_SUFFIX = "ProjectileFormationModifierItem";

	/**
	 * P class suffix for composite item.
	 */
	public static final String P_SUFFIX = "ProjectileItem";

	/**
	 * PP class suffix for composite item.
	 */
	public static final String PP_SUFFIX = "ProjectilePathItem";

	/**
	 * PM class suffix for composite item.
	 */
	public static final String PM_SUFFIX = "ProjectileModifierItem";

	/**
	 * Unknown class suffix for composite item.
	 */
	public static final String UNKNOWN_SUFFIX = "Unknown type";

	/**
	 * Null arguments for I18N.
	 */
	public static final Object[] NULL_I18N_ARGS = new Object[0];

	/**
	 * Mob damage type.
	 */
	public static final String MOB_DAMAGE = "mob";

}
