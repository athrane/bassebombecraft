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
import net.minecraft.entity.monster.CreeperEntity;
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
	 * Kill entity and remove it from the game.
	 * 
	 * @param entity entity to be killed.
	 */
	public static void killEntity(LivingEntity entity) {

		// kill target
		entity.onKillCommand();
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
	 * return true if entity is a {@linkplain CreeperEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain CreeperEntity}.
	 */
	public static boolean isTypeCreeperEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof CreeperEntity;
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
		Optional<LivingEntity> target = getNullableTarget(entity);
		if (target.isPresent())
			return target.get().isAlive();
		return false;
	}

	/**
	 * Returns true if entity has a attacking target defined .
	 * 
	 * @param entity entity to query.
	 * 
	 * @return if entity has a attacking target defined.
	 */
	public static boolean hasTarget(LivingEntity entity) {
		Optional<LivingEntity> target = getNullableTarget(entity);
		return (target.isPresent());
	}

	/**
	 * Return a target.
	 * 
	 * @param entity to get target from.
	 * 
	 * @return target from entity.
	 */
	public static Optional<LivingEntity> getNullableTarget(LivingEntity entity) {
		if (EntityUtils.isTypeCreatureEntity(entity)) {
			CreatureEntity creatureEntity = (CreatureEntity) entity;
			return Optional.ofNullable(creatureEntity.getAttackTarget());
		}
		return Optional.ofNullable(entity.getLastAttackedEntity());
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
	public static LivingEntity getTarget(LivingEntity entity) {

		// get attack target if type is creature entity
		if (EntityUtils.isTypeCreatureEntity(entity)) {
			CreatureEntity typedEntity = (CreatureEntity) entity;
			return typedEntity.getAttackTarget();
		} else {
			return entity.getLastAttackedEntity();
		}
	}

	/**
	 * Returns true if entity supports targeting, i.e. a target can be set at
	 * entity.
	 * 
	 * Targeting is supported for {@linkplain CreatureEntity} and
	 * {@linkplain LivingEntity}.
	 * 
	 * @param entity to test.
	 * 
	 * @return true if entity supports targeting, i.e. a target can be set at
	 *         entity.
	 */
	public static boolean supportTargeting(Entity entity) {
		if (isTypeCreatureEntity(entity))
			return true;
		if (isTypeLivingEntity(entity))
			return true;
		return false;
	}

	/**
	 * Set target as either {@linkplain CreatureEntity} or
	 * {@linkplain LivingEntity}.
	 * 
	 * @param entity    entity to set target for.
	 * @param newTarget target to set.
	 */
	public static void setTarget(Entity entity, LivingEntity newTarget) {
		if (isTypeCreatureEntity(entity)) {
			CreatureEntity creatureEntity = (CreatureEntity) entity;
			creatureEntity.setAttackTarget(newTarget);
			return;
		}
		if (isTypeLivingEntity(entity)) {
			LivingEntity livingEntity = (LivingEntity) entity;
			livingEntity.setLastAttackedEntity(newTarget);
			livingEntity.setRevengeTarget(newTarget);
		}
	}

	/**
	 * Set entity to be aggro'ed. Aggro'ing is only supported for
	 * {@linkplain MobEntity}.
	 * 
	 * @param entity entity to set aggro'ed if it is a {@linkplain MobEntity}.
	 */
	public static void setMobEntityAggroed(Entity entity) {
		// set mob to be aggro'ed
		if (isTypeMobEntity(entity)) {
			MobEntity mobEntity = (MobEntity) entity;
			mobEntity.setAggroed(true);
		}
	}

	/**
	 * Set a random spawn position for living entity.
	 * 
	 * @param pos         block position to spawn from.
	 * @param rotationYaw rotation yaw to spawn from.
	 * @param spawnArea   spawn areas in blocks.
	 * @param entity      entity to set position for.
	 */
	public static void setRandomSpawnPosition(BlockPos pos, float rotationYaw, int spawnArea, LivingEntity entity) {
		setRandomSpawnPosition(pos, rotationYaw, spawnArea, (Entity) entity);
	}

	/**
	 * Set a random spawn position for entity.
	 * 
	 * @param pos         block position to spawn from.
	 * @param rotationYaw rotation yaw to spawn from.
	 * @param spawnArea   spawn areas in blocks.
	 * @param entity      entity to set position for.
	 */
	public static void setRandomSpawnPosition(BlockPos pos, float rotationYaw, int spawnArea, Entity entity) {
		Random random = getBassebombeCraft().getRandom();
		int randomX = random.nextInt(spawnArea) - (spawnArea / 2);
		int randomZ = random.nextInt(spawnArea) - (spawnArea / 2);
		double positionX = pos.getX() + randomX;
		double positionY = pos.getY();
		double positionZ = pos.getZ() + randomZ;
		entity.setLocationAndAngles(positionX, positionY, positionZ, rotationYaw, PITCH);
	}

	/**
	 * Calculate random YAW (counterclockwise rotation about the z-axis) for entity.
	 * 
	 * @return random YAW .
	 */
	public static float calculateRandomYaw() {
		return getBassebombeCraft().getRandom().nextFloat() * 360.0F;
	}

}
