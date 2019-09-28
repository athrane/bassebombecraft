package bassebombecraft.world.dimension;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import com.typesafe.config.Config;

/**
 * Helper class form creation dimensions.
 */
public class DimensionUtils {

	static final boolean LOAD_AT_ALL_TIMES = true;
	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = DimensionUtils.class.getSimpleName();
	
	public void initializeDimension() {
		
		Config configuration = getBassebombeCraft().getConfiguration();
		int dimensionId = configuration.getInt(CONFIG_KEY + ".Id");
		
	
		//DimensionManager.unregisterDimension(dimensionId);
		//DimensionManager.unregisterProviderType(dimensionId );	
		
		// register dimension
        //Object dimensionType = DimensionType.register(ModConstants.MODID, "Looland", dimensionId, LoolandWorldProvider.class, LOAD_AT_ALL_TIMES);
		
		
		
		//DimensionManager.registerDimension(dimensionId, DimensionType.OVERWORLD);
		
		//createProviderFor(dim)
		//registerProviderType(dimensionId, DimensionUtils.class, LOAD_AT_ALL_TIMES);
		//DimensionManager.registerDimension(dimensionId, dimensionId);		
		
	}
}
