package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.config.ModConfiguration.aggroMobEffectAreaOfEffect;
import static bassebombecraft.config.ModConfiguration.aggroMobEffectUpdateFrequency;
import static bassebombecraft.entity.EntityUtils.isTypeCreatureEntity;
import static bassebombecraft.entity.EntityUtils.setMobEntityAggroed;
import static bassebombecraft.entity.EntityUtils.setTarget;
import static bassebombecraft.entity.EntityUtils.supportTargeting;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;

import java.util.Collections;
import java.util.List;

import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.util.function.DiscardSelf;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.phys.AABB;

/**
 * Effect which make the entity with the effect, "aggro" any mob, e.g. attack
 * the mob on sight.
 * 
 * The effect has no effect on the player.
 */
public class AggroMobEffect extends MobEffect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = AggroMobEffect.class.getSimpleName();

	/**
	 * First list index.
	 */
	static final int FIRST_INDEX = 0;

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
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {

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
			PathfinderMob entityCreature = (PathfinderMob) entity;
			target = entityCreature.getTarget();
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			target = entityLiving.getLastHurtMob();
		}

		// exit if target is defined and isn't dead
		if ((target != null) && (!target.isAlive()))
			return;

		// initialize filter
		discardSelfFilter.set(entity);

		// get list of mobs
		int arreaOfEffect = aggroMobEffectAreaOfEffect.get();
		AABB aabb = entity.getBoundingBox().inflate(arreaOfEffect, arreaOfEffect, arreaOfEffect);
		List<LivingEntity> targetList = entity.level.getEntitiesOfClass(LivingEntity.class, aabb, discardSelfFilter);

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
	public boolean isDurationEffectTick(int duration, int amplifier) {
		int updateFrequency = aggroMobEffectUpdateFrequency.get();
		int moduloValue = duration % updateFrequency;
		return (moduloValue == 0);
	}

}
