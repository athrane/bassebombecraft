package bassebombecraft.item.action.inventory;

import static bassebombecraft.entity.EntityUtils.isTypeParrotEntity;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.entity.SharedMonsterAttributes.ATTACK_DAMAGE;
import static net.minecraft.entity.SharedMonsterAttributes.FLYING_SPEED;
import static net.minecraft.entity.SharedMonsterAttributes.MOVEMENT_SPEED;

import java.util.Random;
import java.util.function.Supplier;

import bassebombecraft.BassebombeCraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

/**
 * Implementation of {@linkplain InventoryItemActionStrategy} for construction
 * of inventory item actions. This class spawns angry parrots.
 */
public class SpawnAngryParrots implements InventoryItemActionStrategy {

	/**
	 * Action identifier.
	 */
	public static final String NAME = SpawnAngryParrots.class.getSimpleName();

	/**
	 * Spawn sound.
	 */
	static final SoundEvent SOUND = SoundEvents.ENTITY_PARROT_FLY;

	/**
	 * Parrot damage.
	 */
	final float damage;

	/**
	 * Parrot movement speed.
	 */
	final double movementSpeed;

	/**
	 * SpawnAngryParrots constructor.
	 * 
	 * @param splDamage        parrot attack damage.
	 * @param splMovementSpeed bee movement damage.
	 */
	public SpawnAngryParrots(Supplier<Integer> splDamage, Supplier<Double> splMovementSpeed) {
		damage = splDamage.get();
		movementSpeed = splMovementSpeed.get();
	}

	@Override
	public boolean applyOnlyIfSelected() {
		return true;
	}

	@Override
	public boolean shouldApplyEffect(Entity target, boolean targetIsInvoker) {
		if (targetIsInvoker)
			return false;
		return true;
	}

	@Override
	public void applyEffect(Entity target, World world, LivingEntity invoker) {

		// don't apply when target is a parrot
		if (isTypeParrotEntity(target))
			return;

		// create entity
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		ParrotEntity entity = EntityType.PARROT.create(world);
		entity.copyLocationAndAnglesFrom(invoker);
		entity.setVariant(random.nextInt(5));

		// set entity attributes
		setAttribute(entity, FLYING_SPEED, movementSpeed);
		setAttribute(entity, MOVEMENT_SPEED, movementSpeed);
		setAttribute(entity, ATTACK_DAMAGE, damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, invoker);
		buildChargingAi(entity, entityTarget, damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}

}
