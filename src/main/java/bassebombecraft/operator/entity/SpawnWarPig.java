package bassebombecraft.operator.entity;

import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.ai.AiUtils.buildWarPigAi;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator} interface which spawn a war pig.
 * The pig is spawned at the invoked (e.g. the player).
 */
public class SpawnWarPig implements Operator {

	/**
	 * Operator identifier.
	 */
	public final static String NAME = SpawnWarPig.class.getSimpleName();

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
	 * Movement speed supplier.
	 */
	Supplier<Integer> splDamage;

	/**
	 * Attack damage supplier.
	 */
	Supplier<Double> splMovementSpeed;

	/**
	 * SpawnKillerBees constructor. S
	 * 
	 * @param splEntity        invoker entity supplier.
	 * @param splTarget        target entity supplier.
	 * @param splMovementSpeed movement speed supplier.
	 * @param splDamage        attack damage supplier.
	 */
	public SpawnWarPig(Supplier<LivingEntity> splEntity, Supplier<Entity> splTarget, Supplier<Integer> splDamage,
			Supplier<Double> splMovementSpeed) {
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
		PigEntity entity = EntityType.PIG.create(world);
		entity.copyLocationAndAnglesFrom(livingEntity);

		// set entity attributes
		AbstractAttributeMap attributes = entity.getAttributes();
		attributes.getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(splMovementSpeed.get());
		attributes.registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(splDamage.get());

		// set AI
		LivingEntity entityTarget = resolveTarget(target, entity, livingEntity);
		buildWarPigAi(entity, entityTarget, (float) splDamage.get());

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}
}
