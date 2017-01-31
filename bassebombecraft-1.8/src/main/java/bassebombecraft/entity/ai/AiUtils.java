package bassebombecraft.entity.ai;

import java.util.Set;

import bassebombecraft.entity.ai.task.FollowClosestPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAIOcelotSit;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityOcelot;

/**
 * AI utility class.
 */
public class AiUtils {

	static final boolean DONT_CALL_FOR_HELP = false;
	static final boolean NEARBY_ONLY = true;
	static final boolean SHOULD_CHECK_SIGHT = true;
	static final double MOVEMENT_SPEED = 1.5D; // movement speed towards player
	static final float MINIMUM_DIST = 6.0F; // Entity minimum distance to player
	static final float WATCH_DIST = 10.0F;

	static EntityAiBuilder charmedMobAiBuilder = new CharmedMobAiBuilder();

	/**
	 * Clear passive and fighting AI tasks.
	 * 
	 * @param entity
	 *            to clear AI tasks for.
	 */
	public static void clearAiTasks(EntityLiving entity) {
		removeTasks(entity.tasks);
		removeTasks(entity.targetTasks);
	}

	/**
	 * Remove tasks in a concurrently safe way
	 * 
	 * @param tasks
	 *            AI tasks.
	 */
	static void removeTasks(EntityAITasks tasks) {
		Set<EntityAITaskEntry> entries = tasks.taskEntries;
		EntityAITaskEntry[] entriesArray = entries.toArray(new EntityAITaskEntry[entries.size()]);

		for (EntityAITaskEntry entry : entriesArray) {
			// get task
			EntityAIBase task = entry.action;

			// set to true to force removal
			entry.using = true;

			// remove
			tasks.removeTask(task);
		}
	}

	/**
	 * Assign passive AI tasks.
	 * 
	 * @param entity
	 *            to clear AI tasks for.
	 * @param entries
	 *            tasks entries.
	 */
	public static void assignAiTasks(EntityLiving entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.tasks.addTask(priority, task);
		}
	}

	/**
	 * Assign target AI tasks.
	 * 
	 * @param entity
	 *            to clear AI tasks for.
	 * @param entries
	 *            tasks entries.
	 */
	public static void assignAiTargetTasks(EntityLiving entity, Set<EntityAITaskEntry> entries) {
		for (EntityAITaskEntry entry : entries) {
			int priority = entry.priority;
			EntityAIBase task = entry.action;
			entity.targetTasks.addTask(priority, task);
		}
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity
	 *            entity which will configured with charmed AI.
	 */
	public static void buildCharmedMobAi(EntityLiving entity) {
		charmedMobAiBuilder.build(entity);
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity
	 *            entity which will configured with charmed AI.
	 * @param owner
	 *            entity which charmed mob.
	 */
	public static void buildCharmedMobAi(EntityLiving entity, EntityLivingBase owner) {
		charmedMobAiBuilder.build(entity, owner);
	}

	/**
	 * Build AI for kitten army.
	 * 
	 * @param entity
	 *            entity which will configured with kitten army AI.
	 */
	public static void buildKittenArmyAi(EntityOcelot entity) {

		entity.tasks.addTask(1, new EntityAISwimming(entity));
		entity.tasks.addTask(2, entity.getAISit());
		entity.tasks.addTask(3, new EntityAIFollowOwner(entity, 1.0D, 10.0F, 5.0F));
		entity.tasks.addTask(4, new EntityAIOcelotSit(entity, 0.8D));
		entity.tasks.addTask(5, new EntityAILeapAtTarget(entity, 0.3F));
		entity.tasks.addTask(6, new EntityAIOcelotAttack(entity));
		entity.tasks.addTask(7, new EntityAIWatchClosest(entity, EntityMob.class, WATCH_DIST));
		entity.tasks.addTask(3, new FollowClosestPlayer(entity, MINIMUM_DIST, MOVEMENT_SPEED));
		entity.tasks.addTask(4, new EntityAILookIdle(entity));

		EntityCreature entityCreature = EntityCreature.class.cast(entity);
		entity.targetTasks.addTask(1, new EntityAIHurtByTarget(entityCreature, DONT_CALL_FOR_HELP, new Class[0]));
		entity.targetTasks.addTask(2,
				new EntityAINearestAttackableTarget(entityCreature, EntityMob.class, SHOULD_CHECK_SIGHT, NEARBY_ONLY));
	}

}
