package bassebombecraft.potion.effect;

import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.setMobEntityAggroed;
import static bassebombecraft.entity.EntityUtils.setTarget;
import static bassebombecraft.player.PlayerUtils.isTypePlayerEntity;
import static bassebombecraft.potion.PotionUtils.doCommonEffectInitialization;

import java.util.Collections;
import java.util.List;

import bassebombecraft.config.ModConfiguration;
import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.predicate.DiscardSelf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Effect which make the entity with the effect, a target for by any mob, e.g.
 * the mob will "aggro" and attack the entity on sight.
 * 
 * The effect has no effect on the player.
 */
public class ReceiveAggroEffect extends Effect {

	/**
	 * Effect identifier.
	 */
	public static final String NAME = ReceiveAggroEffect.class.getSimpleName();

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
	public ReceiveAggroEffect() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		doCommonEffectInitialization(this, NAME);
		arreaOfEffect = ModConfiguration.receiveAggroEffectAreaOfEffect.get();
		updateFrequency = ModConfiguration.receiveAggroEffectUpdateFrequency.get();
	}

	@Override
	public void performEffect(LivingEntity entity, int amplifier) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity is player
		if (isTypePlayerEntity(entity))
			return;

		// initialize filter
		discardSelfFilter.set(entity);

		// get list of mobs
		AxisAlignedBB aabb = entity.getBoundingBox().expand(arreaOfEffect, arreaOfEffect, arreaOfEffect);
		List<LivingEntity> targebtList = entity.world.getEntitiesWithinAABB(LivingEntity.class, aabb,
				discardSelfFilter);

		// exit if no targets where found
		if (targebtList.isEmpty())
			return;

		// sort mobs
		entityDistanceSorter.setEntity(entity);
		Collections.sort(targebtList, entityDistanceSorter);

		// set this entity as the mob target and set mob aggro'ed
		targebtList.stream().forEach(m -> {
			setTarget(m, entity);
			setMobEntityAggroed(m);
		});
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int moduloValue = duration % updateFrequency;
		return (moduloValue == 0);
	}

	
	
}
