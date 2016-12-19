package bassebombecraft.entity.ai;

import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
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

		Set<EntityAITaskEntry> entries = entity.tasks.taskEntries;
		for (EntityAITaskEntry entry : entries) {
			EntityAIBase task = entry.action;
			entry.using = true; // to force removal;
			entity.tasks.removeTask(task);
		}

		entries = entity.targetTasks.taskEntries;
		for (EntityAITaskEntry entry : entries) {
			EntityAIBase task = entry.action;
			entry.using = true; // to force removal;
			entity.tasks.removeTask(task);
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
	 *            entity which will configured will charmed AI.
	 */
	public static void buildCharmedMobAi(EntityLiving entity) {
		charmedMobAiBuilder.build(entity);
	}

	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity
	 *            entity which will configured will charmed AI.
	 * @param owner
	 *            entity which charmed mob.
	 */
	public static void buildCharmedMobAi(EntityLiving entity, EntityLivingBase owner) {
		charmedMobAiBuilder.build(entity, owner);
	}

}
