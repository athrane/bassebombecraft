package bassebombecraft.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.BassebombeCraft.getProxy;
import static bassebombecraft.ModConstants.AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE;
import static bassebombecraft.ModConstants.MARKER_ATTRIBUTE_IS_SET;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.Optional;
import java.util.Random;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.event.entity.target.TargetRepository;
import bassebombecraft.player.PlayerDirection;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

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
		Vec3 lookVec = entity.getLookAngle();

		// calculate spawn projectile spawn position
		double x = entity.getX() + (lookVec.x * spawnDisplacement);
		double y = entity.getY() + entity.getEyeHeight();
		double z = entity.getZ() + (lookVec.z * spawnDisplacement);

		// set spawn position
		projectileEntity.setPos(x, y, z);
		float yRot = entity.getYRot();
		projectileEntity.yHeadRot = yRot;
		projectileEntity.yRotO = yRot;
		projectileEntity.setYRot(yRot);
		
		float xRot = entity.getXRot();
		projectileEntity.xRotO = xRot;
		projectileEntity.setXRot(xRot); 
	}

	/**
	 * Setup explosion at entity position.
	 * 
	 * @param entity entity where explosion will happen.
	 * @param world  world
	 * @param size   explosion size in blocks.
	 */
	public static void explode(LivingEntity entity, Level world, int size) {
		world.explode(entity, entity.blockPosition().getX(), entity.blockPosition().getY(),
				entity.blockPosition().getZ(), size, Explosion.BlockInteraction.DESTROY);
	}

	/**
	 * Kill entity and remove it from the game.
	 * 
	 * @param entity entity to be killed.
	 */
	public static void killEntity(LivingEntity entity) {

		// kill target
		entity.kill();
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
			return oe.get() instanceof PathfinderMob;
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
			return oe.get() instanceof Mob;
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
			return oe.get() instanceof Creeper;
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
			return oe.get() instanceof Parrot;
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
			return oe.get() instanceof Bee;
		return false;
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity entity object.
	 * 
	 * @return entity feet position (as a Y coordinate).
	 */
	public static double calculateEntityFeetPositition(Entity entity) {
		return entity.getY() - entity.getMyRidingOffset();
	}

	/**
	 * Calculate entity feet position (as a Y coordinate).
	 * 
	 * @param entity entity object.
	 * 
	 * @return entity feet position (as a Y coordinate).
	 */
	public static int calculateEntityFeetPosititionAsInt(Entity entity) {
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
	public static PlayerDirection getEntityDirection(Entity entity) {
		int direction = Mth.floor((double) ((entity.getXRot() * 4F) / 360F) + 0.5D) & 3;
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
			PathfinderMob creatureEntity = (PathfinderMob) entity;
			return Optional.ofNullable(creatureEntity.getTarget());
		}
		return Optional.ofNullable(entity.getLastHurtMob());
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
			PathfinderMob typedEntity = (PathfinderMob) entity;
			return typedEntity.getTarget();
		} else {
			return entity.getLastHurtMob();
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
			PathfinderMob creatureEntity = (PathfinderMob) entity;
			creatureEntity.setTarget(newTarget);
			return;
		}
		if (isTypeLivingEntity(entity)) {
			LivingEntity livingEntity = (LivingEntity) entity;
			livingEntity.setLastHurtMob(newTarget);
			livingEntity.setLastHurtByMob(newTarget);
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
		try {
			// if invoker is a player then get the player target.
			if (isTypePlayerEntity(invoker)) {

				// type cast
				Player player = (Player) invoker;

				// get player target
				TargetRepository repository = getProxy().getServerTargetRepository();
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

		} catch (Exception e) {
			getBassebombeCraft().reportAndLogException(e);

			// return no target, since we had an error
			return null;
		}

	}

	/**
	 * Set entity to be aggro'ed. Aggro'ing is only supported for
	 * {@linkplain MobEntity}.
	 * 
	 * @param entity entity to set aggro'ed if it is a {@linkplain MobEntity}.
	 */
	public static void setMobEntityAggroed(Entity entity) {
		if (isTypeMobEntity(entity)) {
			Mob mobEntity = (Mob) entity;
			mobEntity.setAggressive(true);
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
		entity.moveTo(positionX, positionY, positionZ, rotationYaw, PITCH);
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
	public static void selfDestruct(Mob entity) {
		entity.setSecondsOnFire(AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);
		entity.setHealth(0);
	}

	/**
	 * Self-destruct entity.
	 * 
	 * @param entity entity to self-destruct.
	 */
	public static void selfDestruct(LivingEntity entity) {
		entity.setSecondsOnFire(AI_COMMANDED_TEAM_MEMBER_SELFDESTRUCT_FIRE);
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
		double distSqr = entity.distanceToSqr(entity2);
		return (minDistanceSqr > distSqr);
	}

	/**
	 * set entity attribute with a double value.
	 * 
	 * If attribute isn't defined then it is registered with entity.
	 * 
	 * @param entity    entity to set attribute at.
	 * @param attribute attribute to set.
	 * @param value     value to set.
	 */
	public static void setAttribute(LivingEntity entity, Attribute attribute, double value) {
		AttributeInstance instance = entity.getAttribute(attribute);

		// handle undefined instance
		if (instance == null) {
			logUndefinedAttribute(entity, attribute);
			return;
		}

		instance.setBaseValue(value);
	}

	/**
	 * Returns true if custom entity attribute is set, e.g. has value 1.0D.
	 * 
	 * @param entity    entity to test attribute at.
	 * @param attribute attribute test set.
	 */
	public static boolean isEntityAttributeSet(LivingEntity entity, Attribute attribute) {
		AttributeInstance instance = entity.getAttribute(attribute);

		// handle undefined instance
		if (instance == null) {
			logUndefinedAttribute(entity, attribute);
			return false;
		}

		return (instance.getValue() == MARKER_ATTRIBUTE_IS_SET);

	}

	/**
	 * Log undefined attribute.
	 * 
	 * @param entity    entity to log attribute information for.
	 * @param attribute undefined attribute to log attribute information for.
	 */
	static void logUndefinedAttribute(LivingEntity entity, Attribute attribute) {
		BassebombeCraft mod = getBassebombeCraft();
		StringBuilder msg = new StringBuilder();
		msg.append("Failed to read value of undefined attribute: " + attribute.getDescriptionId());
		msg.append("Entity: " + entity);
		msg.append("Defined properties: ");

		AttributeMap manager = entity.getAttributes();
		manager.getDirtyAttributes().stream().forEach(i -> msg.append(i.getAttribute().getDescriptionId() + ", "));
		mod.reportAndLogError(msg.toString());
	}

}
