package bassebombecraft.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.Optional;
import java.util.Random;

import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.player.PlayerDirection;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
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
		double x = entity.getPosX() + (lookVec.x * spawnDisplacement);
		double y = entity.getPosY() + entity.getEyeHeight();
		double z = entity.getPosZ() + (lookVec.z * spawnDisplacement);

		// set spawn position
		projectileEntity.setPosition(x, y, z);
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
	 * Return true if entity is an expected type.
	 * 
	 * @param entity entity to test.
	 * @param type   type to test for.
	 * 
	 * @return true if entity is the expected type.
	 */
	public static boolean isType(Entity entity, Class<?> type) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return type.isInstance(oe.get());
		return false;
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
	 * return true if entity is a {@linkplain ParrotEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain ParrotEntity}.
	 */
	public static boolean isTypeParrotEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof ParrotEntity;
		return false;
	}

	/**
	 * return true if entity is a {@linkplain BeeEntity}.
	 * 
	 * @param entity entity to test.
	 * 
	 * @return true if entity is a {@linkplain BeeEntity}.
	 */
	public static boolean isTypeBeeEntity(Entity entity) {
		Optional<Entity> oe = Optional.ofNullable(entity);
		if (oe.isPresent())
			return oe.get() instanceof BeeEntity;
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
		double feetPosY = entity.getPosY() - entity.getYOffset();
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
		if (isTypeCreatureEntity(entity)) {
			CreatureEntity creatureEntity = (CreatureEntity) entity;
			return Optional.ofNullable(creatureEntity.getAttackTarget());
		}
		return Optional.ofNullable(entity.getLastAttackedEntity());
	}

	/**
	 * Return a (hopefully) live target.
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
	 * Resolve the entity target.
	 * 
	 * @param target    some nearby mob.
	 * @param commander invoker of the effect.
	 * 
	 * @return resolved target.
	 */
	public static LivingEntity resolveTarget(Entity target, LivingEntity invoker) {

		// if invoker is a player then get the player target.
		if (isTypePlayerEntity(invoker)) {

			// type cast
			PlayerEntity player = (PlayerEntity) invoker;

			// get player target
			TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
			Optional<LivingEntity> optTarget = repository.getFirst(player);

			// return player target if defined
			if (optTarget.isPresent())
				return optTarget.get();
		}

		// if target is living entity then cast cast and return it
		if (isTypeLivingEntity(target)) {
			return (LivingEntity) target;
		}

		return null;
	}

	/**
	 * Set entity to be aggro'ed. Aggro'ing is only supported for
	 * {@linkplain MobEntity}.
	 * 
	 * @param entity entity to set aggro'ed if it is a {@linkplain MobEntity}.
	 */
	public static void setMobEntityAggroed(Entity entity) {
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

	/**
	 * Self-destruct entity.
	 * 
	 * @param entity entity to self-destruct.
	 */
	public static void selfDestruct(MobEntity entity) {
		entity.setFire(AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);
		entity.setHealth(0);
	}

	/**
	 * Returns true if minimum distance is reached between two entities.
	 *
	 * @param entity         entity #1
	 * @param entity2        entity #2
	 * @param minDistanceSqr the minimum distance squared.
	 * 
	 * @return true if minimum distance is reached.
	 */
	public static boolean isMinimumDistanceReached(Entity entity, Entity entity2, double minDistanceSqr) {
		double distSqr = entity.getDistanceSq(entity2);
		return (minDistanceSqr > distSqr);
	}

}
