package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.entity.EntityUtils.isTypeLivingEntity;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.Collections;
import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.predicate.DiscardSelf;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Effect which make a mob aggro any entity, e.g. would attack it on sight.
 * 
 * The effect has no effect on the player.
 */
public class MobAggroEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public final static String NAME = MobAggroEffect.class.getSimpleName();

	/**
	 * First list index.
	 */
	final static int FIRST_INDEX = 0;

	/**
	 * Area of effect.
	 */
	final int arreaOfEffect;

	/**
	 * Entity distance sorter.
	 */
	EntityDistanceSorter entityDistanceSorter = new EntityDistanceSorter();

	/**
	 * Discard self filter.
	 */
	DiscardSelf discardSelfFilter = new DiscardSelf();

	/**
	 * MobAggroEffect constructor.
	 */
	public MobAggroEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		arreaOfEffect = ModConfiguration.mobAggroEffectAreaOfEffect.get();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// exit if entity isn't a creature to support targeting
		if (!supportTargeting(entity))
			return;

		// get target (either as creature or living entity)
		LivingEntity target = null;
		if (isTypeCreatureEntity(entity)) {
			CreatureEntity entityCreature = (CreatureEntity) entity;
			target = entityCreature.getAttackTarget();
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			target = entityLiving.getLastAttackedEntity();
		}

		// exit if target is defined and isn't dead
		if ((target != null) && (!target.isAlive()))
			return;

		// initialize filter
		discardSelfFilter.set(entity);

		// get list of mobs
		AxisAlignedBB aabb = entity.getBoundingBox().expand(arreaOfEffect, arreaOfEffect, arreaOfEffect);
		List<LivingEntity> targetList = entity.world.getEntitiesWithinAABB(LivingEntity.class, aabb, discardSelfFilter);

		// exit if no targets where found
		if (targetList.isEmpty())
			return;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targetList, entityDistanceSorter);

		// get new target
		LivingEntity newTarget = targetList.get(FIRST_INDEX);

		// update target (either as creature or living entity)
		if (isTypeCreatureEntity(entity)) {
			CreatureEntity entityCreature = (CreatureEntity) entity;
			entityCreature.setAttackTarget(newTarget);
		} else {
			entity.setLastAttackedEntity(newTarget);
			entity.setRevengeTarget(newTarget);
		}
	}

	boolean supportTargeting(LivingEntity entity) {
		if (isTypeCreatureEntity(entity))
			return true;
		if (isTypeLivingEntity(entity))
			return true;
		return false;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
