package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.entity.EntityUtils.setMobEntityAggroed;
import static bassebombecraft.entity.EntityUtils.setTarget;
import static bassebombecraft.entity.EntityUtils.supportTargeting;
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
 * Effect which make the entity with the effect, "aggro" any mob, e.g. attack the mob on sight.
 * 
 * The effect has no effect on the player.
 */
public class AggroMobEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = AggroMobEffect.class.getSimpleName();

	/**
	 * Update frequency for effect.
	 */
	int updateFrequency;
	
	/**
	 * First list index.
	 */
	static final int FIRST_INDEX = 0;

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
	 * Constructor.
	 */
	public AggroMobEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		arreaOfEffect = ModConfiguration.aggroMobEffectAreaOfEffect.get();
		updateFrequency = ModConfiguration.aggroMobEffectUpdateFrequency.get();
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

		// set target
		setTarget(entity, newTarget);

		// set mob aggro'ed
		setMobEntityAggroed(entity);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int moduloValue = duration % updateFrequency; 
		return (moduloValue == 0);
	}

}
