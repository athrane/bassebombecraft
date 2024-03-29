package bassebombecraft.operator.entity;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.config.ModConfiguration.spawnWarPigDamage;
import static bassebombecraft.config.ModConfiguration.spawnWarPigMovementSpeed;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static bassebombecraft.operator.Operators2.applyV;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Function;

import bassebombecraft.event.entity.attribute.EntityAttributeEventHandler;
import bassebombecraft.operator.Operator2;
import bassebombecraft.operator.Ports;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;

/**
 * Implementation of the {@linkplain Operator2} interface which spawn a war pig.
 * The pig is spawned at the invoker (e.g. the player).
 * 
 * The vanilla attribute {@linkplain Attributes.ATTACK_DAMAGE} is assigned to
 * the {@linkplain PigEntity} entity in the event handler
 * {@linkplain EntityAttributeEventHandler} to support the value assignment in
 * this operator.
 */
public class SpawnWarPig2 implements Operator2 {

	/**
	 * Operator identifier.
	 */
	public static final String NAME = SpawnWarPig2.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.PIG_HURT;

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
	public void run(Ports ports) {
		LivingEntity invoker = applyV(fnGetInvoker, ports);
		LivingEntity target = applyV(fnGetTarget, ports);

		// get world
		Level world = invoker.getCommandSenderWorld();

		// create entity
		Random random = getBassebombeCraft().getRandom();
		Pig entity = EntityType.PIG.create(world);
		entity.copyPosition(invoker);

		// set entity attributes
		setAttribute(entity, MOVEMENT_SPEED, movementSpeed);
		setAttribute(entity, ATTACK_DAMAGE, damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, invoker);
		buildChargingAi(entity, entityTarget, (float) damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addFreshEntity(entity);
	}

}
