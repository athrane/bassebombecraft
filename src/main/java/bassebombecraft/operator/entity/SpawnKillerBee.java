package bassebombecraft.operator.entity;

import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE;
import static net.minecraft.entity.SharedMonsterAttributes.FLYING_SPEED;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator} interface which spawn a killer
 * bee. The bee is spawned at the invoker (e.g. the player).
 */
public class SpawnKillerBee implements Operator {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnKillerBee.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_PARROT_FLY;

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Entity target supplier.
	 */
	Supplier<Entity> splTarget;

	/**
	 * Movement speed supplier.
	 */
	IntSupplier splDamage;

	/**
	 * Attack damage supplier.
	 */
	DoubleSupplier splMovementSpeed;

	/**
	 * SpawnKillerBees constructor. S
	 * 
	 * @param splEntity        invoker entity supplier.
	 * @param splTarget        target entity supplier.
	 * @param splMovementSpeed movement speed supplier.
	 * @param splDamage        attack damage supplier.
	 */
	public SpawnKillerBee(Supplier<LivingEntity> splEntity, Supplier<Entity> splTarget, IntSupplier splDamage,
			DoubleSupplier splMovementSpeed) {
		this.splEntity = splEntity;
		this.splTarget = splTarget;
		this.splDamage = splDamage;
		this.splMovementSpeed = splMovementSpeed;
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
		BeeEntity entity = EntityType.BEE.create(world);
		entity.copyLocationAndAnglesFrom(livingEntity);

		// set entity attributes
		setAttribute(entity, FLYING_SPEED, splMovementSpeed.getAsDouble());
		setAttribute(entity, MOVEMENT_SPEED, splMovementSpeed.getAsDouble());
		setAttribute(entity, ATTACK_DAMAGE, splDamage.getAsInt());

		// set AI
		LivingEntity entityTarget = resolveTarget(target, livingEntity);
		buildChargingAi(entity, entityTarget, (float) splDamage.getAsInt());

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}

}
