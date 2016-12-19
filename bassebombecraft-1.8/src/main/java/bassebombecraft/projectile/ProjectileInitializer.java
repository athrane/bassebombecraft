package bassebombecraft.projectile;

import static bassebombecraft.projectile.GenericEggProjectile.PROJECTILE_NAME;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bassebombecraft.BassebombeCraft;
import static bassebombecraft.ModConstants.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Class for initializing projectiles.
 */
public class ProjectileInitializer {

	/**
	 * Logger.
	 */
	static Logger logger = LogManager.getLogger(BassebombeCraft.class);

	/**
	 * Initialize projectile.
	 * 
	 * @param targetTab
	 *            tab that item is added to.
	 */
	public void initialize(BassebombeCraft mod, CreativeTabs targetTab) {
		int modEntityID = 0;

		// register egg projectile
		int trackingRange = 20;
		int updateFrequency = 10;
		boolean sendsVelocityUpdates = true;
		ResourceLocation location = new ResourceLocation(MODID, GenericEggProjectile.PROJECTILE_NAME);
		EntityRegistry.registerModEntity(location, GenericEggProjectile.class, PROJECTILE_NAME, ++modEntityID, mod, trackingRange, updateFrequency , sendsVelocityUpdates);
		logger.info("initializing item: " + GenericEggProjectile.PROJECTILE_NAME);
		
	}

	public static ProjectileInitializer getInstance() {
		return new ProjectileInitializer();
	}

}
