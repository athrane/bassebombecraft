package bassebombecraft.operator.entity;

import static bassebombecraft.config.ModConfiguration.spawnKillerBeeDamage;
import static bassebombecraft.config.ModConfiguration.spawnKillerBeeMovementSpeed;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.FLYING_SPEED;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.operator.Operator;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

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
	static final SoundEvent SOUND = SoundEvents.PARROT_FLY;

	/**
	 * Entity supplier.
	 */
	Supplier<LivingEntity> splEntity;

	/**
	 * Entity target supplier.
	 */
	Supplier<LivingEntity> splTarget;

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
	public SpawnKillerBee(Supplier<LivingEntity> splEntity, Supplier<LivingEntity> splTarget) {
		this.splEntity = splEntity;
		this.splTarget = splTarget;
		this.damage = spawnKillerBeeDamage.get();
		this.movementSpeed = spawnKillerBeeMovementSpeed.get();
	}

	@Override
	public void run() {

		// get entity
		LivingEntity livingEntity = splEntity.get();

		// get target
		Entity target = splTarget.get();

		// get world
		Level world = livingEntity.level;

		// create entity
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		Bee entity = EntityType.BEE.create(world);
		entity.copyPosition(livingEntity);

		// set entity attributes
		setAttribute(entity, FLYING_SPEED, movementSpeed);
		setAttribute(entity, MOVEMENT_SPEED, movementSpeed);
		setAttribute(entity, ATTACK_DAMAGE, damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, livingEntity);
		buildChargingAi(entity, entityTarget, (float) damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addFreshEntity(entity);
	}

}
