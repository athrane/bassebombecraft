package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.spawnWarPigDamage;
import static bassebombecraft.config.ModConfiguration.spawnWarPigMovementSpeed;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator} interface which spawn a war pig.
 * The pig is spawned at the invoker (e.g. the player).
 */
public class SpawnWarPig implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnWarPig.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_PIG_HURT;

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Entity target supplier.
	 */
	Supplier<Entity> splTarget;

	/**
	 * Attack damage.
	 */
	int damage;

	/**
	 * Movement speed.
	 */
	double movementSpeed;

	/**
	 * Constructor.
	 * 
	 * @param splEntity invoker entity supplier.
	 * @param splTarget target entity supplier.
	 */
	public SpawnWarPig(Supplier<LivingEntity> splEntity, Supplier<Entity> splTarget) {
		this.splEntity = splEntity;
		this.splTarget = splTarget;
		this.damage = spawnWarPigDamage.get();
		this.movementSpeed = spawnWarPigMovementSpeed.get();
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// get target
		Entity target = splTarget.get();

		// get world
		World world = livingEntity.world;

		// create entity
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		PigEntity entity = EntityType.PIG.create(world);
		entity.copyLocationAndAnglesFrom(livingEntity);

		// set entity attributes
		setAttribute(entity, MOVEMENT_SPEED, movementSpeed);
		setAttribute(entity, ATTACK_DAMAGE, damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, livingEntity);
		buildChargingAi(entity, entityTarget, (float) damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}
}
