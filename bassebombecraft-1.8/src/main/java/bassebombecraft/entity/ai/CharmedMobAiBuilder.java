package bassebombecraft.entity.ai;

import bassebombecraft.entity.ai.task.CompanionAttack;
import bassebombecraft.entity.ai.task.FollowClosestPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;

/**
 * Implementation of {@linkplain EntityAiBuilder} interface for 
 * creation of AI for charmed mob and guardian.
 */
@Deprecated
public class CharmedMobAiBuilder implements EntityAiBuilder {

	static final boolean DONT_CALL_FOR_HELP = false;
	static final boolean NEARBY_ONLY = true;
	static final boolean SHOULD_CHECK_SIGHT = true;
	static final double MOVEMENT_SPEED = 1.5D; // movement speed towards player
	static final float MINIMUM_DIST = 6.0F; // Entity minimum distance to player	
	static final float WATCH_DIST = 10.0F;
	
	@Override
	public void build(EntityLiving entity) {
		
		// set tasks
		entity.tasks.addTask(0, new EntityAISwimming(entity));
		entity.tasks.addTask(1, new CompanionAttack(entity));
		entity.tasks.addTask(2, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));		
		entity.tasks.addTask(3, new FollowClosestPlayer(entity, MINIMUM_DIST, MOVEMENT_SPEED));
		entity.tasks.addTask(4, new EntityAILookIdle(entity));

		// set targeting task
		if (entity instanceof EntityCreature) {
			EntityCreature entityCreature = EntityCreature.class.cast(entity);
			entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));						
			entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityCreature, EntityMob.class,
					SHOULD_CHECK_SIGHT, NEARBY_ONLY));
		} else {
			entity.targetTasks.addTask(1, new EntityAIFindEntityNearest(entity, EntityMob.class));
		}		
	}

	@Override
	public void build(EntityLiving entity, EntityLivingBase owner) {
		
		// set tasks
		entity.tasks.addTask(0, new EntityAISwimming(entity));
		entity.tasks.addTask(1, new CompanionAttack(entity));
		entity.tasks.addTask(2, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));		
		entity.tasks.addTask(3, new FollowClosestPlayer(entity, MINIMUM_DIST, MOVEMENT_SPEED));
		entity.tasks.addTask(4, new EntityAILookIdle(entity));

		// set targeting task
		if (entity instanceof EntityCreature) {
			EntityCreature entityCreature = EntityCreature.class.cast(entity);
			entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));						
			entity.targetTasks.addTask(2, new EntityAINearestAttackableTarget(entityCreature, EntityLiving.class,
					SHOULD_CHECK_SIGHT, NEARBY_ONLY));
		} else {
			entity.targetTasks.addTask(1, new EntityAIFindEntityNearest(entity, EntityMob.class));
		}
		
	
	}	
	
}
