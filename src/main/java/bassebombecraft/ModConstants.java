package bassebombecraft;

import static bassebombecraft.BassebombeCraft.getItemGroup;

import javax.vecmath.Vector4f;

import bassebombecraft.entity.ai.goal.AiCommandersTargeting;
import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.event.block.ProcessBlockDirectivesEventHandler;
import bassebombecraft.event.particle.ParticleRenderingEventHandler;
import bassebombecraft.item.basic.HudItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
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
	public static final String VERSION = "1.14.4-1.31-beta";

	/**
	 * In game tab name.
	 */
	public static final String TAB_NAME = "bassebombecraft";

	/**
	 * Forge domain.
	 */
	public static final String MINECRAFT_DOMAIN = "minecraft";

	/**
	 * Configuration file post fix.
	 */
	public static final String CONFIG_FILE_POSTFIX = ".conf";

	/**
	 * Configuration file name.
	 */
	public static final String INTERNAL_CONFIG_FILE_NAME = MODID + CONFIG_FILE_POSTFIX;

	/**
	 * Download URL.
	 */
	public static final String DOWNLOAD_URL = "http://minecraft.curseforge.com/projects/bassebombecraft";

	/**
	 * Version check URL.
	 */
	public static final String VERSION_URL = "https://raw.githubusercontent.com/athrane/bassebombecraft/master/bassebombecraft-1.8/version.json";

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
	public static final HudItem TARGETING_OVERLAY_ITEM = new HudItem();

	/**
	 * Rendering frequency for particles. Measured in world ticks.
	 */
	public static final int RENDERING_FREQUENCY = 10;

	/**
	 * Particle spawn frequency in {@linkplain ParticleRenderingEventHandler}.
	 * Measured in world ticks.
	 */
	public static final int SPAWN_PARTICLES_FREQUENCY = 40;

	/**
	 * Number of processed blocks per game tick in
	 * {@linkplain ProcessBlockDirectivesEventHandler}.
	 */
	public static final int BLOCKS_PER_TICK = 3;

	/**
	 * Aggro duration for AI Commanded team members during self destruct in
	 * {@linkplain AiCommandersTargeting}.
	 */
	public static final int AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_AGGRO = 1000;

	/**
	 * Fire duration for AI Commanded team members during self destruct in
	 * {@linkplain AiCommandersTargeting}.
	 */
	public static final int AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE = 1000;

	/**
	 * Update frequency for {@linkplain CompanionAttack}. Measured in ticks.
	 */
	public static final int AI_COMPANION_ATTACK_UPDATE_FREQUENCY = 10;

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
	public static final float TEXT_SCALE = 0.03F;

	/**
	 * Rendering: Text color.
	 */
	public static final int TEXT_COLOR = 0xFFFFFF;

	/**
	 * Rendering: Angle for rotation of text billboard.
	 */
	public static final int TEXT_BILLBOARD_ANGLE = 180;

	/**
	 * Rendering: Rotation of text billboard.
	 */
	public static final Vector4f TEXT_BILLBOARD_ROTATION = new Vector4f(0.0F, 0.0F, 1.0F, TEXT_BILLBOARD_ANGLE);

	/**
	 * Defines should be effect only.
	 */
	public static final boolean LIGHTNING_BOLT_NOT_EFFECT_ONLY = false;

}