package bassebombecraft.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;

import java.util.Optional;
import java.util.Random;

import bassebombecraft.entity.ai.goal.CompanionAttack;
import bassebombecraft.event.rendering.RenderingEventHandler;
import bassebombecraft.player.PlayerDirection;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Utility class for entity manipulation.
 */
public class EntityUtils {

	/*
	 * Spawn location rotation pitch
	 */
	static final float PITCH = 0.0F;

	/**
	 * Calculate spawn position for projectile entity
	 * 
	 * @param entity            entity shooting the projectile entity
	 * @param projectileEntity  projectile entity
	 * @param spawnDisplacement XZ spawn displacement from shooting enity.
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
	 * @param entity entity where explosion will happen.
	 * @param world  world
	 * @param size   explosion size in blocks.
	 */
	public static void explode(LivingEntity entity, World world, int size) {
		world.createExplosion(entity, entity.getPosition().getX(), entity.getPosition().getY(),
				entity.getPosition().getZ(), size, Explosion.Mode.DESTROY);
	}

	/**
	 * return true if entity is a {@linkplain CreatureEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain CreatureEntity}.
	 */
	public static boolean isTypeCreatureEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof CreatureEntity;
		return false;
	}

	/**
	 * Return true if entity is a {@linkplain MobEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain MobEntity}.
	 */
	public static boolean isTypeMobEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof MobEntity;
		return false;
	}

	/**
	 * Return true if entity is a {@linkplain LivingEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain LivingEntity}.
	 */
	public static boolean isTypeLivingEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof LivingEntity;
		return false;
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity player object.
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
	 * @param entity player object.
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
	 * @param entity entity object.
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
	 * @param entity entity to query.
	 * 
	 * @return if entity has a attacking target defined which is alive.
	 */
	public static boolean hasAliveTarget(LivingEntity entity) {
		Optional<LivingEntity> target = null;

		// get attack target if type is creature entity
		if (EntityUtils.isTypeCreatureEntity(entity)) {
			CreatureEntity creatureEntity = (CreatureEntity) entity;
			target = Optional.ofNullable(creatureEntity.getAttackTarget());
		} else {
			target = Optional.ofNullable(entity.getLastAttackedEntity());
		}

		if (target.isPresent())
			return target.get().isAlive();
		return false;
	}

	/**
	 * Return a (hopefully) live target. Use for targeting by the AI task
	 * {@linkplain CompanionAttack} and the target rendering in
	 * {@linkplain RenderingEventHandler}.
	 * 
	 * @param entity to get live target from.
	 * 
	 * @return live target from entity.
	 */
	public static LivingEntity getAliveTarget(LivingEntity entity) {

		// get attack target if type is creature entity
		if (EntityUtils.isTypeCreatureEntity(entity)) {
			CreatureEntity typedEntity = (CreatureEntity) entity;
			return typedEntity.getAttackTarget();
		} else {
			return entity.getLastAttackedEntity();
		}
	}

	/**
	 * Set a random spawn position for entity.
	 * 
	 * @param pos         block position to spawn from.
	 * @param rotationYaw rotation yaw to spawn from.
	 * @param spawnSize   spawn areas in blocks.
	 * @param entity      entity to set position for.
	 */
	public static void setRandomSpawnPosition(BlockPos pos, float rotationYaw, int spawnSize, LivingEntity entity) {
		Random random = getBassebombeCraft().getRandom();
		int randomX = random.nextInt(spawnSize) - (spawnSize / 2);
		int randomZ = random.nextInt(spawnSize) - (spawnSize / 2);
		double positionX = pos.getX() + randomX;
		double positionY = pos.getY();
		double positionZ = pos.getZ() + randomZ;
		entity.setLocationAndAngles(positionX, positionY, positionZ, rotationYaw, PITCH);
	}
}