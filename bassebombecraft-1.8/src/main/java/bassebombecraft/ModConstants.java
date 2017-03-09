package bassebombecraft;

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
    public static final String VERSION = "1.11-1.25";
    
    /**
     * In game tab name.
     */
    public static final String TAB_NAME= "BasseBombeCraft";        
    
    /**
     * Forge domain.
     */
    public static final String MINECRAFT_DOMAIN= "minecraft";
    
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
	 * Lightning isn't created with effect only, but with block changes.
	 */
	public static final boolean LIGHTNING_NOT_EFFECT_ONLY = false; 
	
	/**
	 * Not a bad potion effect.
	 */
	public static final boolean NOT_BAD_POTION_EFFECT = false;
	
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
	
}
