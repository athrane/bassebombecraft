package bassebombecraft.item.action.inventory;

import static bassebombecraft.config.ModConfiguration.spawnAngryParrotsDamage;
import static bassebombecraft.config.ModConfiguration.spawnAngryParrotsMovementSpeed;
import static bassebombecraft.entity.EntityUtils.isTypeParrotEntity;
import static bassebombecraft.entity.EntityUtils.resolveTarget;
import static bassebombecraft.entity.EntityUtils.setAttribute;
import static bassebombecraft.entity.ai.AiUtils.buildChargingAi;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;
import static net.minecraft.world.entity.ai.attributes.Attributes.FLYING_SPEED;

import bassebombecraft.BassebombeCraft;
import java.util.Random;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
	static final SoundEvent SOUND = SoundEvents.PARROT_FLY;

	/**
	 * Parrot damage.
	 */
	final float damage;

	/**
	 * Parrot movement speed.
	 */
	final double movementSpeed;

	/**
	 * Constructor.
	 */
	public SpawnAngryParrots() {
		damage = spawnAngryParrotsDamage.get();
		movementSpeed = spawnAngryParrotsMovementSpeed.get();
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
	public void applyEffect(LivingEntity target, Level world, LivingEntity invoker) {

		// don't apply when target is a parrot
		if (isTypeParrotEntity(target))
			return;

		// create entity
		Random random = BassebombeCraft.getBassebombeCraft().getRandom();
		Parrot entity = EntityType.PARROT.create(world);
		entity.copyPosition(invoker);
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
		world.addFreshEntity(entity);
	}

}
