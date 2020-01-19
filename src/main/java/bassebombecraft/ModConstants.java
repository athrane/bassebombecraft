package bassebombecraft;

import static bassebombecraft.BassebombeCraft.getItemGroup;

import javax.vecmath.Vector4f;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.ai.goal.CommandersTargetGoal;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.event.block.ProcessBlockDirectivesEventHandler;
import bassebombecraft.event.particle.ParticleRenderingEventHandler;
import bassebombecraft.event.potion.MobRespawningEffectEventHandler;
import bassebombecraft.item.action.ShootBaconBazooka;
import bassebombecraft.item.action.ShootBearBlaster;
import bassebombecraft.item.action.ShootCreeperCannon;
import bassebombecraft.item.action.inventory.InventoryItemActionStrategy;
import bassebombecraft.item.basic.HudItem;
import bassebombecraft.item.book.BuildMineBook;
import bassebombecraft.item.inventory.MobsAggroIdolInventoryItem;
import bassebombecraft.item.inventory.PrimeMobIdolInventoryItem;
import bassebombecraft.potion.effect.AmplifierEffect;
import bassebombecraft.potion.effect.MobAggroEffect;
import bassebombecraft.potion.effect.MobPrimingEffect;
import bassebombecraft.potion.effect.MobProjectileEffect;
import bassebombecraft.potion.effect.MobRespawningEffect;
import bassebombecraft.potion.effect.PlayerAggroEffect;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;

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
	public static final String VERSION = "1.14.4-1.37";

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
	 * Download URL.
	 */
	public static final String DOWNLOAD_URL = "http://minecraft.curseforge.com/projects/bassebombecraft";

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
	//public static final String GA_PROPERTY = "UA-91107600-1";

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
	public static final int MOD_STRUCUTRE_GENERATOR_WEIGHT = 1;

	/**
	 * HUD Item.
	 */
	public static final HudItem HUD_ITEM = new HudItem();

	/**
	 * Build mine book.
	 */	
	public static final BuildMineBook BUILD_MINE_BOOK = new BuildMineBook();
		
	/**
	 * Particle spawn frequency in {@linkplain ParticleRenderingEventHandler},
	 * {@linkplain GenericBlockSpiralFillMist} and {@linkplain GenericEntityMist}.
	 * Measured in world ticks.
	 */
	public static final int PARTICLE_RENDERING_FREQUENCY = 3;
		
	/**
	 * Particle spawn frequency in {@linkplain CharmedMobEventHandler}.
	 * Measured in world ticks.
	 */
	public static final int CHARM_PARTICLE_RENDERING_FREQUENCY = 20;

	/**
	 * Effect update frequency in {@linkplain GenericBlockSpiralFillMist}.
	 * Measured in world ticks.
	 */
	public static final int BLOCK_EFFECT_FREQUENCY = 3;

	/**
	 * Effect update frequency in {@linkplain GenericEntityMist}.
	 * Measured in world ticks.
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
	 * Update frequency for {@linkplain FollowClosestPlayer}. Measured in ticks.
	 */
	public static final int AI_FOLLOW_CLOEST_PLAYER_UPDATE_FREQUENCY = 10;
	
	/**
	 * Minimum range for close quarters attacks in {@linkplain CompanionAttack}.
	 * Measured in blocks.
	 */
	public static final int AI_COMPANION_ATTACK_MINIMUM_RANGE = 5;

	/**
	 * Item properties which places item in tab.
	 */
	public static final Properties ITEM_PROPERTIES = new Item.Properties().group(getItemGroup());

	/**
	 * Rendering: Line width for rendering billboards.
	 */
	public static final int BILLBOARD_LINE_WIDTH = 1;

	/**
	 * Rendering: Triangle height for equilateral triangle with side length 1.
	 */
	public static final float EQUILATERAL_TRIANGLE_HEIGHT = 0.866F;

	/**
	 * Rendering: Text scale
	 */
	public static final float TEXT_SCALE = 0.02F;

	/**
	 * Rendering: Text color.
	 */
	//public static final int TEXT_COLOR = 0xFFFFFF;
	public static final int TEXT_COLOR = 0x00C000;

	/**
	 * Rendering: Angle for rotation of text billboard.
	 */
	public static final int TEXT_BILLBOARD_ANGLE = 180;

	/**
	 * Rendering: Rotation of text billboard.
	 */
	public static final Vector4f TEXT_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, TEXT_BILLBOARD_ANGLE);

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
	 * HUD Item: Ray trace range in blocks.
	 */
	public static final double RAYTRACE_RANGE = 20;
	
	/**
	 * HUD Item: Ray trace mode for fluids.
	 */
	public static final FluidMode RAYTRACE_FLUIDS = RayTraceContext.FluidMode.ANY;

	/**
	 * HUD Item: Ray trace mode for blocks.
	 */
	public static final BlockMode RAYTRACE_OUTLINE = RayTraceContext.BlockMode.OUTLINE;
	
	/**
	 * Defines should be effect only.
	 */
	public static final boolean LIGHTNING_BOLT_NOT_EFFECT_ONLY = false;

	/**
	 * Basic items config path prefix in TOML configuration file.
	 */
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
	 * Week amplification potion name.
	 */
	public final static String WEAK_AMPLIFICATION_POTION_NAME = "WeakAmplificationPotion";
	
	/**
	 * Amplification potion name.
	 */
	public final static String AMPLIFICATION_POTION_NAME = "AmplificationPotion";

	/**
	 * Superior amplification potion name.
	 */
	public final static String SUPERIOR_AMPLIFICATION_POTION_NAME = "SuperiorAmplificationPotion";

	/**
	 * Mob aggro potion name.
	 */
	public final static String MOB_AGGRO_POTION_NAME = "MobAggroPotion";

	/**
	 * Mob priming potion name.
	 */
	public final static String MOB_PRIMING_POTION_NAME = "MobPrimingPotion";

	/**
	 * Mob respawning potion name.
	 */
	public final static String MOB_RESPAWNING_POTION_NAME = "MobRespawningPotion";
	
	/**
	 * Bacon bazooka projectile effect name.
	 */
	public final static String BACONBAZOOKA_EFFECT_NAME = "BaconBazookaProjectileEffect";

	/**
	 * Bear blaster projectile effect name.
	 */
	public final static String BEARBLASTER_EFFECT_NAME = "BearBlasterProjectileEffect";

	/**
	 * Creeper cannon projectile effect name.
	 */
	public final static String CREEPERCANNON_EFFECT_NAME = "CreeperCannonProjectileEffect";

	/**
	 * Primed creeper cannon projectile effect name.
	 */
	public final static String PRIMEDCREEPERCANNON_EFFECT_NAME = "PrimedCreeperCannonProjectileEffect";
	
	/**
	 * Mobs aggro effect, used by {@linkplain MobsAggroIdolInventoryItem}.
	 */
	public static final Effect MOB_AGGRO_EFFECT = new MobAggroEffect();

	/**
	 * Player aggro effect, used by {@linkplain MobRespawningEffectEventHandler}.
	 */
	public static final Effect PLAYER_AGGRO_EFFECT = new PlayerAggroEffect();

	/**
	 * Primed mob effect, used by {@linkplain PrimeMobIdolInventoryItem}.
	 */
	public static final Effect MOB_PRIMING_EFFECT = new MobPrimingEffect();

	/**
	 * Mobs respawn effect.
	 */
	public static final Effect MOB_RESPAWNING_EFFECT = new MobRespawningEffect();

	/**
	 * Bacon Bazooka effect, used by {@linkplain ShootBaconBazooka}.
	 */
	public static final Effect BACON_BAZOOKA_EFFECT = new MobProjectileEffect(
			ModConfiguration.baconBazookaProjectileEffectForce.get(),
			ModConfiguration.baconBazookaProjectileEffectExplosion.get());
	
	/**
	 * Bear blaster effect, used by {@linkplain ShootBearBlaster}.
	 */
	public static final Effect BEAR_BLASTER_EFFECT = new MobProjectileEffect(
			ModConfiguration.bearBlasterProjectileEffectForce.get(),
			ModConfiguration.bearBlasterProjectileEffectExplosion.get());
	
	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect CREEPER_CANNON_EFFECT = new MobProjectileEffect(
			ModConfiguration.creeperCannonProjectileEffectForce.get(),
			ModConfiguration.creeperCannonProjectileEffectExplosion.get());

	/**
	 * Creeper cannon effect, used by {@linkplain ShootCreeperCannon}.
	 */
	public static final Effect PRIMED_CREEPER_CANNON_EFFECT = new MobProjectileEffect(
			ModConfiguration.primedCreeperCannonProjectileEffectForce.get(),
			ModConfiguration.primedCreeperCannonProjectileEffectExplosion.get());	

	/**
	 * Potion amplifier effect.
	 */
	public static final Effect AMPLIFIER_EFFECT = new AmplifierEffect();

	/**
	 * Range value for non-AOE effect in {@linkplain InventoryItemActionStrategy} implmentation.
	 */
	public static final int NOT_AN_AOE_EFFECT = 1;
	
}
