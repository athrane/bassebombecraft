package bassebombecraft.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Utility class for entity manipulation.
 */
public class EntityUtils {

	/**
	 * Explosion will make smoke.
	 */
	static final boolean IS_SMOKING = true;

	/**
	 * Calculate spawn position for projectile entity
	 * 
	 * @param entity
	 *            entity shooting the projectile entity
	 * @param projectileEntity
	 *            projectile entity
	 * @param spawnDisplacement
	 *            XZ spawn displacement from shooting enity.
	 */
	public static void setProjectileEntityPosition(EntityLivingBase entity, EntityLivingBase projectileEntity,
			int spawnDisplacement) {
		Vec3d lookVec = entity.getLookVec();

		// calculate spawn projectile spawn position
		double x = entity.posX + (lookVec.xCoord * spawnDisplacement);
		double y = entity.posY + entity.getEyeHeight();
		double z = entity.posZ + (lookVec.zCoord * spawnDisplacement);

		// set spawn position
		projectileEntity.posX = x;
		projectileEntity.posY = y;
		projectileEntity.posZ = z;
		projectileEntity.prevRotationYaw = projectileEntity.rotationYaw = projectileEntity.rotationYawHead = entity.rotationYaw;
		projectileEntity.prevRotationPitch = projectileEntity.rotationPitch = entity.rotationPitch;
	}

	/**
	 * Setup explosion at entity position.
	 * 
	 * @param entity
	 *            entity where explosion will happen.
	 * @param world
	 *            world
	 * @param size
	 *            explosion size in blocks.
	 */
	public static void explode(EntityLivingBase entity, World world, int size) {
		world.createExplosion(entity, entity.getPosition().getX(), entity.getPosition().getY(),
				entity.getPosition().getZ(), size, IS_SMOKING);
	}

}
