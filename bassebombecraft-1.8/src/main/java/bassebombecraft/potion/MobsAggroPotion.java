package bassebombecraft.potion;

import static bassebombecraft.BassebombeCraft.getBassebombeCraft;
import static bassebombecraft.ModConstants.NOT_BAD_POTION_EFFECT;
import static bassebombecraft.ModConstants.POTION_LIQUID_COLOR;
import static bassebombecraft.entity.EntityUtils.isEntityCreature;
import static bassebombecraft.entity.EntityUtils.isLivingEntity;

import java.util.Collections;
import java.util.List;

import com.typesafe.config.Config;

import bassebombecraft.entity.EntityDistanceSorter;
import bassebombecraft.predicate.DiscardSelf;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
/**
 * Potion which make a mob aggro any entity, e.g. would attack it on sight.
 */
public class MobsAggroPotion extends Potion {

	/**
	 * First list index.
	 */
	static final int FIRST_INDEX = 0;

	/**
	 * Configuration key.
	 */
	final static String CONFIG_KEY = MobsAggroPotion.class.getSimpleName();

	/**
	 * Target distance.
	 */
	final int targetDistance;

	/**
	 * Entity distance sorter.
	 */
	EntityDistanceSorter entityDistanceSorter = new EntityDistanceSorter();

	/**
	 * Discard self filter.
	 */
	DiscardSelf discardSelfFilter = new DiscardSelf();
	
	/**
	 * MobAggroPotion constructor.
	 */
	public MobsAggroPotion() {
		super(NOT_BAD_POTION_EFFECT, POTION_LIQUID_COLOR);
		Config configuration = getBassebombeCraft().getConfiguration();
		targetDistance = configuration.getInt(CONFIG_KEY + ".TargetDistance");
	}

	@Override
	public void performEffect(LivingEntity entity, int magicNumber) {

		// exit if entity is undefined
		if (entity == null)
			return;

		// exit if entity isn't a creature to support targeting		
		if(!supportTargeting(entity)) return;
		
		// get target (either as creature or living entity)
		LivingEntity target  = null;
		if(isEntityCreature(entity)) {
			EntityCreature entityCreature = (EntityCreature) entity;
			target = entityCreature.getAttackTarget();			
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			target = entityLiving.getAttackTarget();						
		}
				
		// exit if target is defined and isn't dead
		if ((target != null) && (!target.isDead))
			return;

		// initialize filter
		discardSelfFilter.set(entity);
		
		// get list of mobs
		AxisAlignedBB aabb = entity.getEntityBoundingBox().expand(targetDistance, targetDistance, targetDistance);
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
		if(isEntityCreature(entity)) {
			EntityCreature entityCreature = (EntityCreature) entity;
			entityCreature.setAttackTarget(newTarget);			
		} else {
			LivingEntity entityLiving = (LivingEntity) entity;
			entityLiving .setAttackTarget(newTarget);			
		}
	}

	boolean supportTargeting(LivingEntity entity) {
		if(isEntityCreature(entity)) return true;
		if(isLivingEntity(entity)) return true;
		return false;
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

}
