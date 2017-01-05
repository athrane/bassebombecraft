package bassebombecraft.projectile;

import static bassebombecraft.ModConstants.MODID;
import static bassebombecraft.projectile.GenericEggProjectile.PROJECTILE_NAME;

import bassebombecraft.BassebombeCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Class for initializing projectiles.
 */
public class ProjectileInitializer {

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
		EntityRegistry.registerModEntity(location, GenericEggProjectile.class, PROJECTILE_NAME, ++modEntityID, mod,
				trackingRange, updateFrequency, sendsVelocityUpdates);

	}

	public static ProjectileInitializer getInstance() {
		return new ProjectileInitializer();
	}

}
