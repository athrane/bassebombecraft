package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ModConfiguration.spawnWarPigDamage;
import static bassebombecraft.config.ModConfiguration.spawnWarPigMovementSpeed;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of the {@linkplain Operator2} interface which spawn a war pig.
 * The pig is spawned at the invoker (e.g. the player).
 */
public class SpawnWarPig2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnWarPig2.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_PIG_HURT;

	/**
	 * Function to get invoker entity.
	 */
	Function<Ports, LivingEntity> fnGetInvoker;

	/**
	 * Function to get target entity.
	 */
	Function<Ports, LivingEntity> fnGetTarget;

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
	 * @param fnGetInvoker function to get invoker entity.
	 * @param fnGetTarget  function to get target entity.
	 */
	public SpawnWarPig2(Function<Ports, LivingEntity> fnGetInvoker, Function<Ports, LivingEntity> fnGetTarget) {
		this.fnGetInvoker = fnGetInvoker;
		this.fnGetTarget = fnGetTarget;
		this.damage = spawnWarPigDamage.get();
		this.movementSpeed = spawnWarPigMovementSpeed.get();
	}

	@Override
	public Ports run(Ports ports) {

		// get entities
		LivingEntity invoker = fnGetInvoker.apply(ports);
		LivingEntity target = fnGetTarget.apply(ports);

		// get world
		World world = ports.getWorld();

		// create entity
		Random random = getBassebombeCraft().getRandom();
		PigEntity entity = EntityType.PIG.create(world);
		entity.copyLocationAndAnglesFrom(invoker);

		// set entity attributes
		setAttribute(entity, MOVEMENT_SPEED, movementSpeed);
		setAttribute(entity, ATTACK_DAMAGE, damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, invoker);
		buildChargingAi(entity, entityTarget, (float) damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);

		return ports;
	}

}
