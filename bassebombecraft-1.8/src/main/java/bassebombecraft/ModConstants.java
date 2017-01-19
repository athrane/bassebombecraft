package bassebombecraft;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Mod constants.
 */
public class ModConstants {

	public static final String NAME = "BasseBombeCraft";
    public static final String MODID = "bassebombecraft";
    public static final String VERSION = "1.11-1.21";
    public static final String TAB_NAME= "BasseBombeCraft";        
    public static final String MINECRAFT_DOMAIN= "minecraft";
    public static final String DOWNLOAD_URL = "http://minecraft.curseforge.com/projects/bassebombecraft";
    public static final String VERSION_URL = "https://raw.githubusercontent.com/athrane/bassebombecraft/master/bassebombecraft-1.8/version.json";        
    
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
	
}
