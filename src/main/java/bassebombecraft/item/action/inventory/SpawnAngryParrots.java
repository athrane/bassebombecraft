package bassebombecraft.item.action.inventory;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.entity.EntityUtils.isTypeParrotEntity;
import static bassebombecraft.entity.ai.AiUtils.buildParrotAi;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import javax.naming.OperationNotSupportedException;

import bassebombecraft.BassebombeCraft;
import bassebombecraft.event.entity.target.TargetedEntitiesRepository;
import bassebombecraft.event.particle.ParticleRenderingInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
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
	public final static String NAME = SpawnAngryParrots.class.getSimpleName();

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
	 * @param splDamage parrot attack damage..
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
		entity.getAttributes().getAttributeInstance(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(movementSpeed);
		entity.getAttributes().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(movementSpeed);
		entity.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(damage);

		// set AI
		LivingEntity entityTarget = resolveTarget(target, entity, invoker);
		buildParrotAi(entity, entityTarget, damage);

		// add spawn sound
		entity.playSound(SOUND, 0.5F, 0.4F / random.nextFloat() * 0.4F + 0.8F);

		// spawn
		world.addEntity(entity);
	}

	/**
	 * Resolve the bat target.
	 * 
	 * @param target    some nearby mob.
	 * @param batEntity bat entity
	 * @param commander invoker of the effect.
	 * 
	 * @return resolved target.
	 */
	LivingEntity resolveTarget(Entity target, MobEntity batEntity, LivingEntity invoker) {

		// if invoker is a player then get the player target.
		if (isTypePlayerEntity(invoker)) {

			// type cast
			PlayerEntity player = (PlayerEntity) invoker;

			// get player target
			TargetedEntitiesRepository repository = getBassebombeCraft().getTargetedEntitiesRepository();
			Optional<LivingEntity> optTarget = repository.getFirst(player);

			// return player target if defined
			if (optTarget.isPresent())
				return optTarget.get();
		}

		// if target is living entity then cast cast and return it
		if (isTypeLivingEntity(target)) {

			// type cast
			LivingEntity livingEntity = (LivingEntity) target;

			return livingEntity;
		}

		return null;
	}

	@Override
	public int getEffectRange() throws OperationNotSupportedException {
		throw new OperationNotSupportedException(); // to signal that this method should not be used.
	}

	@Override
	public ParticleRenderingInfo[] getRenderingInfos() throws OperationNotSupportedException {
		throw new OperationNotSupportedException(); // to signal that this method should not be used.
	}

}
