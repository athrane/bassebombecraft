package bassebombecraft.entity.ai;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityLiving;

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
		entity.tasks.taskEntries = Lists.newArrayList();
		entity.targetTasks.taskEntries = Lists.newArrayList();
	}
	
	/**
	 * Build AI for charmed mob.
	 * 
	 * @param entity entity which will configured will charmed AI.
	 */
	public static void buildCharmedMobAi(EntityLiving entity) {
		charmedMobAiBuilder.build(entity);
	}
}
