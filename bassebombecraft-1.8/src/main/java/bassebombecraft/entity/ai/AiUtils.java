package bassebombecraft.entity.ai;

import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;

/**
 * AI utility class.
 */
public class AiUtils {

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
	 * @param tasks AI tasks.
	 */
	static void removeTasks(EntityAITasks tasks) {		
		Set<EntityAITaskEntry> entries = tasks.taskEntries;				
		EntityAITaskEntry[] entriesArray = entries.toArray(new EntityAITaskEntry[entries.size()]);		
		
		for(EntityAITaskEntry entry : entriesArray) {
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

}
