package bassebombecraft.entity;

import bassebombecraft.player.PlayerDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
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

	/**
	 * return true if entity is a {@linkplain EntityCreature}.
	 * 
	 * @param entity
	 *            entity to test.
	 * 
	 * @return true if entity is a {@linkplain EntityCreature}.
	 */
	public static boolean isEntityCreature(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof EntityCreature;
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity
	 *            player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static double calculateEntityFeetPositition(EntityLivingBase entity) {
		double feetPosY = entity.posY - entity.getYOffset();
		return feetPosY;
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity
	 *            player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static int calculateEntityFeetPosititionAsInt(EntityLivingBase entity) {
		return (int) calculateEntityFeetPositition(entity);
	}

	/**
	 * Return entity direction as an integer between 0 to 3: 0 when looking
	 * south, 1 when looking West, 2 looking North and 3 looking East.
	 * 
	 * @param entity
	 *            entity object.
	 * 
	 * @return player direction as an integer between 0 to 3.
	 */
	public static PlayerDirection getPlayerDirection(EntityLivingBase entity) {
		int direction = MathHelper.floor((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		return PlayerDirection.getById(direction);
	}
	
}
