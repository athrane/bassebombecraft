package bassebombecraft.entity;

import bassebombecraft.entity.ai.task.CompanionAttack;
import bassebombecraft.event.rendering.RenderingEventHandler;
import bassebombecraft.player.PlayerDirection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EntityMob;
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
	public static void setProjectileEntityPosition(LivingEntity entity, LivingEntity projectileEntity,
			int spawnDisplacement) {
		Vec3d lookVec = entity.getLookVec();

		// calculate spawn projectile spawn position
		double x = entity.posX + (lookVec.x * spawnDisplacement);
		double y = entity.posY + entity.getEyeHeight();
		double z = entity.posZ + (lookVec.z * spawnDisplacement);

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
	public static void explode(LivingEntity entity, World world, int size) {
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
	 * return true if entity is a {@linkplain EntityMob}.
	 * 
	 * @param entity
	 *            entity to test.
	 * 
	 * @return true if entity is a {@linkplain EntityMob}.
	 */
	public static boolean isEntityMob(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof EntityMob;
	}

	/**
	 * return true if entity is a {@linkplain LivingEntity}.
	 * 
	 * @param entity
	 *            entity to test.
	 * 
	 * @return true if entity is a {@linkplain LivingEntity}.
	 */
	public static boolean isLivingEntity(Entity entity) {
		if (entity == null)
			return false;
		return entity instanceof LivingEntity;
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity
	 *            player object.
	 * 
	 * @return player feet position (as a Y coordinate).
	 */
	public static double calculateEntityFeetPositition(LivingEntity entity) {
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
	public static int calculateEntityFeetPosititionAsInt(LivingEntity entity) {
		return (int) calculateEntityFeetPositition(entity);
	}

	/**
	 * Return entity direction as an integer between 0 to 3: 0 when looking south, 1
	 * when looking West, 2 looking North and 3 looking East.
	 * 
	 * @param entity
	 *            entity object.
	 * 
	 * @return player direction as an integer between 0 to 3.
	 */
	public static PlayerDirection getPlayerDirection(LivingEntity entity) {
		int direction = MathHelper.floor((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		return PlayerDirection.getById(direction);
	}

	/**
	 * Returns true if entity has a attacking target defined which is alive.
	 * 
	 * @param entity
	 *            entity to query.
	 * @return if entity has a attacking target defined which is alive.
	 */
	public static boolean hasAliveTarget(LivingEntity entity) {
		LivingEntity target = entity.getAttackTarget();
		if (target == null)
			return false;
		if (target.isDead)
			return false;
		return true;
	}

	/**
	 * Return a (hopefully) live target. Use for targeting by the AI task
	 * {@linkplain CompanionAttack} and the target rendering in
	 * {@linkplain RenderingEventHandler}.
	 * 
	 * @param entity
	 *            to get live target from.
	 * @return live target from entity.
	 */
	public static LivingEntity getAliveTarget(LivingEntity entity) {
		return entity.getAttackTarget();
	}

}
