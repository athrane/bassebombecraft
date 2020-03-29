package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.DECOY;
import static bassebombecraft.block.BlockUtils.calculatePosition;
import static bassebombecraft.config.ModConfiguration.spawnDecoyKnockBackResistance;
import static bassebombecraft.config.ModConfiguration.spawnDecoyMaxHealth;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.projectile.ProjectileUtils.isBlockHit;
import static bassebombecraft.projectile.ProjectileUtils.isEntityHit;
import static bassebombecraft.projectile.ProjectileUtils.isNothingHit;
import static bassebombecraft.projectile.ProjectileUtils.isTypeBlockRayTraceResult;
import static bassebombecraft.projectile.ProjectileUtils.isTypeEntityRayTraceResult;
import static net.minecraft.entity.SharedMonsterAttributes.KNOCKBACK_RESISTANCE;
import static net.minecraft.entity.SharedMonsterAttributes.MAX_HEALTH;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator} interface which spawn a decoy (2D
 * Panda). The entity is spawned at the invoker (e.g. the player).
 */
public class SpawnDecoy implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnDecoy.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_DOLPHIN_PLAY;

	/**
	 * LivingEntity (for outbound port).
	 */
	LivingEntity livingEntity;

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * RayTraceResult supplier.
	 */
	Supplier<RayTraceResult> splRayTraceResult;

	/**
	 * {@linkplain LivingEntity} supplier.
	 */
	Supplier<LivingEntity> splLivingEntity = () -> livingEntity;

	/**
	 * Constructor.
	 * 
	 * @param splEntity         invoker entity supplier.
	 * @param splRayTraceResult projectile ray trace result.
	 */
	public SpawnDecoy(Supplier<LivingEntity> splEntity, Supplier<RayTraceResult> splRayTraceResult) {
		this.splEntity = splEntity;
		this.splRayTraceResult = splRayTraceResult;
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// get ray trace result
		RayTraceResult result = splRayTraceResult.get();

		// exit if nothing was hit
		if (isNothingHit(result))
			return;

		// declare
		BlockPos spawnPosition = null;

		// spawn at hit entity
		if (isEntityHit(result)) {

			// exit if result isn't entity ray trace result;
			if (!isTypeEntityRayTraceResult(result))
				return;

			// get entity
			Entity entity = ((EntityRayTraceResult) result).getEntity();

			// get position
			spawnPosition = entity.getPosition();
		}

		// spawn at hit block
		if (isBlockHit(result)) {

			// exit if result isn't entity ray trace result
			if (!isTypeBlockRayTraceResult(result))
				return;

			// type cast
			BlockRayTraceResult blockResult = (BlockRayTraceResult) result;

			// get block position
			spawnPosition = calculatePosition(blockResult);
		}

		// get world
		World world = livingEntity.world;

		// create entity
		PandaEntity entity = EntityType.PANDA.create(world);
		entity.setLocationAndAngles(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(),
				livingEntity.rotationYaw, livingEntity.rotationPitch);

		// set entity attributes
		setAttribute(entity, MOVEMENT_SPEED, 0);
		setAttribute(entity, MAX_HEALTH, spawnDecoyMaxHealth.get());
		setAttribute(entity, KNOCKBACK_RESISTANCE, spawnDecoyKnockBackResistance.get());
		setAttribute(entity, DECOY, 1.0D);

		// set AI
		// buildChargingAi(entity, entityTarget, (float) damage);

		// add spawn sound
		Random random = getBassebombeCraft().getRandom();
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);

		this.livingEntity = entity;
	}

	/**
	 * Get {@linkplain LivingEntity} supplier.
	 * 
	 * Defines an outbound port.
	 * 
	 * @return living entity supplier.
	 */
	public Supplier<LivingEntity> getSplLivingEntity() {
		return splLivingEntity;
	}

}
